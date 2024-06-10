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
    private static boolean ended = false;

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
                    handleLeave(command,session);
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

    public static void handleConnect(UserGameCommand command, Session session) throws Exception {
        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        GameData data = sqlGameDAO.getGameData(gameID);
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        String username = sqlAuthDAO.getUsername(authToken);


        if (data == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        String observer = data.getSpectators();

        if (username == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (sqlGameDAO.getGame(gameID) == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid gameID");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (username.equals(data.getWhiteUsername()) || username.equals(data.getBlackUsername())) {
            WSSessions.addSession(command.getGameID(), authToken, session);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username +" joined", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
            return;
        }

        if ((observer == null) || observer.equals(username)) {
            WSSessions.addSession(command.getGameID(), authToken, session);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username +" joined as observer", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);

        }
        else {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid command");
            session.getRemote().sendString(new Gson().toJson(msg));
        }

    }

    public static void handleMakeMove(UserGameCommand command, Session session) throws Exception {
        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        String username = sqlAuthDAO.getUsername(authToken);
        ChessMove move = command.getMove();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        GameData data = sqlGameDAO.getGameData(gameID);

        if (username == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }
        try {

            if (ended) {
                ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Game ended, cannot make any more moves", gameID);
                WSSessions.broadcastSession(gameID,null, notificationMessage);
                return;
            }

            ChessGame game = sqlGameDAO.makeChessMove(move,gameID);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            WSSessions.broadcastSession(gameID,null, loadGameMessage);
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "move was made", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
            check(game,gameID);
        }
        catch (DataAccessException e) {
            if (ended) {
                ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Game ended, cannot make any more moves", gameID);
                WSSessions.broadcastSession(gameID,null, notificationMessage);
                return;
            }
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid move");
            session.getRemote().sendString(new Gson().toJson(msg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void check(ChessGame game, Integer gameID) throws Exception {
        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black in checkmate, White won", gameID);
            WSSessions.broadcastSession(gameID,null, notificationMessage);
            ended = true;
            return;
        }
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White in checkmate, Black won", gameID);
            WSSessions.broadcastSession(gameID,null, notificationMessage);
            ended = true;
            return;

        }
        if (game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate, game is a draw", gameID);
            WSSessions.broadcastSession(gameID,null, notificationMessage);
            ended = true;
            return;

        }
        if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White in check", gameID);
            WSSessions.broadcastSession(gameID,null, notificationMessage);
        }
        if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black in check", gameID);
            WSSessions.broadcastSession(gameID,null, notificationMessage);
        }
    }

    public static void handleResign(UserGameCommand command, Session session) throws Exception {
        ChessGame.TeamColor playerColor = null;
        ChessGame.TeamColor oppPlayerColor = null;

        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        String username = sqlAuthDAO.getUsername(authToken);
        ChessMove move = command.getMove();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        GameData data = sqlGameDAO.getGameData(gameID);

        if (username == null) {
            System.out.println("0");

            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if ((data.getWhiteUsername() == null) || (data.getBlackUsername() == null)) {
            System.out.println("1");
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "cant resign yet", gameID);
            WSSessions.broadcastSession(gameID,null, msg);
            return;
        }
        if (data.getBlackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
            oppPlayerColor = ChessGame.TeamColor.WHITE;
        }
        if (data.getWhiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
            oppPlayerColor = ChessGame.TeamColor.BLACK;
        }

        sqlGameDAO.updateGame(playerColor,gameID, null);
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, oppPlayerColor + " won the game",gameID);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, oppPlayerColor + " won the game", gameID);
        WSSessions.broadcastSession(gameID,authToken, notificationMessage);
    }

    public static void handleLeave(UserGameCommand command, Session session) throws Exception {
        ChessGame.TeamColor playerColor = null;
        ChessGame.TeamColor oppPlayerColor = null;

        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        String username = sqlAuthDAO.getUsername(authToken);
        ChessMove move = command.getMove();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        GameData data = sqlGameDAO.getGameData(gameID);

        if (username == null) {
            System.out.println("0");

            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if ((data.getBlackUsername()!=null) && data.getBlackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
            oppPlayerColor = ChessGame.TeamColor.WHITE;
        }
        if ((data.getWhiteUsername()!= null) && data.getWhiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
            oppPlayerColor = ChessGame.TeamColor.BLACK;
        }

        sqlGameDAO.updateGame(playerColor,gameID, null);
        ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, playerColor + " left the game",gameID);
        session.getRemote().sendString(new Gson().toJson(leaveMessage));
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, playerColor + " left the game", gameID);
        WSSessions.broadcastSession(gameID,authToken, notificationMessage);

        WSSessions.removeSession(gameID, authToken);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace(System.err);
    }
}
