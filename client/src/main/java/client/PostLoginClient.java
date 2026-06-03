package client;

import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private ServerFacade server;
    private AuthData auth;
    private String help;
    private Map<Integer, GameData> gameList;

    public PostLoginClient(ServerFacade server, AuthData auth){
        this.server = server;
        this.auth = auth;
        gameList = new HashMap<>();
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

    public AuthData postLoginInput(String input) throws Exception {
        String[] words = input.split("\\s+");

        if(words[0].toLowerCase().equals("create")){
            if(words.length != 2){
                throw new Exception("Incorrect amount of arguments!");
            }
            int gameID = server.createGame(auth, words[1]);
            System.out.println("Created game number " + gameID);
        } else if(words[0].toLowerCase().equals("list")){
            if(words.length != 1){
                throw new Exception("Incorrect amount of arguments!");
            }
            Collection<GameData> games = server.listGames(auth);
            int index = 1;
            gameList.clear();
            for(GameData i : games){
                System.out.println(index + " " + i.gameName());
                System.out.println("  white: " + i.whiteUsername());
                System.out.println("  black: " + i.blackUsername());
                gameList.put(index, i);
                index += 1;
            }
        } else if(words[0].toLowerCase().equals("join")){
            if(words.length != 3){
                throw new Exception("Incorrect amount of arguments!");
            }
            if(!words[2].toLowerCase().equals("white") || !words[2].toLowerCase().equals("black")){
                throw new Exception("Color must be 'WHITE' or 'BLACK'!");
            }
            int listID = Integer.parseInt(words[1]);
            String color = words[2].toUpperCase();
            server.joinGame(auth, color, gameList.get(listID).gameID());
        } else if(words[0].toLowerCase().equals("observe")){
            if(words.length != 2){
                throw new Exception("Incorrect amount of arguments!");
            }
            int listID = Integer.parseInt(words[1]);
            //rest to be implemented in phase 6
        } else if(words[0].toLowerCase().equals("logout")){
            server.logout(auth);
            return null;
        } else if(words[0].toLowerCase().equals("help")){
            System.out.println(help);
        }
        return auth;
    }
}
