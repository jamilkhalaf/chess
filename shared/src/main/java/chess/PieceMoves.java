package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PieceMoves {
    public static Collection<ChessMove> pieceValidMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, ChessGame.TeamColor myColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int newRow;
        int newCol;
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {

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
        }

        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
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

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] bDirection : bishopDirections) {
                newRow = myPosition.getRow() + bDirection[0];
                newCol = myPosition.getColumn() + bDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition bishopNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(bishopNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, bishopNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + bishopNewPosition);
                    } else {
                        if (board.getPiece(bishopNewPosition).getTeamColor() != myColor) {
                            validMoves.add(new ChessMove(myPosition, bishopNewPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + bishopNewPosition);
                        }
                        break;
                    }
                    newRow += bDirection[0];
                    newCol += bDirection[1];
                }
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            int[][] queenDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1},{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] qDirection : queenDirections) {
                newRow = myPosition.getRow() + qDirection[0];
                newCol = myPosition.getColumn() + qDirection[1];
                while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition queenNewPosition = new ChessPosition(newRow, newCol);
                    if (board.getPiece(queenNewPosition) == null) {
                        validMoves.add(new ChessMove(myPosition, queenNewPosition, null));
                        System.out.println("Valid move: " + myPosition + " to " + queenNewPosition);
                    } else {
                        if (board.getPiece(queenNewPosition).getTeamColor() != myColor) {
                            validMoves.add(new ChessMove(myPosition, queenNewPosition, null));
                            System.out.println("Valid move (capture): " + myPosition + " to " + queenNewPosition);
                        }
                        break;
                    }
                    newRow += qDirection[0];
                    newCol += qDirection[1];
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
