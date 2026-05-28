package dataaccess;

import org.junit.jupiter.api.*;

import chess.ChessGame;

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
        assertNotNull(userDAO.getUser("shanereese"));
    }

    @Test
    public void getUserNegative() throws Exception{
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.clear();

        assertNull(userDAO.getUser("cosmo"));
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

        AuthData auth = new AuthData(null, "cosmo");

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void getAuthPositive() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();

        AuthData auth = new AuthData("UUID", "cosmo");
        authDAO.createAuth(auth);

        assertNotNull(authDAO.getAuth("UUID"));
    }

    @Test
    public void getAuthNegative() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();

        assertNull(authDAO.getAuth("fakeAuth"));
    }

    @Test
    public void deleteAuthPositive() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();

        authDAO.createAuth(new AuthData("UUID", "cosmo"));
        authDAO.deleteAuth("UUID");

        assertNull(authDAO.getAuth("UUID"));
    }

    @Test
    public void deleteAuthNegative() throws Exception{
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.clear();

        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistent"));
    }

    @Test
    public void createGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        int gameID = gameDAO.createGame(new GameData(1, "cosmo", "shane", "byu150", new ChessGame()));

        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    public void createGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        assertThrows(NullPointerException.class, () -> gameDAO.createGame(null));
    }

    @Test
    public void getGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        GameData newGame = new GameData(0, null, null, null, null);
        int gameID = gameDAO.createGame(newGame);

        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    public void getGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        assertNull(gameDAO.getGame(67));
    }

    @Test
    public void listGamesPositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        gameDAO.createGame(new GameData(0, "white", "black", "battle", null));
        gameDAO.createGame(new GameData(0, "white", "black", "battle2", null));

        assertEquals(2, gameDAO.listGames().size());
    }

    @Test
    public void listGamesNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void updateGamePositive() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        int gameID = gameDAO.createGame(new GameData(0, "white", "black", "awesomeGame", null));

        GameData updated = new GameData(gameID, "white", "black", "awesomerGame", null);
        gameDAO.updateGame(gameID, updated);

        assertEquals("awesomerGame", gameDAO.getGame(gameID).gameName());
    }

    @Test
    public void updateGameNegative() throws Exception{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();

        GameData updated = new GameData(67, "white", "black", "none", null);

        assertDoesNotThrow(() -> gameDAO.updateGame(67, updated));
    }
}
