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
        if (userDAO.getUser(user.username()) != null) {
            throw new DataAccessException("username taken");
        }
        userDAO.createUser(user);
        return login(user);
    }

    public AuthData login(UserData user) throws DataAccessException{
        UserData existingUser = userDAO.getUser(user.username());
        if (existingUser == null) {
            throw new DataAccessException("unauthorized");
        }
        AuthData authData = new AuthData(generateToken(), user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws DataAccessException{
        authDAO.deleteAuth(authToken);
    }
}
