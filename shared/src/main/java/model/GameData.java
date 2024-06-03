package model;

import chess.ChessGame;

import chess.ChessBoard;

public class GameData {
    private ChessBoard board;
    private ChessGame game;
    private Integer gameID;
    private String whiteUsername = null;
    private String blackUsername = null;
    private String gameName;


    public GameData(Integer gameID, String gameName, String whiteUsername,String blackUsername, ChessGame chessGame){
        this.gameName = gameName;

        this.gameID = gameID;
        this.blackUsername = blackUsername;

        this.whiteUsername = whiteUsername;

        this.board = chessGame.getBoard();

        this.game = chessGame;

    }

    public String getGameName() {
        return gameName;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        if (this.whiteUsername == null) {
            this.whiteUsername = whiteUsername;
        }

    }

    public void setBlackUsername(String blackUsername) {
        if (this.blackUsername == null) {
            this.blackUsername = blackUsername;
        }

    }

    public ChessBoard getGameBoard() {
        return board;
    }


    public ChessGame getGame() {
        return game;
    }
}
