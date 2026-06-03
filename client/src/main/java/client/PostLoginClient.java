package client;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public class PostLoginClient {
    private ServerFacade server;
    private AuthData auth;
    private String help;

    public PostLoginClient(ServerFacade server, AuthData auth){
        this.server = server;
        this.auth = auth;
        help = """
                create <NAME>
                list
                join <ID> [WHITE|BLACK]
                observe <ID>
                logout
                quit
                help
                """;
    }

    //add error handling
    //map display numbers to game numbers
    public boolean postLoginInput(String input) throws Exception {
        String[] words = input.split("\\s+");

        if(words[0].toLowerCase().equals("create")){
            int gameID = server.createGame(auth, words[1]);
            System.out.println("Created game number " + gameID);
        } else if(words[0].toLowerCase().equals("list")){
            Collection<GameData> games = server.listGames(auth);
            int index = 1;
            for(GameData i : games){
                System.out.println(index + " " + i.gameName());
                index += 1;
            }
        } else if(words[0].toLowerCase().equals("join")){
            int gameID = Integer.parseInt(words[1]);
            String color = words[2].toUpperCase();
            server.joinGame(auth, color, gameID);
        } else if(words[0].toLowerCase().equals("observe")){
            int gameID = Integer.parseInt(words[1]);
            //rest to be implemented in phase 6
        } else if(words[0].toLowerCase().equals("logout")){
            server.logout(auth);
            return false;
        } else if(words[0].toLowerCase().equals("help")){
            System.out.println(help);
        }
        return true;
    }
}
