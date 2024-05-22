package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class RegisterService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String createUser(String username, String password, String email) throws DataAccessException {
        if (userDAO.getUser(username, password) != null) {
            throw new DataAccessException("User already exists");
        }
        userDAO.createUser(username, password, email);
        String authToken = authDAO.createAuth(username);
        return authToken;
    }
}
