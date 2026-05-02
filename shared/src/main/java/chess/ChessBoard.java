package chess;

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
     * @param row the row coordinate for the piece
     * @param col the column coordinate for the piece
     * @param piece the piece to set
     */
    private void setRectangle(int row, int col, ChessPiece.PieceType piecetype){
        addPiece(new ChessPosition(row, col), new ChessPiece(ChessGame.TeamColor.WHITE, piecetype));
        addPiece(new ChessPosition(row, 9 - col), new ChessPiece(ChessGame.TeamColor.WHITE, piecetype));
        addPiece(new ChessPosition(9 - row, col), new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
        addPiece(new ChessPosition(9 - row, 9 - col), new ChessPiece(ChessGame.TeamColor.BLACK, piecetype));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new HashMap<>();
        setRectangle(1, 1, ChessPiece.PieceType.ROOK);
        setRectangle(1, 2, ChessPiece.PieceType.KNIGHT);
        setRectangle(1, 3, ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        for(int col = 1; col < 5; col++){
            setRectangle(2, col, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
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
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }
}
