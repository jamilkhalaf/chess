package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
//        throw new RuntimeException("Not implemented");
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
//        throw new RuntimeException("Not implemented");
        if ((position.getRow()<1) || (position.getRow() > 8) || (position.getColumn() > 8) || (position.getColumn() < 1)) {
        return board[position.getRow()-1][position.getColumn()-1];}

        else {return null;}
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//        throw new RuntimeException("Not implemented");
        //the white pieces
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition position = new ChessPosition(1,1);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(1,2);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(1,3);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        position = new ChessPosition(1,4);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        position = new ChessPosition(1,5);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(1,6);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(1,7);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        position = new ChessPosition(1,8);
        addPiece(position,piece);
        //add white pawns
        for (int i = 1; i<=8; i++) {
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            position = new ChessPosition(2,i);
            addPiece(position,piece);
        }

        //black pieces initialization
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        position = new ChessPosition(8,1);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(8,2);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(8,3);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        position = new ChessPosition(8,4);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        position = new ChessPosition(8,5);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(8,6);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(8,7);
        addPiece(position,piece);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        position = new ChessPosition(1,8);
        addPiece(position,piece);
        //add black pawns
        for (int i = 1; i<=8; i++) {
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            position = new ChessPosition(7,i);
            addPiece(position,piece);
        }

    }
}
