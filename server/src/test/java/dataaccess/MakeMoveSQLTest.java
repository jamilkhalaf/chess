package dataaccess;

import chess.ChessMove;
import chess.ChessPosition;
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

public class MakeMoveSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("make move success")
    public void makeMoveSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        ChessMove move = new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null);
        gameDAO.makeChessMove(move,1);

        assertEquals(new ChessPosition(3,1), move.getEndPosition());
        ;


    }

    @Test
    @DisplayName("make move failure")
    public void makeMoveFailure() throws DataAccessException{

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        // Ensure initial data is present
        String user1 = "jamil";
        String pass1 = "123";
        String email1 = "jamil@byu";
        userDAO.createUser(user1, pass1, email1);

        gameDAO.createGame("jamil's game");

        CreateGameService gameService = new CreateGameService(userDAO, gameDAO, authDAO);

        ChessMove move = new ChessMove(new ChessPosition(2,1),new ChessPosition(5,1),null);


        DataAccessException exception = assertThrows(DataAccessException.class, () -> {gameDAO.makeChessMove(move,1);});

        assertEquals("Invalid move: null", exception.getMessage(), "Exception message should be 'Error: unauthorized' for user1");



    }
}
