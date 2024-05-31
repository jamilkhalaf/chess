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
    @DisplayName("create game success")
    public void createGameSuccess() throws DataAccessException {

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        String user1 = "user";
        String pass1 = "username";
        String email1 = "hello";
        userDAO.createUser(user1, pass1, email1);
        gameDAO.createGame("game");

        GameData data = gameDAO.getGameData(1);

        ChessBoard board = data.getGame();

        assertEquals(board.getPiece(new ChessPosition(1,1)),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        ;


    }

    @Test
    @DisplayName("create game failure")
    public void createGameFailure() throws DataAccessException{

        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();
        String user1 = "user";
        String pass1 = "username";
        String email1 = "hello";
        userDAO.createUser(user1, pass1, email1);
        gameDAO.createGame("game");
        gameDAO.makeChessMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1),null),1);

        GameData data = gameDAO.getGameData(1);

        ChessBoard board = data.getGame();


        assertNotEquals(board.getPiece(new ChessPosition(2,1)),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

    }
}
