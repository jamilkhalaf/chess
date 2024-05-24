package service.service;

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

public class ClearServiceTest {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    @DisplayName("Clear Test")
    public void clearData() throws DataAccessException {
        // Ensure initial data is present


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

    @Test
    @DisplayName("Clear Test")
    public void clearData2() throws DataAccessException {
        // Ensure initial data is present


        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "khalaf";
        String pass2 = "24343";
        String email2 = "jamil@edu";
        userDAO.createUser(user2, pass2, email2);

        gameDAO.createGame("jamil1234");
        authDAO.createAuth("jamil");


        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        clearService.clearUser();

        assertEquals(0, authDAO.getSize());
        assertEquals(0, userDAO.getSize());
        assertEquals(0, gameDAO.getSize());

    }
}
