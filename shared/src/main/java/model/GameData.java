package model;

import chess.ChessGame;

import chess.ChessBoard;

public class GameData {
    private String gameName;
    private ChessGame game;
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;


    public GameData(Integer gameID, String gameName){
        this.gameName = gameName;

        this.game = new ChessGame();
        this.gameID = gameID;
        this.blackUsername = null;

        this.whiteUsername = null;

        ChessBoard board = new ChessBoard();
        board.resetBoard();

        this.game.setBoard(board);
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
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
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
}
