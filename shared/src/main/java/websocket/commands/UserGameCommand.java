package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }
    //move
    public UserGameCommand(String authToken, Integer gameID, ChessMove move) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
    }

    //leave and resign
    public UserGameCommand(String authToken, int gameID, CommandType commandtype) {
        this.commandType = CommandType.LEAVE;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    //connect
    public UserGameCommand(String authToken, Integer gameID, String color) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private final String authToken;
    public int gameID;
    public ChessMove move;
    public String color;



    public String getAuthString() {
        return authToken;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
