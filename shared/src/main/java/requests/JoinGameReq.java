package requests;

import chess.ChessGame;

public class JoinGameReq {
    private Integer gameID;
    private ChessGame.TeamColor playerColor;


    public JoinGameReq(){}

    public JoinGameReq(Integer gameID, ChessGame.TeamColor playerColor){
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
