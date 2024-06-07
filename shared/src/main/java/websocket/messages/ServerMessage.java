package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    public ChessGame game;
    public int gameID;
    public String playerColor;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }


    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public ServerMessage(ServerMessageType type, int gameID) {
        this.serverMessageType = type;
        this.gameID = gameID;
    }

    //Load
    public ServerMessage(ServerMessageType type, String playerColor, int gameID, ChessGame game) {
        this.serverMessageType = type;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.game = game;
    }

    public ServerMessage(ServerMessageType serverMessageType, ChessGame game) {
        this.serverMessageType = serverMessageType;
        this.game = game;
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
