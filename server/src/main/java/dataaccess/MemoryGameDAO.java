package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> games = new HashMap<>();
    int count;

    public MemoryGameDAO(){
        count = 1;
    }

    public int createGame(GameData game) throws DataAccessException{
        games.put(count, game);
        return count++;
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(gameID);
    }

    public Collection<GameData> listGames() throws DataAccessException{
        Collection<GameData> allGames = new ArrayList<>();
        for(GameData i : games.values()){
            allGames.add(i);
        }
        return allGames;
    }

    public void updateGames(int gameID, GameData game) throws DataAccessException{
        games.replace(gameID, game);
    }

    public void clear() throws DataAccessException{
        games.clear();
    }
}
