package dataaccess;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateGameReq;
import responses.CreateGameRes;
import service.ClearService;
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Create Game Success")
    public void testCreateGameSuccess() throws DataAccessException {
        // Clear existing data
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        clearService.clearUser();

        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("alex's game");

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        Integer gameID = gameService.createGame("newgame");

        CreateGameRes gameResponse = new CreateGameRes(gameID);

        assertEquals(2, gameResponse.getGameID());
    }

    @Test
    @DisplayName("SQL Create Game Failure")
    public void testCreateGameFailure() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        CreateGameReq gameRequest = new CreateGameReq();

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gameService.createGame(gameRequest.getGameName());
        });
        assertEquals("Invalid parameter", exception.getMessage(), "Exception message should be 'Invalid parameter' for user1");
    }
}
