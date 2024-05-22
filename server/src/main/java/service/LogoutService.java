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


        String AuthToken = authDAO.getAuth(authToken);
        if (AuthToken != null) {
            authDAO.deleteAuth(AuthToken);
        }
        else {throw new DataAccessException("Error: unauthorized");}



    }
}
