package dataaccess;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;

public class DaoTests {
    @Test
    public void createUserPositive() throws Exception {
        UserDAO userDAO = new DatabaseUserDAO();
        UserData cosmo = new UserData("cosmo", "couger", "cosmo@byu.edu");
        userDAO.createUser(cosmo);

        assertNotNull(userDAO.getUser("cosmo"));
    }

    @Test
    public void createUserNegative() throws Exception {
        UserDAO userDAO = new DatabaseUserDAO();
        UserData shane = new UserData("shanereese", "gocougs", "shanereese@byu.edu");
        UserData shane2 = new UserData("shanereese", "gocougs", "shanereese@byu.edu");

        userDAO.createUser(shane);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(shane2));
    }

    @Test
    public void getUserPositive() throws Exception{}

    @Test
    public void getUserNegative() throws Exception{}

    @Test
    public void createAuthPositive() throws Exception{}

    @Test
    public void createAuthNegative() throws Exception{}

    @Test
    public void getAuthPositive() throws Exception{}

    @Test
    public void getAuthNegative() throws Exception{}

    @Test
    public void deleteAuthPositive() throws Exception{}

    @Test
    public void deleteAuthNegative() throws Exception{}

    @Test
    public void createGamePositive() throws Exception{}

    @Test
    public void createGameNegative() throws Exception{}

    @Test
    public void getGamePositive() throws Exception{}

    @Test
    public void getGameNegative() throws Exception{}

    @Test
    public void listGamesPositive() throws Exception{}

    @Test
    public void listGamesNegative() throws Exception{}

    @Test
    public void updateGamePositive() throws Exception{}

    @Test
    public void updateGameNegative() throws Exception{}
}
