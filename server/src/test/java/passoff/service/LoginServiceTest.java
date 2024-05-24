package passoff.service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import requests.LoginReq;
import responses.LoginRes;
import service.ClearService;
import service.LoginService;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    @DisplayName("Login success")
    public void LoginSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "khalaf";
        String pass2 = "24343";
        String email2 = "jamil@edu";
        userDAO.createUser(user2, pass2, email2);

        LoginReq loginRequest1 = new LoginReq("jamil", "123");
        LoginReq loginRequest2 = new LoginReq("khalaf", "24343");


        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("jamil", "123");
        LoginRes loginResponse1 = new LoginRes(loginRequest1.getUsername(), authToken1);

        String authToken2 = loginService.loginUser("khalaf", "24343");
        LoginRes loginResponse2 = new LoginRes(loginRequest2.getUsername(), authToken2);

        assertNotNull(loginResponse1.getAuthToken());

        assertEquals("jamil", loginResponse1.getUsername());

        assertNotNull(loginResponse2.getAuthToken());

        assertEquals("khalaf", loginResponse2.getUsername());


    }

    @Test
    @DisplayName("Login failure")
    public void LoginFailure() throws DataAccessException {


        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "khalaf";
        String pass2 = "24343";
        String email2 = "jamil@edu";
        userDAO.createUser(user2, pass2, email2);

        LoginReq loginRequest1 = new LoginReq("jamil", "123454");
        LoginReq loginRequest2 = new LoginReq("khalaf", "243435454");


        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
            loginService.loginUser(loginRequest1.getUsername(), loginRequest1.getPassword());
        });
        assertEquals("Error: unauthorized", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

        DataAccessException exception2 = assertThrows(DataAccessException.class, () -> {
            loginService.loginUser(loginRequest2.getUsername(), loginRequest2.getPassword());
        });
        assertEquals("Error: unauthorized", exception2.getMessage(), "Exception message should be 'Error: unauthorized' for user2");

    }
}
