package service;

import model.*;
import dataaccess.*;
import java.util.Collection;

public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken) throws DataAccessException{
        if(authDAO.getAuth(authToken) != null){
            return gameDAO.createGame(new GameData());
        } else{
            throw new DataAccessException("Could not create game");
        }
    }

    public Collection<GameData> listGames() throws DataAccessException{
        return gameDAO.listGames();
    }

    public void joinGame(){}
}
