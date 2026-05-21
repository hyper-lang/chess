package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserService userService;
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData auth = userService.register(user);

        assertNotNull(auth);
        assertEquals("testUser", auth.username());
        assertNotNull(auth.authToken());
        assertNotNull(userDAO.getUser("testUser"));
    }

    @Test
    public void registerEmptyPassword() {
        UserData user = new UserData("testUser", null, "email@test.com");
        assertThrows(DataAccessException.class, () -> userService.register(user));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);

        AuthData auth = userService.login(user);
        assertNotNull(auth);
        assertEquals("testUser", auth.username());
    }

    @Test
    public void loginWrongPassword() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);

        UserData wrongUser = new UserData("testUser", "wrongPass", null);
        assertThrows(DataAccessException.class, () -> userService.login(wrongUser));
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData auth = userService.register(user);

        userService.logout(auth.authToken());
        assertNull(authDAO.getAuth(auth.authToken()));
    }

    @Test
    public void logoutInvalidToken() {
        assertThrows(DataAccessException.class, () -> userService.logout("invalid-token"));
    }
}
