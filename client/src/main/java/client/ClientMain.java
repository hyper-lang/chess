package client;

import chess.*;
import client.PrintBoard;

public class ClientMain {
    public static void main(String[] args) {
        // var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("Welcome to Chess! Type help to get started");
        ChessClient client = new ChessClient("http://localhost");
        client.run();
    }
}
