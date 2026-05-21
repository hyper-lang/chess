package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameService gameService;
    private MemoryGameDAO gameDAO;
    private MemoryAuthDAO authDAO;
    private String authToken;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(authDAO, gameDAO);

        authToken = "test-token";
        authDAO.createAuth(new AuthData(authToken, "testUser"));
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        int gameID = gameService.createGame(authToken, "Test Game");
        assertTrue(gameID > 0);
        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    public void createGameUnauthorized() {
        assertThrows(DataAccessException.class, () -> gameService.createGame("wrong-token", "Test Game"));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        gameService.createGame(authToken, "Game 1");
        gameService.createGame(authToken, "Game 2");

        Collection<GameData> games = gameService.listGames(authToken);
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesEmpty() throws DataAccessException {
        Collection<GameData> games = gameService.listGames(authToken);
        assertTrue(games.isEmpty());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        int gameID = gameService.createGame(authToken, "Join Me");
        gameService.joinGame(authToken, gameID, "WHITE");

        GameData game = gameDAO.getGame(gameID);
        assertEquals("testUser", game.whiteUsername());
    }

    @Test
    public void joinGameColorTaken() throws DataAccessException {
        int gameID = gameService.createGame(authToken, "Join Me");
        gameService.joinGame(authToken, gameID, "WHITE");

        // Someone else tries to join as white
        String token2 = "token2";
        authDAO.createAuth(new AuthData(token2, "user2"));

        assertThrows(DataAccessException.class, () -> gameService.joinGame(token2, gameID, "WHITE"));
    }

    @Test
    public void listGamesUnauthorized() {
        assertThrows(DataAccessException.class, () -> gameService.listGames("invalid-token"));
    }
}
