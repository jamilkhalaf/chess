package dataaccess;
import java.util.List;
import chess.ChessGame;


public interface GameDAO {

    List<String> gamesList() throws DataAccessException;
    String getGameName(String gameName) throws DataAccessException;

    Integer getGameID(Integer gameID) throws DataAccessException;
    void clear() throws DataAccessException;

    Integer createGame(String gameName) throws DataAccessException;


}
