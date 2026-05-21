package service;

import model.AuthData;
import model.UserData;
import dataaccess.*;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (userDAO.getUser(user.username()) != null) {
            throw new DataAccessException("already taken");
        }
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("bad request");
        }
        userDAO.createUser(user);
        return login(user);
    }

    public AuthData login(UserData user) throws DataAccessException {
        if (user == null || user.username() == null || user.password() == null) {
            throw new DataAccessException("bad request");
        }
        UserData existingUser = userDAO.getUser(user.username());
        if (existingUser == null || !existingUser.password().equals(user.password())) {
            throw new DataAccessException("unauthorized");
        }
        AuthData authData = new AuthData(generateToken(), user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
