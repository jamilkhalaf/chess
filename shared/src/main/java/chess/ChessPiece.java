package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        int[][] moves = {};
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.PAWN) {
            return PawnPieceMoves.pieceValidMoves(board, myPosition, piece, getTeamColor());
        }
        if (piece.getPieceType() == PieceType.ROOK) {
            int[][] pawnMoves = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            return PieceMoves.pieceValidMoves(board, myPosition, piece, getTeamColor(), pawnMoves);
        }
        if (piece.getPieceType() == PieceType.QUEEN) {
            int[][] queenMoves = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1},{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            return PieceMoves.pieceValidMoves(board, myPosition, piece, getTeamColor(), queenMoves);
        }
        if (piece.getPieceType() == PieceType.BISHOP) {
            int[][] bishopMoves = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            return PieceMoves.pieceValidMoves(board, myPosition, piece, getTeamColor(), bishopMoves);
        }
        else{return PieceMoves.pieceValidMoves(board, myPosition, piece, getTeamColor(), moves);}

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
