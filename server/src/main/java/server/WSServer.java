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
//                case LEAVE:
//                    break;
//                case RESIGN:
//                    handleResign(command,session);
//                    break;
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
        if (observer == null) {
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

    public static void handleMakeMove(UserGameCommand command, Session session) throws IOException {
        Integer gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessMove move = command.getMove();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        try {
            sqlGameDAO.makeChessMove(move,gameID);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            WSSessions.broadcastSession(gameID,null, loadGameMessage);
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "move was made", gameID);
            WSSessions.broadcastSession(gameID,authToken, notificationMessage);

        }
        catch (DataAccessException e) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "invalid move");
            session.getRemote().sendString(new Gson().toJson(msg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace(System.err);
    }
}
