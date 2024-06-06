import chess.*;
import gameplay.PreLoginUI;
import gameplay.WSClient;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        WSClient client = new WSClient();
        client.connect("ws://localhost:4510/connect");
        PreLoginUI.init(client);
        PreLoginUI.display();

        while (true) {
            Thread.sleep(1000);
        }
    }
}