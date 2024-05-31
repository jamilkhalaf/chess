package dataaccess;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.BaseReq;
import responses.ListGamesRes;
import service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL List Game Success")
    public void testListGameSuccess() throws DataAccessException {
        // Clear existing data
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        clearService.clearUser();

        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("alex's game");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);
        String authToken = loginService.loginUser("alex", "pass123");

        BaseReq listRequest = new BaseReq();

        ListGamesService listGameService = new ListGamesService(userDAO, gameDAO, authDAO);

        List<GameData> gamesList = listGameService.listGames(authToken);

        ListGamesRes listGameResponse = new ListGamesRes(gamesList);

        assertNotNull(listGameResponse.getGames());
    }

    @Test
    @DisplayName("SQL List Game Failure")
    public void testListGameFailure() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("alex's game");

        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);
        String authToken = loginService.loginUser("alex", "pass123");

        BaseReq listRequest = new BaseReq();

        ListGamesService listGameService = new ListGamesService(userDAO, gameDAO, authDAO);

        List<GameData> gameData = listGameService.listGames("");

        assertNotNull(gameData);
    }
}
