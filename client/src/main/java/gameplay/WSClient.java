package gameplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.Mysqlx;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WSClient {
    private Session session;
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) throws InterruptedException {
        System.out.println("Message from server: " + message);

        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);

        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            if (GameUI.getPlayerColor() == ChessGame.TeamColor.WHITE) {

                Integer gameID = GameUI.getGameID();
                PostLoginUI.getBoard(gameID);
                PostLoginUI.printWhiteBoard();
            }
            if (GameUI.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                Integer gameID = GameUI.getGameID();
                PostLoginUI.getBoard(gameID);
                PostLoginUI.printBlackBoard();
            }
        }

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            int gameID = userGameCommand.getGameID();
            GameUI.redrawBoard(gameID);
        }
        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            GameUI.redrawBoard(msg.gameID);
        }

    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        } else {
            System.out.println("No open session to send message");
        }
    }

    public void connect(String uri) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(uri));
    }

    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}
