package dataaccess;

import dataaccess.*;
import org.junit.jupiter.api.*;
import requests.LoginReq;
import responses.LoginRes;
import service.ClearService;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Login success")
    public void testLoginSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "sara";
        String pass2 = "securePass";
        String email2 = "sara@edu.com";
        userDAO.createUser(user2, pass2, email2);

        LoginReq loginRequest1 = new LoginReq("alex", "pass123");
        LoginReq loginRequest2 = new LoginReq("sara", "securePass");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("alex", "pass123");
        LoginRes loginResponse1 = new LoginRes(loginRequest1.getUsername(), authToken1);

        String authToken2 = loginService.loginUser("sara", "securePass");
        LoginRes loginResponse2 = new LoginRes(loginRequest2.getUsername(), authToken2);

        assertNotNull(loginResponse1.getAuthToken());
        assertEquals("alex", loginResponse1.getUsername());

        assertNotNull(loginResponse2.getAuthToken());
        assertEquals("sara", loginResponse2.getUsername());
    }

    @Test
    @DisplayName("SQL Login failure")
    public void testLoginFailure() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "sara";
        String pass2 = "securePass";
        String email2 = "sara@edu.com";
        userDAO.createUser(user2, pass2, email2);

        LoginReq loginRequest1 = new LoginReq("alex", "wrongPass");
        LoginReq loginRequest2 = new LoginReq("sara", "incorrectPass");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
            loginService.loginUser(loginRequest1.getUsername(), loginRequest1.getPassword());
        });
        assertEquals("Error: unauthorized", exception1.getMessage());

        DataAccessException exception2 = assertThrows(DataAccessException.class, () -> {
            loginService.loginUser(loginRequest2.getUsername(), loginRequest2.getPassword());
        });
        assertEquals("Error: unauthorized", exception2.getMessage());
    }
}
