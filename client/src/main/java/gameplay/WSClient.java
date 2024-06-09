package gameplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
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
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(msg.getErrorMessage());
            if (msg.getErrorMessage().equals("invalid gameID")) {
                System.out.println("press enter to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            System.out.println(msg.getMessage());
            if (msg.getMessage().contains("joined game")) {
                PreLoginUI.setCurrentState(PreLoginUI.State.IN_GAME);
                GameUI.redrawBoard(msg.getGame());
            }
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {


        }

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace(System.err);
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
