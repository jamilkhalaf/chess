package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
//        throw new RuntimeException("Not implemented");
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
//        throw new RuntimeException("Not implemented");
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        throw new RuntimeException("Not implemented");
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> valid_moves = new ArrayList<>();
        if (piece.type == PieceType.PAWN) {
            int[][] directions = {{1,0},{1,1},{1,-1}};
            for (int[] direction : directions) {
                int newRow = myPosition.getRow() + direction[0];
                int newCol = myPosition.getColumn() + direction[1];
                while (true) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    if (direction[0] == 1 && direction[1] == 0) {
                        if (board.getPiece(newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                        else {break;}
                    } else {
                        if (board.getPiece(newPosition).pieceColor == ChessGame.TeamColor.BLACK) {
                            valid_moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                        else {break;}
                    }
                }
            }
        }
        return valid_moves;
    }
}
