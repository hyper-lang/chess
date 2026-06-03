package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess! Type help to get started");
        ChessClient client = new ChessClient("http://localhost:8000");
        client.run();
    }
}