package service;

import model.UserData;
import dataaccess.*;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user){
        userDAO.createUser(user);
    }

    public AuthData login(){}
    
    public void logout(String authToken){}
}
