package client;

import java.util.Scanner;

import model.AuthData;

public class ChessClient {
    private ServerFacade server;
    private Scanner scanner = new Scanner(System.in);

    private PreLoginClient preLoginClient;
    private PostLoginClient postLoginClient;
    private AuthData auth;

    private boolean loggedIn = false;
    private boolean running = true;

    public ChessClient(String url){
        server = new ServerFacade(url);
        preLoginClient = new PreLoginClient(server);
    }

    public void run(){
        String input;
        while(running){
            try{
                if(loggedIn){
                    System.out.print("[LOGGED_IN] >>> ");
                    input = scanner.nextLine();
                    running = postLoginClient.postLoginInput(input);
                } else{
                    System.out.print("[LOGGED_OUT] >>> ");
                    input = scanner.nextLine();
                    auth = preLoginClient.preLoginInput(input);
                    if(auth != null){
                        loggedIn = true;
                        postLoginClient = new PostLoginClient(server, auth);
                    }
                }
            } catch(Exception e){
                System.out.println(e);
                System.out.println("Error occurred! Please try again.");
            }
        }
    }
}
