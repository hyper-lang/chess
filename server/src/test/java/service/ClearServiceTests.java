package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
    @Test
    public void clearAll() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        userDAO.createUser(new UserData("u", "p", "e"));
        authDAO.createAuth(new AuthData("t", "u"));
        gameDAO.createGame(new model.GameData(0, null, null, "g", null));

        new ClearService(userDAO, authDAO, gameDAO).clear();

        assertNull(userDAO.getUser("u"));
        assertNull(authDAO.getAuth("t"));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}
