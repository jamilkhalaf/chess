package dataaccess;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateGameReq;
import requests.JoinGameReq;
import requests.LoginReq;
import responses.BaseRes;
import responses.CreateGameRes;
import service.ClearService;
import service.CreateGameService;
import service.JoinGameService;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("join game success")
    public void joinGameSuccess() throws DataAccessException {
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");


        LoginReq loginRequest1 = new LoginReq("jamil", "123");
        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken1 = loginService.loginUser("jamil", "123");
        JoinGameReq joinRequest1 = new JoinGameReq(1214, ChessGame.TeamColor.WHITE);

        JoinGameService joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);

        joinGameService.joinGame(authToken1,1,ChessGame.TeamColor.WHITE);

        BaseRes joinResponse = new BaseRes();

        assertNotNull(joinResponse);
        ;


    }

    @Test
    @DisplayName("join game failure")
    public void joinGameFailure() throws DataAccessException {
        try {
            ClearService clear = new ClearService(userDAO, gameDAO, authDAO);
            clear.clearUser();

            String user1 = "jamil";
            String pass1 = "123";
            String email1 = "jamil@byu";
            userDAO.createUser(user1, pass1, email1);

            gameDAO.createGame("jamil's game");


            LoginReq loginRequest1 = new LoginReq("jamil", "123");
            LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

            String authToken1 = loginService.loginUser("jamil", "123");

            JoinGameService joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);
            joinGameService.joinGame(authToken1,1,ChessGame.TeamColor.WHITE);

            IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
                joinGameService.joinGame(authToken1,2,ChessGame.TeamColor.WHITE);
            });
            assertEquals("Error joining game: Error: bad request", exception1.getMessage(), "Exception message should be 'Error: Player color already taken.' for user1");
        }
        catch (IllegalArgumentException e) {

        }


    }
}
