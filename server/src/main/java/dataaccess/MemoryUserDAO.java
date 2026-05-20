package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private Map<String, String[]> users = new HashMap<>();

    public void createUser(UserData user){
        users.put(user.username(), new String[]{user.email(), user.password()});
    }

    public UserData getUser(String username){
        String[] data = users.get(username);
        return new UserData(username, data[0], data[1]);
    }

    public void clear(){
        users = new HashMap<>();
    }
}
