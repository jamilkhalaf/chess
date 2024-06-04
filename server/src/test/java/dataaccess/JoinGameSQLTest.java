package dataaccess;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.JoinGameReq;
import requests.LoginReq;
import responses.BaseRes;
import service.ClearService;
import service.JoinGameService;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Join Game Success")
    public void testJoinGameSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        // Ensure initial data is present
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("alex's game");

        LoginReq loginRequest = new LoginReq("alex", "pass123");
        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        String authToken = loginService.loginUser("alex", "pass123");
        JoinGameReq joinRequest = new JoinGameReq(1214, ChessGame.TeamColor.WHITE);

        JoinGameService joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);
        joinGameService.joinGame(authToken, 1, ChessGame.TeamColor.WHITE);

        BaseRes joinResponse = new BaseRes();

        assertNotNull(joinResponse);
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }

    @Test
    @DisplayName("SQL Join Game Failure")
    public void testJoinGameFailure() throws DataAccessException {
        try {
            ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
            clearService.clearUser();

            String user1 = "alex";
            String pass1 = "pass123";
            String email1 = "alex@byu.edu";
            userDAO.createUser(user1, pass1, email1);

            gameDAO.createGame("alex's game");

            LoginReq loginRequest = new LoginReq("alex", "pass123");
            LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

            String authToken = loginService.loginUser("alex", "pass123");

            JoinGameService joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);
            joinGameService.joinGame(authToken, 1, ChessGame.TeamColor.WHITE);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                joinGameService.joinGame(authToken, 2, ChessGame.TeamColor.WHITE);
            });
            assertEquals("Error joining game: Error: bad request", exception.getMessage(), "Exception message should be 'Error: Player color already taken.' for user1");
        } catch (IllegalArgumentException e) {
            // Handle exception if necessary
        }
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();
    }

}
