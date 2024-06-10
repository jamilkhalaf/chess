package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;

public class WSSessions {
    private static final Map<Integer, Map<String, Session>> SESSIONS = new HashMap<>();

    public static void addSession(Integer gameID, String authToken, Session session) {
        SESSIONS.computeIfAbsent(gameID, k -> new HashMap<>()).put(authToken, session);
    }

    public static void removeSession(Integer gameID, String authToken) {
        Map<String, Session> sessionsForGame = getSESSIONSForGame(gameID);
        if (sessionsForGame != null) {
            sessionsForGame.remove(authToken);
            if (sessionsForGame.isEmpty()) {
                SESSIONS.remove(gameID);
            }
        }
    }

    public static void broadcastSession(Integer gameID, String excludeAuthToken, ServerMessage message) throws Exception {
        Map<String, Session> sessionsForGame = getSESSIONSForGame(gameID);

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

    static Map<String, Session> getSESSIONSForGame(Integer gameID) {
        return SESSIONS.get(gameID);
    }
}
