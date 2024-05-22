package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;

public class CreateGameService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public CreateGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
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


    public Integer createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }

        return gameDAO.createGame(gameName);

    }
}
