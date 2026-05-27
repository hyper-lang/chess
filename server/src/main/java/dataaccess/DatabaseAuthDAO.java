package dataaccess;

import java.sql.SQLException;

import com.google.gson.Gson;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO {
    public DatabaseAuthDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement =  """
            CREATE TABLE IF NOT EXISTS  auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `auth_token` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
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
    public void createAuth(AuthData auth) throws DataAccessException{
        var statement = "INSERT INTO auths (auth_token, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }    
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        var statement = "SELECT * FROM auths WHERE auth_token = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    return new AuthData(rs.getString("auth_token"), rs.getString("username"));
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        var statement = "DELETE FROM auths WHERE auth_token = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }

    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE auths";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }
}
