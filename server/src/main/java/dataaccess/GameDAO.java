package dataaccess;
import java.util.*;

import chess.ChessGame;
import model.GameData;


public interface GameDAO {

    void clear() throws DataAccessException;

    Integer createGame(String gameName) throws DataAccessException;

    Integer getGame(Integer gameID) throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;

    void updateGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException;

    GameData getGameData(Integer gameID) throws DataAccessException;


}
