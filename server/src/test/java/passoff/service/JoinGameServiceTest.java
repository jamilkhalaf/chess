package passoff.service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateGameReq;
import requests.JoinGameReq;
import requests.LoginReq;
import responses.BaseRes;
import responses.CreateGameRes;
import service.CreateGameService;
import service.JoinGameService;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTest {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    @DisplayName("join game success")
    public void JoinGameSuccess() throws DataAccessException {
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
    public void JoinGameFailure() throws IllegalArgumentException, DataAccessException{

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


        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            joinGameService.joinGame(authToken1,3,ChessGame.TeamColor.WHITE);
        });
        assertEquals("Error joining game: Error: bad request", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

    }
}
