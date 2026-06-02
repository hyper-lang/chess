package client;

import model.AuthData;
import model.UserData;

public class PreloginClient {
    private ServerFacade server;
    private String help;
    
    public PreloginClient(){
        server = new ServerFacade("https://localhost");
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

        if(words[0].toLowerCase() == "register"){
            user = new UserData(words[1], words[2], words[3]);
            return server.register(user);
        } else if(words[0].toLowerCase() == "login"){
            user = new UserData(words[1], words[2], null);
            return server.login(user);
        } else if(words[0].toLowerCase() == "help"){
            System.out.println(help);
            return null;
        }
        return null;
    }
}
