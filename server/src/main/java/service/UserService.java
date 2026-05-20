package service;

import model.AuthData;
import model.UserData;
import dataaccess.*;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData user) throws DataAccessException{
        userDAO.createUser(user);
        return login(user);
    }

    public AuthData login(UserData user) throws DataAccessException{
        AuthData authData = new AuthData(generateToken(), user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken){}
}
