package dataaccess;

import java.sql.SQLException;

import dataaccess.DatabaseManager;

public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement =  """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
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

    
}
