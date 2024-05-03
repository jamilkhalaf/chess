package chess;

import java.util.ArrayList;
import java.util.Collection;
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
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> valid_moves = new ArrayList<>();
        int newRow;
        int newCol;
        if (piece.type == PieceType.PAWN) {

            int[][] pawn_directions = {{1,0},{1,1},{1,-1},{2,0},{-1,0},{-1,1},{-1,-1},{-2,0}};

            for (int[] p_direction : pawn_directions) {
                // white pieces
                if ((p_direction[0] > 0) && (piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                    newRow = myPosition.getRow() + p_direction[0];
                    newCol = myPosition.getColumn() + p_direction[1];
                    if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {continue;}
                    ChessPosition pawn_newPosition = new ChessPosition(newRow, newCol);
                    if ((p_direction[1] == 0) && (p_direction[0] != 2) && (newRow <= 7)) {
                        if (board.getPiece(pawn_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));
                        }
                    }
                    else if ((p_direction[1] == 0) && (p_direction[0] != 2) && (newRow == 8)) {
                        if (board.getPiece(pawn_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.QUEEN));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.BISHOP));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.KNIGHT));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.ROOK));
                        }
                    }
                    else if ((p_direction[1] == 0) && (myPosition.getRow()==2) && (p_direction[0] == 2)) {
                        int middle_row = myPosition.getRow() + 1;
                        ChessPosition mid_position = new ChessPosition(middle_row, myPosition.getColumn());
                        if ((board.getPiece(mid_position) == null) && (board.getPiece(pawn_newPosition) == null)) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));
                        }
                    }
                    else if ((p_direction[1] != 0) && (newRow<=7)) {
                        if ((board.getPiece(pawn_newPosition) != null) && (board.getPiece(pawn_newPosition).getTeamColor() == ChessGame.TeamColor.BLACK)){
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));
                        }
                    }
                    else if ((p_direction[1] != 0) && (newRow==8)) {
                        if ((board.getPiece(pawn_newPosition) != null) && (board.getPiece(pawn_newPosition).getTeamColor() == ChessGame.TeamColor.BLACK)){
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.QUEEN));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.BISHOP));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.KNIGHT));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.ROOK));
                        }
                    }
                }
                //black pieces
                if ((p_direction[0] < 0) && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    newRow = myPosition.getRow() + p_direction[0];
                    newCol = myPosition.getColumn() + p_direction[1];

                    if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {continue;}

                    ChessPosition pawn_newPosition = new ChessPosition(newRow, newCol);
                    if ((p_direction[1] == 0) && (p_direction[0] != -2) && (newRow >=2)) {
                        if (board.getPiece(pawn_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));
                        }
                    }
                    else if ((p_direction[1] == 0) && (p_direction[0] != -2) && (newRow == 1)) {
                        if (board.getPiece(pawn_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.QUEEN));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.BISHOP));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.KNIGHT));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.ROOK));
                        }
                    }
                    else if ((p_direction[1] == 0) && (myPosition.getRow()==7) && (p_direction[0] == -2)) {
                        int middle_row = myPosition.getRow() - 1;
                        ChessPosition mid_position = new ChessPosition(middle_row, myPosition.getColumn());
                        if (board.getPiece(mid_position) == null) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));
                        }
                    }
                    else if ((p_direction[1] != 0) && (newRow>=2)) {
                        if ((board.getPiece(pawn_newPosition) != null) && (board.getPiece(pawn_newPosition).getTeamColor() == ChessGame.TeamColor.WHITE)){
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, null));}
                    }
                    else if ((p_direction[1] != 0) && (newRow==1)) {
                        if ((board.getPiece(pawn_newPosition) != null) && (board.getPiece(pawn_newPosition).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.QUEEN));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.BISHOP));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.KNIGHT));
                            valid_moves.add(new ChessMove(myPosition, pawn_newPosition, PieceType.ROOK));
                        }
                    }
                }
            }
        }

        if (piece.type == PieceType.ROOK) {
            int[][] rook_directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] r_direction : rook_directions) {
                newRow = myPosition.getRow() + r_direction[0];
                newCol = myPosition.getColumn() + r_direction[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rook_newPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rook_newPosition) == null) {
                        valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rook_newPosition);
                    } else {
                        if (board.getPiece(rook_newPosition).getTeamColor() != this.getTeamColor()) {
                            valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + rook_newPosition);
                        }
                        break;
                    }
                    newRow += r_direction[0];
                    newCol += r_direction[1];
                }
            }
        }

        if (piece.type == PieceType.KING) {
            int[][] king_directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}, {1,1}, {1,-1}, {-1,-1}, {-1,1}};
            for (int[] r_direction : king_directions) {
                newRow = myPosition.getRow() + r_direction[0];
                newCol = myPosition.getColumn() + r_direction[1];
                ChessPosition king_newPosition = new ChessPosition(newRow, newCol);
                ChessGame.TeamColor color = piece.getTeamColor();
                if (color == ChessGame.TeamColor.WHITE) {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(king_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, king_newPosition, null));
                        }
                        else if (board.getPiece(king_newPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            valid_moves.add(new ChessMove(myPosition, king_newPosition, null));
                        }

                    }

                }
                else {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(king_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, king_newPosition, null));
                        }
                        else if (board.getPiece(king_newPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            valid_moves.add(new ChessMove(myPosition, king_newPosition, null));
                        }

                    }
                }

            }
        }

        if (piece.type == PieceType.BISHOP) {
            int[][] bishop_directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] r_direction : bishop_directions) {
                newRow = myPosition.getRow() + r_direction[0];
                newCol = myPosition.getColumn() + r_direction[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rook_newPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rook_newPosition) == null) {
                        valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rook_newPosition);
                    } else {
                        if (board.getPiece(rook_newPosition).getTeamColor() != this.getTeamColor()) {
                            valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + rook_newPosition);
                        }
                        break;
                    }
                    newRow += r_direction[0];
                    newCol += r_direction[1];
                }
            }
        }

        if (piece.type == PieceType.QUEEN) {
            int[][] queen_directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1},{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] r_direction : queen_directions) {
                newRow = myPosition.getRow() + r_direction[0];
                newCol = myPosition.getColumn() + r_direction[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rook_newPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rook_newPosition) == null) {
                        valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rook_newPosition);
                    } else {
                        if (board.getPiece(rook_newPosition).getTeamColor() != this.getTeamColor()) {
                            valid_moves.add(new ChessMove(myPosition, rook_newPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + rook_newPosition);
                        }
                        break;
                    }
                    newRow += r_direction[0];
                    newCol += r_direction[1];
                }
            }
        }

        if (piece.type == PieceType.KNIGHT) {
            int[][] knight_directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
            for (int[] r_direction : knight_directions) {
                newRow = myPosition.getRow() + r_direction[0];
                newCol = myPosition.getColumn() + r_direction[1];
                ChessPosition knight_newPosition = new ChessPosition(newRow, newCol);
                ChessGame.TeamColor color = piece.getTeamColor();
                if (color == ChessGame.TeamColor.WHITE) {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(knight_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, knight_newPosition, null));
                        }
                        else if (board.getPiece(knight_newPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            valid_moves.add(new ChessMove(myPosition, knight_newPosition, null));
                        }

                    }

                }
                else {
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        if (board.getPiece(knight_newPosition) == null) {
                            valid_moves.add(new ChessMove(myPosition, knight_newPosition, null));
                        }
                        else if (board.getPiece(knight_newPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            valid_moves.add(new ChessMove(myPosition, knight_newPosition, null));
                        }

                    }
                }

            }
        }



        return valid_moves;
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
