package chess;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    ChessBoard boardCopy;
    private boolean isGameOver = false;
    private TeamColor teamTurn = TeamColor.WHITE;


    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
//        throw new RuntimeException("Not implemented");
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
//        throw new RuntimeException("Not implemented");
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
//        throw new RuntimeException("Not implemented");
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> valid_moves = piece.pieceMoves(board,startPosition);
        for (ChessMove move : valid_moves) {
            try {
                tryMove(move);
            }
            catch (InvalidMoveException e) {
                valid_moves.remove(move);
            }
        }
    return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void tryMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.startPosition;
        ChessPosition endPosition = move.endPosition;
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor;
        if (piece != null) {
            teamColor = piece.getTeamColor();
        }
        else {
            throw new InvalidMoveException("No piece at this position");
        }

        for (ChessMove move_1 : piece.pieceMoves(boardCopy,startPosition)) {
            if ((move_1 == move) && (getTeamTurn()==teamColor)) {
                try {
                    boardCopy = (ChessBoard) board.clone();
                    boardCopy.addPiece(startPosition,null);
                    boardCopy.addPiece(endPosition,piece);
                    if (is_pinned(teamColor)) {
                        throw new InvalidMoveException("Move puts own king in check");
                    }

                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
                }

            }
        }
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");

        ChessPosition startPosition = move.startPosition;
        ChessPosition endPosition = move.endPosition;
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor;
        if (piece != null) {
            teamColor = piece.getTeamColor();
        }
        else {
            throw new InvalidMoveException("No piece at this position");
        }

        for (ChessMove move_1 : validMoves(startPosition)) {
            if ((move_1 == move) && (getTeamTurn()==teamColor)) {

                board.addPiece(startPosition,null);
                board.addPiece(endPosition,piece);
                    if (teamColor == TeamColor.WHITE) {
                        setTeamTurn(TeamColor.BLACK);
                    }
                    if (teamColor == TeamColor.BLACK) {
                        setTeamTurn(TeamColor.WHITE);
                    }



            }
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int i = 1;i<=8;i++) {
            for (int j = 1;j<=8;j++) {
                ChessPosition position = new ChessPosition(i,j);
                if (board.getPiece(kingPosition) != null) {
                    if ((board.getPiece(position).getTeamColor() == teamColor) && (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING)) {
                        kingPosition = new ChessPosition(i, j);
                    }
                }
            }
        }
        return kingPosition;
    }

    public boolean is_pinned(TeamColor teamColor) {
        boolean isPinned = false;
        ChessPosition kingPosition = getKingPosition(teamColor);

        for (int i = 1;i<=8;i++) {
            for (int j = 1;j<=8;j++) {
                ChessPosition position = new ChessPosition(i,j);
                if (boardCopy.getPiece(position) != null) {
                    if ((boardCopy.getPiece(position).getTeamColor() != teamColor)) {
                        ChessPiece piece = boardCopy.getPiece(position);
                        Collection<ChessMove> validMoves = piece.pieceMoves(boardCopy, position);
                        for (ChessMove move : validMoves) {
                            if ((move.endPosition == kingPosition)) {
                                isPinned = true;
                                break;
                            }
                        }
                    }
                }

                if (isPinned) {
                    break;
                }
            }
            if (isPinned) {
                break;
            }
        }
        return isPinned;
    }
    public boolean isInCheck(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented")
        boolean isInCheck = false;
        ChessPosition kingPosition = getKingPosition(teamColor);

        for (int i = 1;i<=8;i++) {
            for (int j = 1;j<=8;j++) {
                ChessPosition position = new ChessPosition(i,j);
                if (board.getPiece(position) != null) {
                    if ((board.getPiece(position).getTeamColor() != teamColor)) {
                        ChessPiece piece = board.getPiece(position);
                        Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
                        for (ChessMove move : validMoves) {
                            if ((move.endPosition == kingPosition)) {
                                isInCheck = true;
                                break;
                            }
                        }
                    }
                }

                if (isInCheck) {
                    break;
                }
            }
            if (isInCheck) {
                break;
            }
        }
        return isInCheck;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        boolean isInCheckmate = false;
        ChessPosition kingPosition = getKingPosition(teamColor);
        ChessPiece king = board.getPiece(kingPosition);
        if (isInCheck(teamColor) && (king.pieceMoves(board,kingPosition)) == null) {
            isInCheckmate = true;
        }
        return isInCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        boolean isInStalemate = false;
        ChessPosition kingPosition = getKingPosition(teamColor);
        ChessPiece king = board.getPiece(kingPosition);
        if (!isInCheck(teamColor) && (king.pieceMoves(board,kingPosition) == null)) {
            isInStalemate = true;
        }
        return isInStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
//        throw new RuntimeException("Not implemented");
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
//        throw new RuntimeException("Not implemented");
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return isGameOver == chessGame.isGameOver && Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, isGameOver, teamTurn);
    }
}
