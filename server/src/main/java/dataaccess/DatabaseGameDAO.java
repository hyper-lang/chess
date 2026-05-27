package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;

public class DatabaseGameDAO implements GameDAO {
    public DatabaseGameDAO() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String statement =  """
            CREATE TABLE IF NOT EXISTS games (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`id`),
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
    public int createGame(GameData game) throws DataAccessException{
        var statement = "INSERT INTO games (json) VALUES (?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                Gson gson = new Gson();
                preparedStatement.setString(1, gson.toJson(game));
                preparedStatement.executeUpdate();
                var rs = preparedStatement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{}
    
    public Collection<GameData> listGames() throws DataAccessException{}
    public void updateGame(int gameID, GameData game) throws DataAccessException{}

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE games";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }
}
