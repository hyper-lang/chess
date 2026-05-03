package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = switch(type){
            case PAWN -> pawnMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
        };

        return moves;
    }

    /**
     * Helper function that determines if a position is within the bounds of the board
     * 
     * @param position
     * @return true if the position is on the board
     */
    private boolean checkBounds(ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
        if(row >= 1 && row <= 8 && col >= 1 && row <= 8){
            return true;
        }
        return false;
    }

    /**
     * Determines if a square can be moved to on the board
     * 
     * @param board
     * @param position
     * @param color
     * @return true if the square can be moved to
     */
    private boolean canLand(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        if(board.getPiece(position) == null){
            return true;
        }
        return false;
    }

    /**
     * Helper function for pawn move. Determines if a pawn can take
     * 
     * @param board
     * @param position
     * @param color
     * @return true if the pawn can take at that square
     */
    private boolean canTake(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        if(board.getPiece(position) != null && board.getPiece(position).pieceColor != color){
            return true;
        }
        return false;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean lastFile = (pieceColor == ChessGame.TeamColor.WHITE && row == 7) || (pieceColor == ChessGame.TeamColor.BLACK && row == 2);
        boolean startingFile = (pieceColor == ChessGame.TeamColor.WHITE && row == 2) || (pieceColor == ChessGame.TeamColor.BLACK && row == 7);

        //needs to be written better
        if(lastFile){
            ChessPosition end = new ChessPosition(row + direction, col - 1);
            if(canTake(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                moves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
            }
            end = new ChessPosition(row + direction, col + 1);
            if(canTake(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                moves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
            }
            end = new ChessPosition(row + direction, col);
            if(canLand(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                moves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
            }
        } else{
            ChessPosition end = new ChessPosition(row + direction, col - 1);
            if(canTake(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, null));
            }
            end = new ChessPosition(row + direction, col + 1);
            if(canTake(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, null));
            }
            end = new ChessPosition(row + direction, col);
            if(canLand(board, end, pieceColor) && checkBounds(end)){
                moves.add(new ChessMove(myPosition, end, null));
                end = new ChessPosition(row + direction * 2, col);
                if(startingFile && canLand(board, end, pieceColor)){
                    moves.add(new ChessMove(myPosition, end, null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        ChessPiece other = (ChessPiece) obj;
        if (pieceColor != other.pieceColor)
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
