package dataaccess;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.RegisterReq;
import responses.RegisterRes;
import service.ClearService;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterSQLTest {

    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Register success")
    public void testRegisterSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        // Ensure initial data is present
        String user1 = "charlie";
        String pass1 = "321";
        String email1 = "charlie@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "delta";
        String pass2 = "password";
        String email2 = "delta@edu.edu";
        userDAO.createUser(user2, pass2, email2);

        RegisterReq registerRequest1 = new RegisterReq("eva", "abc", "eva@gmail.com");
        RegisterReq registerRequest2 = new RegisterReq("finn", "xyz", "finn@gmail.com");

        RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

        String authToken1 = registerService.createUser("eva", "abc", "eva@gmail.com");
        RegisterRes registerResponse1 = new RegisterRes(registerRequest1.getUsername(), authToken1);

        String authToken2 = registerService.createUser("finn", "xyz", "finn@gmail.com");
        RegisterRes registerResponse2 = new RegisterRes(registerRequest2.getUsername(), authToken2);

        assertNotNull(registerResponse1.getAuthToken());
        assertEquals("eva", registerResponse1.getUsername());

        assertNotNull(registerResponse2.getAuthToken());
        assertEquals("finn", registerResponse2.getUsername());

        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }

    @Test
    @DisplayName("SQL Register failure")
    public void testRegisterFailure() throws DataAccessException {

        String user1 = "charlie";
        String pass1 = "321";
        String email1 = "charlie@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "delta";
        String pass2 = "password";
        String email2 = "delta@edu.edu";
        userDAO.createUser(user2, pass2, email2);

        RegisterReq registerRequest1 = new RegisterReq("charlie", "321", "charlie@byu.edu");
        RegisterReq registerRequest2 = new RegisterReq("delta", "password", "delta@edu.edu");

        RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

        DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
            registerService.createUser(registerRequest1.getUsername(), registerRequest1.getPassword(), registerRequest1.getEmail());
        });
        assertEquals("User already exists", exception1.getMessage(), "Exception message should be 'User already exists' for user1");

        DataAccessException exception2 = assertThrows(DataAccessException.class, () -> {
            registerService.createUser(registerRequest2.getUsername(), registerRequest2.getPassword(), registerRequest2.getEmail());
        });
        assertEquals("User already exists", exception2.getMessage(), "Exception message should be 'User already exists' for user2");
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }
}
