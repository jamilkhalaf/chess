package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;

public class LoginService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String loginUser(String username, String password) throws DataAccessException {
        UserData user = userDAO.getUser(username, password);
        if (user != null && user.password().equals(password)) {
            String authToken = authDAO.createAuth(username);
            return authToken;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }

    }
}
