package chess;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private Map<ChessPosition, ChessPiece> board = new HashMap<>();

    public ChessBoard() {
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                board.put(new ChessPosition(j, i), null);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    /**
     * Sets 4 corners of a rectangle to a certain piece based on it's quadrant 3 position (helper function for resetBoard)
     * 
     * @param j the row coordinate for the piece
     * @param i the column coordinate for the piece
     * @param piece the piece to set
     */
    private void setRectangle(int i, int j, ChessPiece.PieceType piecetype){
        if(piecetype == null){
            addPiece(new ChessPosition(j, i), null)
            addPiece(new ChessPosition(j, 9 - i), null);
            addPiece(new ChessPosition(9 - j, i), null);
            addPiece(new ChessPosition(9 - j, 9 - i), null);
        }else {
            addPiece(new ChessPosition(j, i), new ChessPiece(ChessGame.TeamColor.WHITE, piecetype))
            addPiece(new ChessPosition(j, 9 - i), new ChessPiece(ChessGame.TeamColor.WHITE, piecetype));
            addPiece(new ChessPosition(9 - j, i), new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
            addPiece(new ChessPosition(9 - j, 9 - i), new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
        }
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
