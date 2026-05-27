package dataaccess;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import model.UserData;

public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement =  """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPass = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, hashedPass);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }                
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE users";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }
}
