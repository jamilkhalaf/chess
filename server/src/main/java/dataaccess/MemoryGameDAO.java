package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private static int nextGameID = 1;
    protected static Map<Integer, GameData> games = new HashMap<>();

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            return gameID;
        }
        return null;
    }

    @Override
    public GameData getGameData(Integer gameID) {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        nextGameID = 1;
    }


    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        Integer gameID = nextGameID++;

        GameData game = new GameData(gameID, gameName,null,null,new ChessGame());
        game.setBlackUsername(null);
        game.setWhiteUsername(null);
        games.put(gameID, game);

        return gameID;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> gamesList = new ArrayList<>();
        for (Map.Entry<Integer, GameData> entry : games.entrySet()) {
            GameData gameData = entry.getValue();
            gamesList.add(gameData);
        }
        return gamesList;
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

    @Override
    public Integer getSize() {
        return games.size();
    }

    @Override
    public ChessGame makeChessMove(ChessMove move) {
        return new ChessGame();
    }
}