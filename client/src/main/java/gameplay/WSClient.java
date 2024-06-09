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
    @SuppressWarnings("unused")

    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    @SuppressWarnings("unused")

    public void onMessage(String message) throws InterruptedException {
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(msg.getErrorMessage());
            if (msg.getErrorMessage().equals("invalid gameID")) {
                System.out.println("press enter to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
            if (msg.getErrorMessage().equals("invalid command")) {
                System.out.println("press enter to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                PreLoginUI.setCurrentState(PreLoginUI.State.IN_GAME);
                ObserverGameUI.redrawBoard(msg.getGame());
                GameUI.redrawBoard(msg.getGame());
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            if (msg.getMessage().equals("move was made")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("joined as observer")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("won the game")) {
                GameUI.resigned = true;
                System.out.println(msg.getMessage());
            }

        }

    }

    @OnClose
    @SuppressWarnings("unused")

    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    @OnError
    @SuppressWarnings("unused")

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


    @SuppressWarnings("unused")
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}
