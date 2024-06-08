package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;

public class WSSessions {
    private static final Map<Integer, Map<String, Session>> sessions = new HashMap<>();

    public static void addSession(Integer gameID, String authToken, Session session) {
        sessions.computeIfAbsent(gameID, k -> new HashMap<>()).put(authToken, session);
    }

    public static void broadcastSession(Integer gameID, String excludeAuthToken, ServerMessage message) throws Exception {
        Map<String, Session> sessionsForGame = getSessionsForGame(gameID);

        if (sessionsForGame == null) {
            throw new Exception("Game ID not found: " + gameID);
        }

        for (Map.Entry<String, Session> entry : sessionsForGame.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();

            if (excludeAuthToken == null || !authToken.equals(excludeAuthToken)) {
                session.getRemote().sendString(new Gson().toJson(message));
            }
        }
    }

    static Map<String, Session> getSessionsForGame(Integer gameID) {
        return sessions.get(gameID);
    }
}
