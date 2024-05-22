package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;

import java.util.List;

public class ListGamesService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public ListGamesService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
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

    public List<GameData> listGames(String authToken) throws DataAccessException {
        return gameDAO.listGames();
    }



}
