package dataaccess;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;

public class DaoTests {
    @Test
    public void createUserPositive() throws Exception {
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.clear();
        UserData cosmo = new UserData("cosmo", "couger", "cosmo@byu.edu");
        userDAO.createUser(cosmo);

        assertNotNull(userDAO.getUser("cosmo"));
    }

    @Test
    public void createUserNegative() throws Exception {
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.clear();
        UserData shane = new UserData("shanereese", "gocougs", "shanereese@byu.edu");
        UserData shane2 = new UserData("shanereese", "gocougs", "shanereese@byu.edu");

        userDAO.createUser(shane);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(shane2));
    }

    @Test
    public void getUserPositive() throws Exception{
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.clear();

        UserData cosmo = new UserData("cosmo", "couger", "cosmo@byu.edu");
        userDAO.createUser(cosmo);

        UserData shane = new UserData("shanereese", "gocougs", "shanereese@byu.edu");
        userDAO.createUser(shane);

        assertNotNull(userDAO.getUser("cosmo"));
        assertNotNull(userDAO.getUser("shane"));
    }

    @Test
    public void getUserNegative() throws Exception{
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.clear();

        assertThrows(DataAccessException.class, () -> userDAO.getUser("cosmo"));
    }

    @Test
    public void createAuthPositive() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();

        AuthData testauth = new AuthData("testToken", "cosmo");
        authDAO.createAuth(testauth);

        assertNotNull(authDAO.getAuth("testToken"));

    }

    @Test
    public void createAuthNegative() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();
    }

    @Test
    public void getAuthPositive() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();
    }

    @Test
    public void getAuthNegative() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();
    }

    @Test
    public void deleteAuthPositive() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();
    }

    @Test
    public void deleteAuthNegative() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void getGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void getGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void listGamesPositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void listGamesNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void updateGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }

    @Test
    public void updateGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
    }
}
