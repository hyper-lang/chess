package websocket.commands;

import chess.ChessMove;

public class MoveUserGameCommand extends UserGameCommand {
    private ChessMove move;

    public MoveUserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move){
        super(commandType, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}