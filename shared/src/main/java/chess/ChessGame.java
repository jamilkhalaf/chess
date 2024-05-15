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
    ChessPosition rightRookPosition;
    ChessPosition newRightRookPosition;
    ChessPosition leftRookPosition;
    ChessPosition newLeftRookPosition;
    private boolean canCastleLeft = false;
    private boolean canCastleRight = false;
    private ChessBoard board = new ChessBoard();
    private boolean isGameOver = false;
    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessMove lastMove;
    private boolean isEnPassant = false;
    private ChessPosition enPassantPosition;

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


    public void setLastMove(ChessMove lastMove) {
        this.lastMove = lastMove;
    }

    public ChessMove getLastMove() {
        return lastMove;
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

        ChessGame.TeamColor teamColor = piece.getTeamColor();
        int column = startPosition.getColumn();
        // enPassant for pawns
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if ((startPosition.getRow() == 5) && (teamColor == TeamColor.WHITE)) {

                if ((lastMove.endPosition.getColumn() == column + 1) && (lastMove.endPosition.getRow() == 5)) {
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1),null));
                    isEnPassant = true;
                    enPassantPosition = lastMove.endPosition;
                }
                if ((lastMove.endPosition.getColumn() == column -1)&& (lastMove.endPosition.getRow() == 5)) {
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1),null));
                    isEnPassant = true;
                    enPassantPosition = lastMove.endPosition;
                }
            }
            else if ((startPosition.getRow() == 4) && (teamColor == TeamColor.BLACK)) {
                if ((lastMove.endPosition.getColumn() == column + 1)&& (lastMove.endPosition.getRow() == 4)) {
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1),null));
                    isEnPassant = true;
                    enPassantPosition = lastMove.endPosition;
                }
                if ((lastMove.endPosition.getColumn() == column -1)&& (lastMove.endPosition.getRow() == 4)) {
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1),null));
                    isEnPassant = true;
                    enPassantPosition = lastMove.endPosition;
                }
            }

        }

        if ((piece.getPieceType() == ChessPiece.PieceType.KING) && !isInCheck(teamColor)) {
            if (teamColor == TeamColor.WHITE) {
                if ((startPosition.getRow() == 1) && (startPosition.getColumn() == 5) && (board.getPiece(new ChessPosition(1,1)).getPieceType()== ChessPiece.PieceType.ROOK) && emptyForCastle(teamColor,new ChessPosition(1,1))){
                    canCastleLeft = true;
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(1,3),null));
                    leftRookPosition = new ChessPosition(1,1);
                    newLeftRookPosition = new ChessPosition(1,4);
                }
                if ((startPosition.getRow() == 1) && (startPosition.getColumn() == 5) && (board.getPiece(new ChessPosition(1,8)).getPieceType()== ChessPiece.PieceType.ROOK) && emptyForCastle(teamColor,new ChessPosition(1,8))) {
                    canCastleRight = true;
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(1,7),null));
                    rightRookPosition = new ChessPosition(1,8);
                    newRightRookPosition = new ChessPosition(1,6);
                }
            }
            else {
                if ((startPosition.getRow() == 8) && (startPosition.getColumn() == 5) && (board.getPiece(new ChessPosition(8,1)).getPieceType()== ChessPiece.PieceType.ROOK) && emptyForCastle(teamColor,new ChessPosition(8,1))) {
                    canCastleLeft = true;
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(8,3),null));
                    leftRookPosition = new ChessPosition(8,1);
                    newLeftRookPosition = new ChessPosition(8,4);
                }
                if ((startPosition.getRow() == 8) && (startPosition.getColumn() == 5) && (board.getPiece(new ChessPosition(8,8)).getPieceType()== ChessPiece.PieceType.ROOK) && emptyForCastle(teamColor,new ChessPosition(8,8))) {
                    canCastleRight = true;
                    valid_moves.add(new ChessMove(startPosition,new ChessPosition(8,7),null));
                    rightRookPosition = new ChessPosition(8,8);
                    newRightRookPosition = new ChessPosition(8,6);
                }
            }
        }

        Iterator<ChessMove> iterator = valid_moves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            System.out.println(move.endPosition);

            try {
                tryMove(move);

            } catch (InvalidMoveException e) {
                iterator.remove();
            }



        }
        return valid_moves;
    }

     public boolean emptyForCastle(TeamColor teamColor, ChessPosition position) {
        boolean empty = false;
        if (teamColor == TeamColor.WHITE) {
            if (position.getColumn() == 1) {
                if ((board.getPiece(new ChessPosition(1,2)) == null) && (board.getPiece(new ChessPosition(1,3)) == null) && (board.getPiece(new ChessPosition(1,4)) == null)) {
                    empty = true;
                }

            }
            if (position.getColumn() == 8) {
                if ((board.getPiece(new ChessPosition(1,6)) == null) && (board.getPiece(new ChessPosition(1,7)) == null)) {
                    empty = true;
                }

            }
        }
        else {
            if (position.getColumn() == 1) {
                if ((board.getPiece(new ChessPosition(8,2)) == null) && (board.getPiece(new ChessPosition(8,3)) == null) && (board.getPiece(new ChessPosition(8,4)) == null)) {
                    empty = true;
                }

            }
            if (position.getColumn() == 8) {
                if ((board.getPiece(new ChessPosition(8,6)) == null) && (board.getPiece(new ChessPosition(8,7)) == null)) {
                    empty = true;
                }

            }
        }
        return empty;
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
        Collection<ChessMove> valid_moves;
        if (piece != null) {
            color = piece.getTeamColor();
        }
        else {
            throw new InvalidMoveException();
        }


        valid_moves = validMoves(move.getStartPosition());

        if (valid_moves.isEmpty()) {
            throw new InvalidMoveException();
        }
        boolean foundMove = false;
        for (ChessMove move_1 : valid_moves) {
            if ((move_1.equals(move)) && (teamTurn == color)) {
                ChessPiece.PieceType promotedPiece = move_1.promotionPiece;
                foundMove = true;
                setLastMove(move);
                System.out.println(board.getPiece(startPosition).getPieceType());
                board.addPiece(startPosition,null);
                if ((piece.getPieceType() == ChessPiece.PieceType.PAWN) && ((endPosition.getRow() == 1) || (endPosition.getRow() == 8))) {
                    board.addPiece(endPosition, new ChessPiece(color,promotedPiece));
                }
                else if ((piece.getPieceType() == ChessPiece.PieceType.PAWN) && (isEnPassant)) {
                    board.addPiece(endPosition, piece);
                    board.addPiece(enPassantPosition,null);
                }
                else if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (canCastleLeft) && (move.endPosition.getColumn()==3) && !isInCheck(color)) {
                    board.addPiece(endPosition, piece);
                    board.addPiece(leftRookPosition,null);
                    board.addPiece(newLeftRookPosition, new ChessPiece(color, ChessPiece.PieceType.ROOK));
                }
                else if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (canCastleRight) && (move.endPosition.getColumn()==7) && !isInCheck(color)) {
                    board.addPiece(endPosition, piece);
                    board.addPiece(rightRookPosition,null);
                    board.addPiece(newRightRookPosition, new ChessPiece(color, ChessPiece.PieceType.ROOK));
                }
                else {
                    board.addPiece(endPosition, piece);
                }
                System.out.println(board.getPiece(endPosition).getPieceType());
                if (color == TeamColor.BLACK) {
                    setTeamTurn(TeamColor.WHITE);
                }
                else {
                    setTeamTurn(TeamColor.BLACK);
                }
            }
        }
        if (!foundMove) {
            throw new InvalidMoveException();
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
                isGameOver = true;
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
                isGameOver = true;
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
