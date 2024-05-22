package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    protected static Map<Integer, GameData> games = new HashMap<>();

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException {
        if (MemoryUserDAO.users.containsKey(gameID)) {
            return gameID;
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }


    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        Integer gameID = gameName.hashCode();
        while (games.containsKey(gameID)) {
            gameID++;
        }
        GameData game = new GameData(gameID, gameName);
        games.put(gameID, game);

        return gameID;
    }

    @Override
    public List<List<String>> listGames() throws DataAccessException {
        List<List<String>> GamesSet = new ArrayList<>();
        for (Map.Entry<Integer, GameData> entry : MemoryGameDAO.games.entrySet()) {
            GameData game = entry.getValue();
            List<String> gameInformation = new ArrayList<>();
            Integer gameID = game.getGameID();
            gameInformation.add(String.valueOf(gameID));
            gameInformation.add(game.getWhiteUsername());
            gameInformation.add(game.getBlackUsername());
            gameInformation.add(game.getGameName());
            GamesSet.add(gameInformation);
        }

        return GamesSet;

    }


    @Override
    public void updateGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException{
        if (playerColor == ChessGame.TeamColor.WHITE){
            games.get(gameID).setWhiteUsername(username);
        }
        else if (playerColor == ChessGame.TeamColor.BLACK) {
            games.get(gameID).setBlackUsername(username);
        }
    }
}
