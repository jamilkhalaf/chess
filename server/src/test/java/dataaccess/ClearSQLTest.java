package dataaccess;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import requests.BaseReq;
import responses.BaseRes;
import server.Server;
import service.ClearService;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();


    @Test
    @DisplayName("Clear Test")
    public void clearData() throws DataAccessException {
        // Ensure initial data is present
        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();

        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "khalaf";
        String pass2 = "24343";
        String email2 = "jamil@edu";
        userDAO.createUser(user2, pass2, email2);

        gameDAO.createGame("jamil's game");
        authDAO.createAuth("jamil");


        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        clearService.clearUser();

        assertEquals(0, authDAO.getSize());
        assertEquals(0, userDAO.getSize());
        assertEquals(0, gameDAO.getSize());

    }
}
