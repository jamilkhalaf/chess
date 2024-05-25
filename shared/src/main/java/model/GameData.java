package model;

import chess.ChessGame;

import chess.ChessBoard;

public class GameData {
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;


    public GameData(Integer gameID, String gameName){
        this.gameName = gameName;

        this.gameID = gameID;
        this.blackUsername = null;

        this.whiteUsername = null;

        ChessBoard board = new ChessBoard();
        board.resetBoard();
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
}
