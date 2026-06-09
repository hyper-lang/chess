package client;

import java.util.Scanner;

import model.AuthData;

public class ChessClient {
    private ServerFacade server;
    private Scanner scanner = new Scanner(System.in);

    private PreLoginClient preLoginClient;
    private PostLoginClient postLoginClient;
    private GameplayClient gameplayClient;
    private AuthData auth;

    private boolean loggedIn = false;
    private boolean running = true;

    public ChessClient(String url) throws Exception {
        server = new ServerFacade("http://" + url);
        gameplayClient = new GameplayClient("ws://" + url + "/ws");
        preLoginClient = new PreLoginClient(server);
    }

    public void run(){
        String input;
        while(running){
            try{
                if(loggedIn){
                    System.out.print("[LOGGED_IN] >>> ");
                    input = scanner.nextLine();

                    if(input.toLowerCase().startsWith("quit")){
                        running = false;
                        System.out.println("Thanks for playing!");
                        break;
                    }

                    auth = postLoginClient.postLoginInput(input);

                    if(auth == null){
                        loggedIn = false;
                    }
                } else{
                    System.out.print("[LOGGED_OUT] >>> ");
                    input = scanner.nextLine();

                    if(input.toLowerCase().startsWith("quit")){
                        running = false;
                        System.out.println("Thanks for playing!");
                        break;
                    }

                    auth = preLoginClient.preLoginInput(input);

                    if(auth != null){
                        loggedIn = true;
                        postLoginClient = new PostLoginClient(server, gameplayClient, auth);
                    }
                }
            } catch(Exception e){
                System.out.println(e.getMessage() + "\nPlease try again.");
            }
        }
    }
}
