package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String message;
    private String errorMessage;
    private Integer game;
    private int gameID;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage(ServerMessageType type, String errorMessage) {
        this.serverMessageType = type;
        this.errorMessage = errorMessage;
    }


    //load game, game is gameID
    public ServerMessage(ServerMessageType type, Integer game) {
        this.serverMessageType = type;
        this.game = game;
    }

    public ServerMessage(ServerMessageType type, String message, int gameID) {
        this.serverMessageType = type;
        this.message = message;
        this.gameID = gameID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getGame() {
        return game;
    }

    public String getMessage() {
        return message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
