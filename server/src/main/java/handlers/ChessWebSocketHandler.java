package handlers;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebSocket
public class ChessWebSocketHandler {
    private static final ConcurrentMap<Session, String> SESSIONS = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        SESSIONS.put(session, "");
        System.out.println("New client connected: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received: " + message);
        SESSIONS.forEach((s, user) -> {
            try {
                s.getRemote().sendString("Server received message: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        SESSIONS.remove(session);
        System.out.println("Client disconnected: " + session.getRemoteAddress().getAddress());
    }
}
