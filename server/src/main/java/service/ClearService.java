package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public void clearUser(String authToken) throws DataAccessException {


        String AuthToken = authDAO.getAuth(authToken);
        if (AuthToken != null) {
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
        }
        else {throw new DataAccessException("Not Logged In");}



    }
}
