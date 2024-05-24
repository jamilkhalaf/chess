package passoff.service;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.BaseReq;
import requests.LoginReq;
import responses.BaseRes;
import responses.LoginRes;
import service.LoginService;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogoutServiceTest {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    @DisplayName("Logoutsuccess")
    public void logoutSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);


        LoginReq loginRequest1 = new LoginReq("jamil", "123");


        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("jamil", "123");


        BaseReq logoutRequest = new BaseReq();

        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);

        logoutService.logoutUser(authToken1);

        BaseRes logoutResponse = new BaseRes();


        assertNotNull(logoutResponse);

    }

    @Test
    @DisplayName("Logout failure")
    public void logoutFailure() throws DataAccessException {
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);



        BaseReq logoutRequest = new BaseReq();

        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);


        BaseRes logoutResponse = new BaseRes();


        assertNotNull(logoutResponse);

        DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
            logoutService.logoutUser("");
        });
        assertEquals("Error: unauthorized", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

    }
}
