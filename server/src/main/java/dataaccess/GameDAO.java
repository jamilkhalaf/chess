package dataaccess;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import chess.ChessGame;
import model.GameData;


public class GameDAO {

    Collection[] gamesList() throws DataAccessException {
        int gameID = 0;
        String whiteUsername = "";
        String blackUsername = "";
        String gameName = "";
        Collection[] gameInfo = {Collections.singleton(gameID),
                Collections.singleton(whiteUsername),
                Collections.singleton(blackUsername),
                Collections.singleton(gameName)};
        Collection[] gamesList = {List.of(gameInfo)};
        return gamesList;
    }
    String getGameName(String gameName) throws DataAccessException {
        return gameName;
    }

    Integer getGameID(Integer gameID) throws DataAccessException {
        return gameID;
    }
    void clear(GameData data) throws DataAccessException {
    }

    Integer createGame(String gameName) throws DataAccessException {
        int ID = 0;
        return ID;
    }


}
