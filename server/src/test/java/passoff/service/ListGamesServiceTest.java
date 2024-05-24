package passoff.service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.BaseReq;
import requests.CreateGameReq;
import responses.CreateGameRes;
import responses.ListGamesRes;
import service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesServiceTest {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    @DisplayName("list game success")
    public void ListGameSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("jamil", "123");

        BaseReq listRequest1 = new BaseReq();

        ListGamesService listGameService = new ListGamesService(userDAO, gameDAO, authDAO);



        List<GameData> gamesList = listGameService.listGames(authToken1);

        ListGamesRes listGameResponse1 = new ListGamesRes(gamesList);

        assertNotNull(listGameResponse1.getGames());
        ;


    }

    @Test
    @DisplayName("list game failure")
    public void ListGameFailure() throws DataAccessException{

        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("jamil", "123");

        BaseReq listRequest1 = new BaseReq();

        ListGamesService listGameService = new ListGamesService(userDAO, gameDAO, authDAO);

        List<GameData> gameData = listGameService.listGames("");

        assertNotNull(gameData);

    }
}
