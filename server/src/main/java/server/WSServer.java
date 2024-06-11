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
    private static String color;

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
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid gameID");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }


        if (username == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid GameID");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (sqlGameDAO.getGame(gameID) == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid gameID");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (username.equals(data.getWhiteUsername()) || username.equals(data.getBlackUsername())) {
            if (username.equals(data.getWhiteUsername())) {color = "WHITE";}
            else {color = "BLACK";}
            WSSessions.addSession(command.getGameID(), authToken, session);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username +" joined as " + color, gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
            return;
        }
        if (command.getPlayerColor() == ChessGame.TeamColor.empty) {
            WSSessions.addSession(command.getGameID(), authToken, session);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username +" joined as observer", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
        }
        else {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "color is already taken", gameID);
            session.getRemote().sendString(new Gson().toJson(notificationMessage));

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
                System.out.println("no");
                ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Game ended, cannot make any more moves");
                session.getRemote().sendString(new Gson().toJson(msg));
                return;
            }
            ChessGame game = sqlGameDAO.makeChessMove(move,gameID);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            WSSessions.broadcastSession(gameID,null, loadGameMessage);
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "move was made", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
            check(game,gameID,session, authToken);
        }
        catch (DataAccessException e) {
            check(data.getGame(),gameID,session, authToken);
            if (ended) {
                ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Game ended, cannot make any more moves");
                session.getRemote().sendString(new Gson().toJson(msg));
                return;
            }
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid move");
            session.getRemote().sendString(new Gson().toJson(msg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void check(ChessGame game, Integer gameID, Session session,String authToken) throws Exception {
        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black in checkmate, White won", gameID);
            WSSessions.broadcastSession(gameID,null, msg);
            ended = true;
            return;
        }
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White in checkmate, Black won", gameID);
            WSSessions.broadcastSession(gameID,null, msg);
            ended = true;
            return;

        }
        if (game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate", gameID);
            WSSessions.broadcastSession(gameID,null, msg);
            ServerMessage msg2 = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Stalemate");
            WSSessions.broadcastSession(gameID,authToken, msg2);
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
        ChessGame game = data.getGame();

        if (username == null) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (!username.equals(data.getBlackUsername()) && !username.equals(data.getWhiteUsername())) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "cant resign as observer");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if ((data.getWhiteUsername() == null) || (data.getBlackUsername() == null)) {
            System.out.println("1");
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "cant resign, opponent already resigned");
            session.getRemote().sendString(new Gson().toJson(msg));
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
        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        String username = sqlAuthDAO.getUsername(authToken);
        ChessMove move = command.getMove();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        GameData data = sqlGameDAO.getGameData(gameID);

        if (username == null) {
            System.out.println("0");
            System.out.println("check");

            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth");
            session.getRemote().sendString(new Gson().toJson(msg));
            return;
        }

        if (!username.equals(data.getBlackUsername()) && !username.equals(data.getWhiteUsername())) {
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " left the game", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);
            return;
        }
        if (data.getBlackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        if (data.getWhiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        ended = false;
        sqlGameDAO.updateGame(playerColor,gameID, null);
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " left the game", gameID);
        WSSessions.broadcastSession(gameID,authToken, notificationMessage);
        WSSessions.removeSession(gameID, authToken);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace(System.err);
    }
}
