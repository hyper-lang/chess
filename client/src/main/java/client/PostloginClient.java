package client;

import model.AuthData;

public class PostloginClient {
    private ServerFacade server;
    private AuthData auth;

    public PostloginClient(String url, AuthData auth){
        server = new ServerFacade(url);
        this.auth = auth;
    }

    public boolean postLoginInput(String input){
        String[] words = input.split("\\s+");

        if(words[0].toLowerCase().equals("create")){}
        else if(words[0].toLowerCase().equals("list")){}
        else if(words[0].toLowerCase().equals("join")){}
        else if(words[0].toLowerCase().equals("observe")){}
        else if(words[0].toLowerCase().equals("logout")){}
        else if(words[0].toLowerCase().equals("quit")){}
        else if(words[0].toLowerCase().equals("help")){}
        return true;
    }
}
