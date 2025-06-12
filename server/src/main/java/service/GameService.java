package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.GameSummary;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class GameService extends Service {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int gameIndex;
    private ConnectionManager connections = new ConnectionManager();

    private record GameSessions(Session black, Session white, HashSet<Session> observers) {
    }

    private static final Gson GSON = new Gson();

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        try {
            gameIndex = gameDAO.getMaxGameID();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        gameIndex = gameDAO.getMaxGameID() + 1;
        GameData newGame = new GameData(gameIndex, null, null, gameName, new ChessGame().toDense());
        gameDAO.addGame(newGame);
        return gameIndex;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID)
            throws UnauthorizedException, DataAccessException, AlreadyTakenException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("Game doesn't exist");
        }
        if (playerColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, auth.username(), game.whiteUsername(), game.gameName(), game.game()));
            return;
        }
        if (playerColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, game.blackUsername(), auth.username(), game.gameName(), game.game()));
            return;
        }
        throw new AlreadyTakenException();
    }

    public Collection<GameSummary> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        return gameDAO.getGameSummaries();
    }

    // Webhook methods
    public void connectToGame(Session session, String authToken, int gameID) throws IOException {
        AuthData auth;
        GameData game;
        try {
            auth = authDAO.getAuthByAuthToken(authToken);
            game = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        if (game == null) {
            send(session, errorString("Game not found"));
            return;
        }
        send(session, loadGameString(game));

        ArrayList<Connection> existingConnections = connections.getConnections(gameID);
        existingConnections = existingConnections == null ? new ArrayList<>() : existingConnections;
        Connection black = null;
        Connection white = null;
        for (Connection c : existingConnections) {
            if (c.relation == Connection.Relation.BLACK) {
                black = c;
            } else if (c.relation == Connection.Relation.WHITE) {
                white = c;
            }
        }

        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String username = auth.username();

        Connection.Relation relation;
        String notification = "user '" + username + "' has joined as ";
        if (black == null || Objects.equals(blackUsername, username)) {
            relation = Connection.Relation.BLACK;
            notification += "black";
        } else if (white == null || Objects.equals(whiteUsername, username)) {
            relation = Connection.Relation.WHITE;
            notification += "white";
        } else {
            relation = Connection.Relation.OBSERVER;
            notification += "observer";
        }
        connections.add(auth, gameID, session, relation);
        notification = notificationString(notification);
        connections.broadcast(gameID, auth, notification);
    }

    public void makeMove(Session session, String authToken, int gameID, ChessMove move) throws IOException {
        GameData gameData;
        AuthData authData;
        try {
            authData = authDAO.getAuthByAuthToken(authToken);
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        ArrayList<Connection> connectedSessions = connections.getConnections(gameID);
        ChessGame game = new ChessGame(gameData.game());
        AuthData whiteAuth = null;
        AuthData blackAuth = null;
        for (Connection c : connectedSessions) {
            if (c.relation == Connection.Relation.BLACK) {
                blackAuth = c.auth;
            }
            if (c.relation == Connection.Relation.WHITE) {
                whiteAuth = c.auth;
            }
        }
        ChessGame.TeamColor turn = game.getTeamTurn();
        if ((turn == ChessGame.TeamColor.WHITE && authData == whiteAuth) ||
                (turn == ChessGame.TeamColor.BLACK && authData == blackAuth)) {
            try {
                game.makeMove(move);
                GameData updatedGame = new GameData(
                        gameID, gameData.blackUsername(), gameData.whiteUsername(), gameData.gameName(), game.toDense());
                gameDAO.updateGame(gameID, updatedGame);
                String message = loadGameString(updatedGame);
                connections.broadcast(gameID, null, message);
            } catch (InvalidMoveException e) {
                send(session, errorString("Invalid move"));
            } catch (DataAccessException e) {
                send(session, errorString("Couldn't update game"));
            }
        } else if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && authData == blackAuth) {
            send(session, errorString("It is " + gameData.whiteUsername() + "'s turn"));
        } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && authData == whiteAuth) {
            send(session, errorString("It is " + gameData.blackUsername() + "'s turn"));
        } else {
            send(session, errorString("Observers cannot make moves"));
        }
    }

    public void leaveGame(Session session, String authToken, int gameID) throws IOException {
        AuthData authData;
        try {
            authData = authDAO.getAuthByAuthToken(authToken);
            connections.remove(gameID, authData);
            String message = notificationString("User '" + authData.username() + "' has left");
            connections.broadcast(gameID, null, message);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
        }
    }

    public void resign(Session session, String authToken, int gameID) throws IOException {
        // TODO must lock game
    }

    private String errorString(String errorMessage) {
        return GSON.toJson(new ErrorMessage(errorMessage), ErrorMessage.class);
    }

    private String notificationString(String notification) {
        return GSON.toJson(new NotificationMessage(notification), NotificationMessage.class);
    }

    private String loadGameString(GameData game) {
        return GSON.toJson(new LoadGameMessage(game), LoadGameMessage.class);
    }

    private void send(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
