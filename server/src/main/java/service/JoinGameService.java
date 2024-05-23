package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;

import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;

public class JoinGameService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public JoinGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public String getAuth(String authToken) throws DataAccessException {
        String authInfo = authDAO.getAuth(authToken);
        if (authInfo == null) {
            throw new DataAccessException("Unauthorized: Invalid or expired authToken");
        }
        return authInfo;
    }


    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
        try {
            String username = authDAO.getUsername(authToken);
            Integer existingGameID = gameDAO.getGame(gameID);
            if (existingGameID == null) {
                throw new DataAccessException("Error: bad request");
            }

            GameData gameData = gameDAO.getGameData(existingGameID);

            if (playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK) {
                throw new DataAccessException("Error: Invalid team color");
            }

            if ((playerColor == ChessGame.TeamColor.WHITE && gameData.getWhiteUsername() != null) ||
                    (playerColor == ChessGame.TeamColor.BLACK && gameData.getBlackUsername() != null)) {
                throw new IllegalAccessError("Error: Player color already taken.");
            }

            gameDAO.updateGame(playerColor, existingGameID, username);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Error joining game: " + e.getMessage());
        }
    }
}
