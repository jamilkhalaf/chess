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
        Collection<ChessMove> validMoves = new ArrayList<>();
        int newRow;
        int newCol;
        if (piece.type == PieceType.PAWN) {

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
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.ROOK));
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
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.ROOK));
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
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.ROOK));
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
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, pawnNewPosition, PieceType.ROOK));
                        }
                    }
                }
            }
        }

        if (piece.type == PieceType.ROOK) {
            int[][] rookDirections = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] rDirection : rookDirections) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rookNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rookNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, rookNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rookNewPosition);
                    } else {
                        if (board.getPiece(rookNewPosition).getTeamColor() != this.getTeamColor()) {
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

        if (piece.type == PieceType.KING) {
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

        if (piece.type == PieceType.BISHOP) {
            int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] rDirection : bishopDirections) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rookNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rookNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, rookNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rookNewPosition);
                    } else {
                        if (board.getPiece(rookNewPosition).getTeamColor() != this.getTeamColor()) {
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

        if (piece.type == PieceType.QUEEN) {
            int[][] queenDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1},{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] rDirection : queenDirections) {
                newRow = myPosition.getRow() + rDirection[0];
                newCol = myPosition.getColumn() + rDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition rookNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(rookNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, rookNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + rookNewPosition);
                    } else {
                        if (board.getPiece(rookNewPosition).getTeamColor() != this.getTeamColor()) {
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

        if (piece.type == PieceType.KNIGHT) {
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
