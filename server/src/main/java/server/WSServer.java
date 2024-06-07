package server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.*;


@WebSocket
public class WSServer {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ServerMessage message = null;


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
                    break;
                case CONNECT:
                    handleConnect(command, session);
                    break;
                default:
                    System.out.println("Unknown command type: " + command.getCommandType());
                    break;
            }


        } catch (Exception e) {
            System.out.println("Error parsing message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleMakeMove(UserGameCommand command, Session session) throws DataAccessException, InterruptedException {
        Integer gameID = command.getGameID();
        ChessMove move = command.getMove();
        System.out.println("Handling make move command: " + command);
        try {
            SQLGameDAO gameDao = new SQLGameDAO();
            gameDao.makeChessMove(move, gameID);
        }
        catch (DataAccessException e) {
            System.out.println("error");
        }
        ChessGame game = gameDAO.getGameData(gameID).getGame();

        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("White won");
            System.exit(0);
        }
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Black won");
            System.exit(0);
        }
        if (game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Draw");
            System.exit(0);
        }

        try {
            session.getRemote().sendString(new Gson().toJson(command));
            message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,gameID);
            WSSessions.broadcastSession(gameID, null, message);
        }
        catch (Exception e) {
            System.out.println();
        }

    }



    private void handleConnect(UserGameCommand command, Session session) {
        try {
            WSSessions.addSession(command.getGameID(), command.getAuthToken(), session);
//            session.getRemote().sendString(new Gson().toJson(command));
            message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, command.game);
            WSSessions.broadcastSession(command.getGameID(), null, message);
        }
        catch (Exception e) {
            System.out.println();
        }
    }
}
