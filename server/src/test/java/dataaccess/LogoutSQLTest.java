package dataaccess;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.BaseReq;
import requests.LoginReq;
import responses.BaseRes;
import responses.LoginRes;
import service.ClearService;
import service.LoginService;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Logout success")
    public void testLogoutSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        LoginReq loginRequest1 = new LoginReq("alex", "pass123");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("alex", "pass123");

        BaseReq logoutRequest = new BaseReq();

        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);

        logoutService.logoutUser(authToken1);

        BaseRes logoutResponse = new BaseRes();

        assertNotNull(logoutResponse);
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }

    @Test
    @DisplayName("SQL Logout failure")
    public void testLogoutFailure() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        BaseReq logoutRequest = new BaseReq();

        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);

        BaseRes logoutResponse = new BaseRes();

        assertNotNull(logoutResponse);

        DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
            logoutService.logoutUser("");
        });
        assertEquals("Error: unauthorized", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }
}
