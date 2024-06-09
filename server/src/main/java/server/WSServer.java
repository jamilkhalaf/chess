package server;

import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import chess.ChessGame;
import chess.ChessMove;
import dataaccess.*;

import java.io.IOException;

@WebSocket
public class WSServer {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public WSServer(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("New client connected: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Received message: " + message);

        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            System.out.println("Command Type: " + command.getCommandType());

            switch (command.getCommandType()) {
                case MAKE_MOVE:
                    handleMakeMove(command, session);
                    break;
                case LEAVE:
                    break;
                case RESIGN:
                    handleResign(command,session);
                    break;
                case CONNECT:
                    handleConnect(command, session);
                    break;
                default:
                    System.out.println("Unknown command type: " + command.getCommandType());
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void handleResign(UserGameCommand command, Session session) throws Exception {
        Integer gameID = command.getGameID();
        ChessMove move = command.getMove();
        System.out.println("Handling make move command: " + command);

        try {
            SQLGameDAO gameDao = new SQLGameDAO();
            gameDao.updateGame(command.playerColor, command.gameID, command.getUsername());
        } catch (DataAccessException e) {
            sendErrorMessage(command,session, "error");
            return;
        }
        sendResignMessage(command,session,command.playerColor + " resigned");

    }

    private void handleMakeMove(UserGameCommand command, Session session) throws Exception {
        Integer gameID = command.getGameID();
        ChessMove move = command.getMove();
        System.out.println("Handling make move command: " + command);

        try {
            SQLGameDAO gameDao = new SQLGameDAO();
            gameDao.makeChessMove(move, gameID);
        } catch (DataAccessException e) {
            sendErrorMessage(command,session, "error");
            return;
        }

        ChessGame game = gameDAO.getGameData(gameID).getGame();

        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            sendWinningMessage(gameID, "WHITE");
        } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            sendWinningMessage(gameID, "BLACK");
        } else if (game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            sendWinningMessage(gameID, "Draw");
        } else {
            sendMoveMessage(command,session,"Move was made");
        }
    }

    private void handleConnect(UserGameCommand command, Session session) throws Exception {
            SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
            String username = sqlAuthDAO.getUsername(command.getAuthToken());
            boolean hasObserver = false;
            boolean hasBlack = false;
            boolean hasWhite = false;
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData data = gameDAO.getGameData(command.gameID);

            if (username == null) {
                sendErrorMessage(command, session, "Game not found");
                return;
            }

            if (gameDAO.getGame(command.gameID) == null) {
                sendErrorMessage(command, session, "Game not found");
                return;
            }

            if ((data.getSpectators() == null)){
                hasObserver = true;
            }
            System.out.println(hasObserver);

            if (data.getWhiteUsername() != null) {
                hasWhite = true;
            }

            if (data.getBlackUsername() != null) {
                hasBlack = true;
            }

            if ((hasBlack && data.getBlackUsername().equals(username)) || (hasWhite && data.getWhiteUsername().equals(username)) || (hasObserver) ) {
                WSSessions.addSession(command.getGameID(), command.getAuthToken(), session);
                ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, data.getGame());
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));
                String joinMessageContent = username + " has joined the game.";
                sendJoinMessage(command, command.getAuthToken(), joinMessageContent);
                return;
            }


            sendErrorMessage(command, session, "Color Taken");




    }

    private void sendObserveMessage(UserGameCommand command, Session session, String errorMessage) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, command.getGameID(), errorMessage);
        try {
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendJoinMessage(UserGameCommand command, String excludeAuthToken, String messageContent) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, messageContent);
        try {
            WSSessions.broadcastSession(command.getGameID(), excludeAuthToken, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendErrorMessage(UserGameCommand command, Session session, String errorMessage) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, command.getGameID(), errorMessage);
        try {
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResignMessage(UserGameCommand command, Session session, String errorMessage) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, command.getGameID(), errorMessage);
        try {
            WSSessions.broadcastSession(command.getGameID(), null, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMoveMessage(UserGameCommand command, Session session, String errorMessage) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, command.getGameID(), errorMessage);
        try {
            WSSessions.broadcastSession(command.getGameID(), null, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendWinningMessage(Integer gameID, String winner) throws Exception {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.WINNINGPLAYER, winner, gameID);
        WSSessions.broadcastSession(gameID, null, message);
        System.exit(0);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace(System.err);
    }
}
