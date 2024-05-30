package dataaccess;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateGameReq;
import requests.LoginReq;
import responses.CreateGameRes;
import responses.LoginRes;
import service.ClearService;
import service.CreateGameService;
import service.LoginService;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateGameSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("create game success")
    public void createGameSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        Integer gameID = gameService.createGame("game2");

        CreateGameRes gameResponse1 = new CreateGameRes(gameID);

        assertEquals(2, gameResponse1.getGameID());
        ;


    }

    @Test
    @DisplayName("create game failure")
    public void createGameFailure() throws DataAccessException{

        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        CreateGameReq gameRequest1 = new CreateGameReq();

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            gameService.createGame(gameRequest1.getGameName());
        });
        assertEquals("Invalid parameter", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

    }
}
