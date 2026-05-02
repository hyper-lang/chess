package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;
    private ChessPiece piece;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
        setPiece(null);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    /**
     * Sets the piece at this position
     * 
     * @param piece the piece being set
     */
    public void setPiece(ChessPiece piece){
        this.piece = piece;
    }

        /**
     * Returns the piece at this position
     * 
     * @return the piece stored in the position
     */
    public ChessPiece getPiece(){
        return this.piece;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        result = prime * result + ((piece == null) ? 0 : piece.hashCode());
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
        ChessPosition other = (ChessPosition) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        if (piece == null) {
            if (other.piece != null)
                return false;
        } else if (!piece.equals(other.piece))
            return false;
        return true;
    }

    
}
