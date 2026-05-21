package service;

import model.*;
import dataaccess.*;
import java.util.Collection;

public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken == null || authToken.isBlank()) {
            throw new DataAccessException("unauthorized");
        }

        if (gameName == null || gameName.isBlank()) {
            throw new DataAccessException("bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("unauthorized");
        }

        GameData newGame = new GameData(0, null, null, gameName, new chess.ChessGame());
        return gameDAO.createGame(newGame);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isBlank()) {
            throw new DataAccessException("unauthorized");
        }

        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }

        return gameDAO.listGames();
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("unauthorized");
        }

        String username = auth.username();

        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("game not found");
        }

        String white = game.whiteUsername();
        String black = game.blackUsername();

        if (playerColor == null || playerColor.isBlank() ||
                (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
            throw new DataAccessException("bad request");
        }

        GameData newGame;

        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (white != null) {
                throw new DataAccessException("already taken");
            }
            newGame = new GameData(game.gameID(), username, black, game.gameName(), game.game());
        } else if (playerColor.equalsIgnoreCase("BLACK")) {
            if (black != null) {
                throw new DataAccessException("already taken");
            }

            newGame = new GameData(game.gameID(), white, username, game.gameName(), game.game());
        } else {
            throw new DataAccessException("bad request");
        }

        gameDAO.updateGame(gameID, newGame);
    }
}
