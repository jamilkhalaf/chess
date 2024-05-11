package chess;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
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
        Iterator<ChessMove> iterator = valid_moves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            try {
                tryMove(move);
            } catch (InvalidMoveException e) {
                iterator.remove();
            }
        }
        return valid_moves;
    }


    public void tryMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");

        ChessPosition startPosition = move.startPosition;
        ChessPosition endPosition = move.endPosition;
        ChessPiece piece = board.getPiece(startPosition);
        ChessPiece eatenPiece = null;
        if (board.getPiece(endPosition) != null) {
            eatenPiece = board.getPiece(endPosition);
        }

        ChessGame.TeamColor teamColor;
        if (board.getPiece(startPosition) != null) {
            teamColor = piece.getTeamColor();
        }
        else {

            throw new InvalidMoveException();
        }
        System.out.println(move.endPosition);


        board.addPiece(startPosition,null);
        board.addPiece(endPosition,piece);
        if (isInCheck(teamColor)) {
            board.addPiece(startPosition,piece);
            board.addPiece(endPosition,eatenPiece);
            throw new InvalidMoveException();
        }
        board.addPiece(startPosition,piece);
        board.addPiece(endPosition,eatenPiece);





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
        ChessGame.TeamColor color;

        if (piece != null) {
            color = piece.getTeamColor();
        }
        else {
            throw new InvalidMoveException();
        }


        Collection<ChessMove> valid_moves = validMoves(move.getStartPosition());
        for (ChessMove move_1 : valid_moves) {
            if (move_1.equals(move)) {
                board.addPiece(startPosition,null);
                board.addPiece(endPosition,piece);
                if (color == TeamColor.BLACK) {
                    setTeamTurn(TeamColor.WHITE);
                }
                else {
                    setTeamTurn(TeamColor.BLACK);
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
                if (board.getPiece(position) != null) {
                    if ((board.getPiece(position).getTeamColor() == teamColor) && (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING)) {
                        kingPosition = new ChessPosition(i, j);
                    }
                }
            }
        }
        return kingPosition;
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
                            System.out.println(move.endPosition);
                            if ((move.endPosition.equals(kingPosition))) {
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
    public boolean pieceCanCover(TeamColor teamColor) {
        boolean canCover = false;
        for (int i = 1;i<=8;i++) {
            for (int j = 1;j<=8;j++) {
                ChessPosition position = new ChessPosition(i,j);
                if (board.getPiece(position) != null) {
                    if ((board.getPiece(position).getTeamColor() == teamColor)) {
                        ChessPiece piece = board.getPiece(position);
                        Collection<ChessMove> validMoves = validMoves(position);
                        if (!validMoves.isEmpty()) {
                            canCover = true;
                        }
                    }
                }

                if (canCover) {
                    break;
                }
            }
            if (canCover) {
                break;
            }
        }
        return canCover;
    }
    public boolean isInCheckmate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        boolean isInCheckmate = false;
        ChessPosition kingPosition = getKingPosition(teamColor);
        Collection<ChessMove> kingMoves = validMoves(kingPosition);
        if ((isInCheck(teamColor)) && (kingMoves.isEmpty())) {
            if (!pieceCanCover(teamColor)) {
                isInCheckmate = true;
            }
            else {
                isInCheckmate= false;
            }
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

        Collection<ChessMove> kingMoves = validMoves(kingPosition);
        if ((!isInCheck(teamColor)) && (kingMoves.isEmpty())) {
            if (!pieceCanCover(teamColor)) {
                isInStalemate = true;
            }

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
