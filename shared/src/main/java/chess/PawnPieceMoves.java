package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnPieceMoves {
    public static Collection<ChessMove> pieceValidMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, ChessGame.TeamColor myColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int[][] pawnDirections = {{1, 0}, {1, 1}, {1, -1}, {2, 0}, {-1, 0}, {-1, 1}, {-1, -1}, {-2, 0}};
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int endRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessGame.TeamColor opponentColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

        for (int[] pDirection : pawnDirections) {
            if (direction * pDirection[0] > 0) {
                int newRow = myPosition.getRow() + pDirection[0];
                int newCol = myPosition.getColumn() + pDirection[1];
                if (isWithinBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    handleMove(board, myPosition, newPosition, pDirection, direction, startRow, endRow, opponentColor, validMoves);
                }
            }
        }
        return validMoves;
    }

    private static boolean isWithinBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private static void handleMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition, int[] pDirection, int direction, int startRow, int endRow, ChessGame.TeamColor opponentColor, Collection<ChessMove> validMoves) {
        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
        if (pDirection[1] == 0) {
            handleVerticalMove(board, myPosition, newPosition, pDirection, direction, startRow, endRow, validMoves, pieceAtNewPosition);
        } else {
            handleDiagonalMove(myPosition, newPosition, endRow, opponentColor, validMoves, pieceAtNewPosition);
        }
    }

    private static void handleVerticalMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition, int[] pDirection, int direction, int startRow, int endRow, Collection<ChessMove> validMoves, ChessPiece pieceAtNewPosition) {
        if (pDirection[0] == direction * 2) {
            int middleRow = myPosition.getRow() + direction;
            ChessPosition midPosition = new ChessPosition(middleRow, myPosition.getColumn());
            if (myPosition.getRow() == startRow && board.getPiece(midPosition) == null && pieceAtNewPosition == null) {
                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        } else if (pieceAtNewPosition == null) {
            if (newPosition.getRow() == endRow) {
                addPromotionMoves(myPosition, newPosition, validMoves);
            } else {
                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private static void handleDiagonalMove(ChessPosition myPosition, ChessPosition newPosition, int endRow, ChessGame.TeamColor opponentColor, Collection<ChessMove> validMoves, ChessPiece pieceAtNewPosition) {
        if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor() == opponentColor) {
            if (newPosition.getRow() == endRow) {
                addPromotionMoves(myPosition, newPosition, validMoves);
            } else {
                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private static void addPromotionMoves(ChessPosition from, ChessPosition to, Collection<ChessMove> validMoves) {
        validMoves.add(new ChessMove(from, to, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(from, to, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(from, to, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(from, to, ChessPiece.PieceType.ROOK));
    }
}
