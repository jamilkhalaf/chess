package gameplay;


import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WSClient {
    private Session session;

    @OnOpen

    public void onOpen(Session session) throws Exception {
        this.session = session;
        System.out.println("Connected to server");
//        simulateLifecycleMethods();
    }

    @OnMessage

    public void onMessage(String message) throws InterruptedException {
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(msg.getErrorMessage());
            if (msg.getErrorMessage().equals("invalid gameID")) {
                GameUI.stopRun = true;
                System.out.println("press c to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
            if (msg.getErrorMessage().equals("invalid auth")) {
                GameUI.stopRun = true;
                System.out.println("press c to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
            if (msg.getErrorMessage().equals("invalid command")) {
                GameUI.stopRun = true;
                System.out.println("press c to continue");
                PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_IN);
            }
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                PreLoginUI.setCurrentState(PreLoginUI.State.IN_GAME);
                GameUI.redrawBoard(msg.getGame());
        }

        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            if (msg.getMessage().equals("move was made")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("joined as observer")) {
                System.out.println(msg.getMessage());
            }
            else if (msg.getMessage().contains("joined")) {
                System.out.println(msg.getMessage());
            }

            if (msg.getMessage().contains("in checkmate")) {
                System.out.println(msg.getMessage());
            }

            else if (msg.getMessage().contains("in check")) {
                System.out.println(msg.getMessage());
            }

            if (msg.getMessage().contains("Stalemate")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("Game ended")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("won the game")) {
                GameUI.resigned = true;
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("left the game")) {
                System.out.println(msg.getMessage());
            }
            if (msg.getMessage().contains("cant resign yet")) {
                System.out.println(msg.getMessage());
            }

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


    public void simulateLifecycleMethods() throws Exception {
        Session mockSession = null;
        CloseReason mockCloseReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Testing");

        onOpen(mockSession);

        String mockMessage = "{\"serverMessageType\":\"ERROR\",\"errorMessage\":\"invalid gameID\"}";
        onMessage(mockMessage);

        onClose(mockSession, mockCloseReason);
        onError(mockSession, new Exception("Simulated error"));
    }
}
