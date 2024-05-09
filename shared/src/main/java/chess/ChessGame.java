package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private boolean isGameOver = false;
    private TeamColor teamTurn = TeamColor.WHITE;


    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
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
        ChessGame.TeamColor color = board.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> valid_moves = piece.pieceMoves(board,startPosition);
        for (ChessMove move : valid_moves) {
            try {
                makeMove(move);
            }
            catch (InvalidMoveException e) {
                System.out.println("Invalid move");
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

    public void makeMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");
        ChessPosition startPosition = move.startPosition;
        ChessPosition endPosition = move.endPosition;
        ChessPiece piece = board.getPiece(startPosition);

        ChessGame.TeamColor teamColor = piece.getTeamColor();
        for (ChessMove move_1 : validMoves(startPosition)) {
            if ((move_1 == move) && (getTeamTurn()==teamColor)) {
                board.addPiece(startPosition,null);
                board.addPiece(endPosition,piece);
                if (isInCheck(teamColor)) {
                    // Reverse the move
                    board.addPiece(startPosition, piece);
                    board.addPiece(endPosition, null);
                    throw new InvalidMoveException("Move puts own king in check");
                }
                else {
                    if (teamColor == TeamColor.WHITE) {
                        setTeamTurn(TeamColor.BLACK);
                    }
                    if (teamColor == TeamColor.BLACK) {
                        setTeamTurn(TeamColor.WHITE);
                    }
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
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
