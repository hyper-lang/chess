package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor turn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if(piece == null){
            return null;
        }
        Collection<ChessMove> legalMoves = new ArrayList<>();
        for(ChessMove i : piece.pieceMoves(board, startPosition)){
            ChessBoard hypothetical = new ChessBoard(board);
            hypothetical.movePiece(i);

            if(!calculateCheck(piece.getTeamColor(), hypothetical)){
                legalMoves.add(i);
            }
        }
        return legalMoves;
    }

    /**
     * Helper function for checking hypothetical moves
     * @param startPosition
     * @param moveBoard
     * @return a collection of legal moves from the given board.
     */
    public Collection<ChessMove> semivalidMoves(ChessPosition startPosition, ChessBoard moveBoard) {
        ChessPiece piece;
        piece = moveBoard.getPiece(startPosition);
        if(piece != null){
            return piece.pieceMoves(moveBoard, startPosition);
        }
        return null;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        
        if(piece == null){
            throw new InvalidMoveException();
        }

        if(piece.getTeamColor() != turn){
            throw new InvalidMoveException();
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());

        if(moves == null || !moves.contains(move)){
            throw new InvalidMoveException();
        }

        board.movePiece(move);
        turn = turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Helper function for legalMoves and calculateCheck. Gets all the possible moves for all pieces of one color.
     * @param team
     * @return a collection of all possible moves all pieces of one team can make.
     */
    private Collection<ChessMove> possibleMoves(TeamColor team, ChessBoard moveBoard){
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        ChessPosition current;
        ChessPiece temp;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                current = new ChessPosition(j, i);
                temp = moveBoard.getPiece(current);
                if(temp != null && temp.getTeamColor() == team){
                    allMoves.addAll(semivalidMoves(current, moveBoard));
                }
            }
        }
        return allMoves;
    }

    private Collection<ChessMove> legalMoves(TeamColor team, ChessBoard moveBoard){
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        ChessPosition current;
        ChessPiece temp;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                current = new ChessPosition(j, i);
                temp = moveBoard.getPiece(current);
                if(temp != null && temp.getTeamColor() == team){
                    allMoves.addAll(validMoves(current));
                }
            }
        }
        return allMoves;
    }


    /**
     * Helper function for isInCheck. Built to accept board as parameter to also be used in isInCheckMate
     * @param teamColor
     * @param board
     * @return true if the king is in check.
     */
    private boolean calculateCheck(TeamColor teamColor, ChessBoard board){
        Collection<ChessMove> allMoves = new ArrayList<>();
        TeamColor opposingColor = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition kingPosition = board.findKing(teamColor);
        allMoves = possibleMoves(opposingColor, board);
        for(ChessMove i : allMoves){
            if(i.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return calculateCheck(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }

        for(ChessMove i : possibleMoves(teamColor, board)){
            ChessBoard hypothetical = new ChessBoard(board);
            hypothetical.movePiece(i);
            if(!calculateCheck(teamColor, hypothetical)){
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor) && legalMoves(teamColor, board).isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        result = prime * result + ((turn == null) ? 0 : turn.hashCode());
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
        ChessGame other = (ChessGame) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        if (turn != other.turn)
            return false;
        return true;
    }

    
}
