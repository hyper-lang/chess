package client;

import model.AuthData;
import model.UserData;

public class PreLoginClient {
    private ServerFacade server;
    private String help;
    
    public PreLoginClient(ServerFacade server){
        this.server = server;
        help = """
                register <username> <password> <email>
                login <username> <password>
                quit
                help
                """;
    }

    public AuthData preLoginInput(String input) throws Exception {
        String[] words = input.split("\\s+");
        UserData user;
        AuthData auth;

        if(words[0].toLowerCase().equals("register")){
            if(words.length != 4){
                throw new Exception("Incorrect amount of arguments!");
            }
            user = new UserData(words[1], words[2], words[3]);
            auth = server.register(user);
            if(auth.authToken() != null){
                System.out.println("Registered!");
            } else{
                throw new Exception("Registration error!");
            }
            return auth;
        } else if(words[0].toLowerCase().equals("login")){
            if(words.length != 3){
                throw new Exception("Incorrect amount of arguments!");
            }
            user = new UserData(words[1], words[2], null);
            auth = server.login(user);
            if(auth.authToken() != null){
                System.out.println("Logged in as " + auth.username());
            } else{
                throw new Exception("Authentication error!");
            }
            return auth;
        } else if(words[0].toLowerCase().equals("help")){
            System.out.println(help);
            return null;
        }
        System.out.println("Invalid command. Type 'help' to view list of commands");
        return null;
    }
}
