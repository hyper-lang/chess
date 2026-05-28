package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DatabaseGameDAO implements GameDAO {
    public DatabaseGameDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement =  """
            CREATE TABLE IF NOT EXISTS games (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`id`)
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
                preparedStatement.setString(1, "{}");
                preparedStatement.executeUpdate();

                var rs = preparedStatement.getGeneratedKeys();
                rs.next();

                int generatedID = rs.getInt(1);

                GameData correctedGame = new GameData(
                        generatedID,
                        game.whiteUsername(),
                        game.blackUsername(),
                        game.gameName(),
                        game.game());

                updateGame(generatedID, correctedGame);

                return generatedID;
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        var statement = "SELECT * FROM games WHERE id = ?";
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    return gson.fromJson(rs.getString("json"), GameData.class);
                }                
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        Collection<GameData> games = new ArrayList<>();
        var statement = "SELECT * FROM games";
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var rs = preparedStatement.executeQuery();
                while(rs.next()){
                    String json = rs.getString("json");
                    GameData game = gson.fromJson(json, GameData.class);
                    games.add(game);
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
        return games;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException{
        var statement = "UPDATE games SET json = ? WHERE id = ?";
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String updatedGame = gson.toJson(game);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, updatedGame);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Database connection or query failed", e);
        }
    }

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
