package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logoutUser(String authToken) throws DataAccessException {


        String authsToken = authDAO.getAuth(authToken);
        if (authsToken != null) {
            authDAO.deleteAuth(authsToken);
        }
        else {throw new DataAccessException("Error: unauthorized");}



    }
}
