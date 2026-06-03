package client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import server.Server;
import model.UserData;
import model.AuthData;

import java.util.UUID;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + Integer.toString(port));
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws Exception {
        String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        AuthData auth = facade.register(new UserData(randomStr, randomStr, randomStr));
        assertNotNull(auth.authToken());
    }

    @Test
    public void registerNegative() throws Exception {
        facade.register(new UserData("bob", "bob", "bob"));
        assertThrows(Exception.class, () -> facade.register(new UserData("bob", "bob", "bob")));
    }

    @Test
    public void loginPositive() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.login(new UserData("fake", "fake", "fake")));
    }

    @Test
    public void logoutPositive() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertDoesNotThrow(() -> facade.logout(auth));
    }

    @Test
    public void logoutNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.logout(null));
    }

    @Test
    public void createGamePositive() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertDoesNotThrow(() -> facade.createGame(auth, "hype_game"));
    }

    @Test
    public void createGameNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.createGame(null, "hype_game"));
    }

    @Test
    public void listGamesPositive() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertDoesNotThrow(() -> facade.listGames(auth));
    }

    @Test
    public void listGamesNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.listGames(null));
    }

    @Test
    public void joinGamePositive() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        int gameInt = facade.createGame(auth, "new_game_created");
        assertDoesNotThrow(() -> facade.joinGame(auth, "WHITE", gameInt));
    }

    @Test
    public void joinGameNegative() throws Exception {
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertThrows(Exception.class, () -> facade.joinGame(auth, "WHITE", 1000));
    }
}