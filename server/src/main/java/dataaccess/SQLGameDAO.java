package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLGameDAO implements GameDAO{
    protected static Map<Integer, GameData> games = new HashMap<>();
    static Integer gameID = 0;

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException {
        if (gameID == null) {
            throw new DataAccessException("error");
        }
        String sql = "SELECT gameID FROM game WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("gameID");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting game ID: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public GameData getGameData(Integer gameID) throws DataAccessException {

        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);

                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("gameName"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            chessGame
                    );
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting game data: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        gameID = 0;
        String sql = "DELETE FROM game";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error clearing games: " + ex.getMessage());
        }
    }


    @Override
    public Integer createGame(String gameName) throws DataAccessException {

        String sql = "INSERT INTO game (gameID, gameName, chessGame) VALUES (?,?,?)";
        gameID += 1;
        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                String chessGameJson = new Gson().toJson(new ChessGame());
                stmt.setInt(1, gameID);

                stmt.setString(2, gameName);
                stmt.setString(3, chessGameJson);
                stmt.executeUpdate();

            } catch (SQLException ex) {
                connection.rollback();
                throw new DataAccessException("Error creating game: " + ex.getMessage());
            }
            connection.commit();
        } catch (SQLException ex) {
            throw new DataAccessException("SQL Exception on connection: " + ex.getMessage());
        }
        return gameID;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> gamesList = new ArrayList<>();
        String sql = "SELECT * FROM game";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
//                try {chessGame.makeMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null));}
//                catch (InvalidMoveException e) {throw new DataAccessException("error");}
                gamesList.add(new GameData(
                        rs.getInt("gameID"),
                        rs.getString("gameName"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        chessGame
                ));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error listing games: " + ex.getMessage());
        }
        return gamesList;
    }


    @Override
    public void updateGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException{
        String sql;
//        makeChessMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null));
        if (playerColor == ChessGame.TeamColor.WHITE) {
            sql = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";

        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            sql = "UPDATE game SET blackUsername = ? WHERE gameID = ?";

        } else if (playerColor == ChessGame.TeamColor.empty) {
            sql = "UPDATE game SET spectators = ? WHERE gameID = ?";
        } else {
            throw new DataAccessException("Invalid player color");
        }

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, gameID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error updating game: " + ex.getMessage());
        }
    }

    @Override
    public ChessGame makeChessMove(ChessMove move) throws DataAccessException{
        ChessGame chessGame;
        String selectSql = "SELECT chessGame FROM game WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, gameID);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    String chessGameJson = rs.getString("chessGame");
                    chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);
                } else {
                    throw new DataAccessException("Game not found with gameID: " + gameID);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving game data: " + ex.getMessage());
        }

        try {
            chessGame.makeMove(move);
        } catch (InvalidMoveException ex) {
            throw new DataAccessException("Invalid move: " + ex.getMessage());
        }

        // Step 3: Update the ChessGame object back in the database
        String updateSql = "UPDATE game SET chessGame = ? WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            String updatedChessGameJson = new Gson().toJson(chessGame);
            updateStmt.setString(1, updatedChessGameJson);
            updateStmt.setInt(2, gameID);
            updateStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error updating game data: " + ex.getMessage());
        }

        return chessGame;
    }

    @Override
    public Integer getSize() throws DataAccessException {
        int databaseSize = 0;
        String sql = "SELECT COUNT(*) FROM game";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                databaseSize = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting game size: " + ex.getMessage());
        }
        return databaseSize;
    }
}
