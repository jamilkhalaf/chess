package gameplay;

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

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            if (userGameCommand.getColor().equals("WHITE")) {

                Integer gameID = userGameCommand.gameID;
                PostLoginUI.getBoard(gameID);
                PostLoginUI.printWhiteBoard();
            }
            if (userGameCommand.getColor().equals("BLACK")) {
                Integer gameID = userGameCommand.gameID;
                PostLoginUI.getBoard(gameID);
                PostLoginUI.printBlackBoard();
            }
        }

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            int gameID = userGameCommand.getGameID();
            GameUI.redrawBoard(gameID);
        }

        else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            GameUI.redrawBoard(userGameCommand.getGameID());
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
