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
        if(row >= 1 && row <= 8 && col >= 1 && col <= 8){
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
     * Helper function for the pawnMoves function. Determines if a pawn can take
     * 
     * @param board
     * @param position
     * @param color
     * @return true if the piece can take at that square
     */
    private boolean canTake(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        if(board.getPiece(position) != null && board.getPiece(position).pieceColor != color){
            return true;
        }
        return false;
    }

    /**
     * Helper function for knight and king. Determines if the square can be landed on.
     * 
     * @param board
     * @param position
     * @param color
     * @return true if a knight or king can move to that position
     */
    private boolean canMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        if(canLand(board, position, color) || canTake(board, position, color)){
            return true;
        }
        return false;
    }

    /**
     * A helper function for knight and king moves. Validates a list of relative coordinates
     * 
     * @param board
     * @param position
     * @param color
     * @return All valid moves for the given scenario
     */
    private Collection<ChessMove> validateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, int[][] relativePositions){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition end;

        for(int i = 0; i < 8; i++){
            end = new ChessPosition(row + relativePositions[i][0], col + relativePositions[i][1]);
            if(checkBounds(end) && canMove(board, end, pieceColor)){
                moves.add(new ChessMove(position, end, null));
            }
        }

        return moves;
    }

    /**
     * Checks all the moves for one direction of rook or bishop moves, and adds them to the passed collection
     */
    private void checkOneDirection(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int[] direction){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int count = 1;
        ChessPosition end;

        while(true){
            end = new ChessPosition(row + direction[0] * count, col + direction[1] * count);
            if(!checkBounds(end)){
                break;
            }
            if(canTake(board, end, pieceColor)){
                moves.add(new ChessMove(myPosition, end, null));
                break;
            } else if(canLand(board, end, pieceColor)){
                moves.add(new ChessMove(myPosition, end, null));
            }else{
                break;
            }
            count++;
        }
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
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for(int i = 0; i < 4; i++){
            checkOneDirection(board, myPosition, moves, directions[i]);
        }
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        int[][] positions = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1,  -2}};
        return validateMoves(board, myPosition, pieceColor, positions);
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        for(int i = 0; i < 4; i++){
            checkOneDirection(board, myPosition, moves, directions[i]);
        }
        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(rookMoves(board, myPosition));
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        int[][] positions = {{1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0,  -1}};
        return validateMoves(board, myPosition, pieceColor, positions);
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
