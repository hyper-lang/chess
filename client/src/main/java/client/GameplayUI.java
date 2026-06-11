package client;

import java.util.Scanner;
import chess.*;
import websocket.commands.*;
import model.AuthData;

public class GameplayUI {
    private AuthData auth;
    private int gameID;
    private boolean isWhite;
    private GameplayClient gameplayClient;
    private Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public GameplayUI(String url, AuthData auth, int gameID, boolean isWhite) throws Exception{
        this.auth = auth;
        this.gameID = gameID;
        this.isWhite = isWhite;
        this.gameplayClient = new GameplayClient("ws://" + url + "/ws", isWhite);

        gameplayClient.send(new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth.authToken(), gameID));
    }

    public void run(){
        System.out.println("Welcome to Chess! Type help to get started.");
        while(running){
            System.out.print("[GAMEPLAY] >>> ");
            String input = scanner.nextLine().trim().toLowerCase();
            String[] words = input.split("\\s+");

            try{
                switch(words[0]){
                    case "help" -> help();
                    case "redraw" -> redraw();
                    case "leave" -> leave();
                    case "make" -> makeMove(words);
                    case "resign" -> resign();
                    case "highlight" -> highlight(words);
                    default -> System.out.println("Unknown command. Type 'help' for options.");
                }
            }catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void help(){
        System.out.println("""
                help - display this menu
                redraw - redraw the chess board
                leave - leave the game
                make <FROM> <TO> - make a move (e.g., make e2 e4)
                resign - forfeit the game
                highlight <PIECE> - highlight legal moves for a piece (e.g., highlight e2)
                """);
    }

    private void redraw(){
        if(gameplayClient.getGame() != null){
            PrintBoard.printBoard(gameplayClient.getGame().getBoard(), isWhite);
        }else{
            System.out.println("Board not loaded yet.");
        }
    }

    private void leave() throws Exception{
        gameplayClient.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth.authToken(), gameID));
        running = false;
        System.out.println("Left the game.");
    }

    private void makeMove(String[] words) throws Exception{
        if(words.length < 3 || !words[1].equals("move")){
            if(words.length == 3){
                String from = words[1];
                String to = words[2];
                sendMove(from, to);
            }else if(words.length == 4 && words[1].equals("move")){
                String from = words[2];
                String to = words[3];
                sendMove(from, to);
            }else{
                System.out.println("Invalid format. Use 'make <FROM> <TO>'");
            }
        }else{
            sendMove(words[2], words[3]);
        }
    }

    private void sendMove(String from, String to) throws Exception{
        ChessPosition start = parsePosition(from);
        ChessPosition end = parsePosition(to);
        ChessMove move = new ChessMove(start, end, null);
        gameplayClient.send(new MoveUserGameCommand(auth.authToken(), gameID, move));
    }

    private void resign() throws Exception{
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if(confirm.equals("yes")){
            gameplayClient.send(new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth.authToken(), gameID));
        }
    }

    private void highlight(String[] words){
        if(words.length < 2){
            System.out.println("Use 'highlight <PIECE>'");
            return;
        }
        try{
            ChessPosition pos = parsePosition(words[1]);
            if(gameplayClient.getBoard() != null){
                ChessGame game = gameplayClient.getGame();
                if(game == null){
                    System.out.println("Game state not available for highlighting.");
                    return;
                }
                var moves = game.validMoves(pos);
                if(moves == null || moves.isEmpty()){
                    System.out.println("No legal moves for this piece.");
                }else{
                    PrintBoard.printBoardWithHighlights(game.getBoard(), isWhite, pos, moves);
                }
            }else{
                System.out.println("Board not loaded yet.");
            }
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private ChessPosition parsePosition(String pos) throws Exception{
        if(pos.length() != 2)
            throw new Exception("Invalid position: " + pos);
        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);
        int col = colChar - 'a' + 1;
        int row = rowChar - '0';
        if(col < 1 || col > 8 || row < 1 || row > 8)
            throw new Exception("Position out of bounds: " + pos);
        return new ChessPosition(row, col);
    }
}
