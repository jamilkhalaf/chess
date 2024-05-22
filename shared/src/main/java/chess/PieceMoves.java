package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PieceMoves {
    public static Collection<ChessMove> pieceValidMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, ChessGame.TeamColor myColor, int[][] directions) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int newRow;
        int newCol;
        if ((piece.getPieceType() == ChessPiece.PieceType.ROOK) || (piece.getPieceType() == ChessPiece.PieceType.QUEEN) || (piece.getPieceType() == ChessPiece.PieceType.BISHOP)) {
            for (int[] rDirection : directions) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rookNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rookNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, rookNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rookNewPosition);
                    } else {
                        if (board.getPiece(rookNewPosition).getTeamColor() != myColor) {
                            validMoves.add(new ChessMove(myPosition, rookNewPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + rookNewPosition);
                        }
                        break;
                    }
                    newRow += rDirection[0];
                    newCol += rDirection[1];
                }
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            int[][] kingDirections = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}, {1,1}, {1,-1}, {-1,-1}, {-1,1}};
            for (int[] rDirection : kingDirections) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                ChessPosition kingNewPosition = new ChessPosition(newRow, newCol);
                ChessGame.TeamColor color = piece.getTeamColor();
                if (color == ChessGame.TeamColor.WHITE) {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(kingNewPosition) == null) {
                            validMoves.add(new ChessMove(myPosition, kingNewPosition, null));
                        }
                        else if (board.getPiece(kingNewPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            validMoves.add(new ChessMove(myPosition, kingNewPosition, null));
                        }
                    }
                }
                else {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(kingNewPosition) == null) {
                            validMoves.add(new ChessMove(myPosition, kingNewPosition, null));
                        }
                        else if (board.getPiece(kingNewPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            validMoves.add(new ChessMove(myPosition, kingNewPosition, null));
                        }

                    }
                }
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            int[][] knightDirections = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
            for (int[] rDirection : knightDirections) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                ChessPosition knightNewPosition = new ChessPosition(newRow, newCol);
                ChessGame.TeamColor color = piece.getTeamColor();
                if (color == ChessGame.TeamColor.WHITE) {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(knightNewPosition) == null) {
                            validMoves.add(new ChessMove(myPosition, knightNewPosition, null));
                        }
                        else if (board.getPiece(knightNewPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            validMoves.add(new ChessMove(myPosition, knightNewPosition, null));
                        }
                    }
                }
                else {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(knightNewPosition) == null) {
                            validMoves.add(new ChessMove(myPosition, knightNewPosition, null));
                        }
                        else if (board.getPiece(knightNewPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            validMoves.add(new ChessMove(myPosition, knightNewPosition, null));
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}
