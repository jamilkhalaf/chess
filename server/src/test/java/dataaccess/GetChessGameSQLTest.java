package dataaccess;

import chess.*;
import dataaccess.*;
import model.GameData;
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

public class GetChessGameSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("get game success")
    public void getGameSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        String user1 = "user";
        String pass1 = "username";
        String email1 = "hello";
        userDAO.createUser(user1, pass1, email1);
        gameDAO.createGame("game");

        GameData data = gameDAO.getGameData(1);

        ChessBoard board = data.getGameBoard();

        assertEquals(board.getPiece(new ChessPosition(1,1)),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        ;
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();

    }

    @Test
    @DisplayName("get game failure")
    public void getGameFailure() throws DataAccessException{

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        String user1 = "user";
        String pass1 = "username";
        String email1 = "hello";
        userDAO.createUser(user1, pass1, email1);
        gameDAO.createGame("game");
        gameDAO.makeChessMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null),1);

        GameData data = gameDAO.getGameData(1);

        ChessBoard board = data.getGameBoard();


        assertNotEquals(board.getPiece(new ChessPosition(2,1)),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();

    }
}
