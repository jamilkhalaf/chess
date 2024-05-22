package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnPieceMoves {
    public static Collection<ChessMove> pieceValidMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, ChessGame.TeamColor myColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int newRow;
        int newCol;
        int[][] pawnDirections = {{1,0},{1,1},{1,-1},{2,0},{-1,0},{-1,1},{-1,-1},{-2,0}};

        for (int[] pDirection : pawnDirections) {
            // white pieces
            if ((pDirection[0] > 0) && (piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                newRow = myPosition.getRow() + pDirection[0];
                newCol = myPosition.getColumn() + pDirection[1];
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {continue;}
                ChessPosition pawnNewPosition = new ChessPosition(newRow, newCol);
                if ((pDirection[1] == 0) && (pDirection[0] != 2) && (newRow <= 7)) {
                    if (board.getPiece(pawnNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));
                    }
                }
                else if ((pDirection[1] == 0) && (pDirection[0] != 2) && (newRow == 8)) {
                    if (board.getPiece(pawnNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.ROOK));
                    }
                }
                else if ((pDirection[1] == 0) && (myPosition.getRow()==2) && (pDirection[0] == 2)) {
                    int middleRow = myPosition.getRow() + 1;
                    ChessPosition midPosition = new ChessPosition(middleRow, myPosition.getColumn());
                    if ((board.getPiece(midPosition) == null) && (board.getPiece(pawnNewPosition) == null)) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));
                    }
                }
                else if ((pDirection[1] != 0) && (newRow<=7)) {
                    if ((board.getPiece(pawnNewPosition) != null) && (board.getPiece(pawnNewPosition).getTeamColor() == ChessGame.TeamColor.BLACK)){
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));
                    }
                }
                else if ((pDirection[1] != 0) && (newRow==8)) {
                    if ((board.getPiece(pawnNewPosition) != null) && (board.getPiece(pawnNewPosition).getTeamColor() == ChessGame.TeamColor.BLACK)){
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.ROOK));
                    }
                }
            }
            //black pieces
            if ((pDirection[0] < 0) && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                newRow = myPosition.getRow() + pDirection[0];
                newCol = myPosition.getColumn() + pDirection[1];

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {continue;}

                ChessPosition pawnNewPosition = new ChessPosition(newRow, newCol);
                if ((pDirection[1] == 0) && (pDirection[0] != -2) && (newRow >=2)) {
                    if (board.getPiece(pawnNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));
                    }
                }
                else if ((pDirection[1] == 0) && (pDirection[0] != -2) && (newRow == 1)) {
                    if (board.getPiece(pawnNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.ROOK));
                    }
                }
                else if ((pDirection[1] == 0) && (myPosition.getRow()==7) && (pDirection[0] == -2)) {
                    int middleRow = myPosition.getRow() - 1;
                    ChessPosition midPosition = new ChessPosition(middleRow, myPosition.getColumn());
                    if (board.getPiece(midPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));
                    }
                }
                else if ((pDirection[1] != 0) && (newRow>=2)) {
                    if ((board.getPiece(pawnNewPosition) != null) && (board.getPiece(pawnNewPosition).getTeamColor() == ChessGame.TeamColor.WHITE)){
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, null));}
                }
                else if ((pDirection[1] != 0) && (newRow==1)) {
                    if ((board.getPiece(pawnNewPosition) != null) && (board.getPiece(pawnNewPosition).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, pawnNewPosition, ChessPiece.PieceType.ROOK));
                    }
                }
            }
        }
    return validMoves;
    }
}
