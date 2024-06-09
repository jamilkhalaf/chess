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

    //connect command
    public UserGameCommand(String authToken, Integer gameID, CommandType type)
    {
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandType = type;
    }

    public UserGameCommand(String authToken, Integer gameID, ChessMove move, CommandType type)
    {
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
        this.commandType = type;
    }


    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private ChessMove move;
    private final String authToken;
    private Integer gameID;

    public ChessMove getMove() {
        return move;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getAuthString() {
        return authToken;
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
