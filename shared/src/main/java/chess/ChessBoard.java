package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPosition[] board;

    public ChessBoard() {
        board = new ChessPosition[64];
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                board[translate(i, j)] = new ChessPosition(i, j);
            }
        }
    }

    /**
     * Translates 2D coordinates to the 1D array
     * 
     * @param i the row index
     * @param j the column index
     * @return the 1D board array index
     */
    private int translate(int i, int j){
        return ((i - 1) * 8) + (j - 1);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[translate(position.getRow(), position.getColumn())].setPiece(piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[translate(position.getRow(), position.getColumn())].getPiece();
    }

    /**
     * Sets 4 corners of a rectangle to a certain piece based on it's quadrant 3 position (helper function for resetBoard)
     * 
     * @param i the row coordinate for the piece
     * @param j the column coordinate for the piece
     * @param piece the piece to set
     */
    private void setRectangle(int i, int j, ChessPiece.PieceType piecetype){
        board[translate(i, j)].setPiece(new ChessPiece(ChessGame.TeamColor.WHITE, piecetype));
        board[translate(9 - i, j)].setPiece(new ChessPiece(ChessGame.TeamColor.WHITE, piecetype));
        board[translate(i, 9 - j)].setPiece(new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
        board[translate(9 - i, 9 - j)].setPiece(new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(board);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessBoard other = (ChessBoard) obj;
        if (!Arrays.equals(board, other.board))
            return false;
        return true;
    }
}
