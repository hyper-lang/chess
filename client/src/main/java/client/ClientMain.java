package client;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Chess! Type help to get started");
        ChessClient client = new ChessClient("localhost:8000");
        client.run();
    }
}