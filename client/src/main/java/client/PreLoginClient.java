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

        //add error handling
        if(words[0].toLowerCase().equals("register")){
            user = new UserData(words[1], words[2], words[3]);
            System.out.println("Registered!");
            return server.register(user);
        } else if(words[0].toLowerCase().equals("login")){
            user = new UserData(words[1], words[2], null);
            AuthData auth = server.login(user);
            System.out.println("Logged in as " + auth.username());
            return auth;
        } else if(words[0].toLowerCase().equals("help")){
            System.out.println(help);
            return null;
        }
        System.out.println("Invalid command. Type 'help' to view list of commands");
        return null;
    }
}
