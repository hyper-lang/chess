package websocket.commands;

import chess.ChessMove;

public class MoveUserGameCommand extends UserGameCommand {
    private ChessMove move;

    public MoveUserGameCommand(String authToken, Integer gameID, ChessMove move){
        super(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}