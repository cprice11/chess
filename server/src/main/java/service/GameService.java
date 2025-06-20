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
import java.util.Objects;

public class GameService extends Service {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int gameIndex;
    private static ConnectionManager connections;
    private static final Gson GSON = new Gson();
    private AuthData authData;
    private GameData gameData;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        connections = new ConnectionManager();
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
    public void clear() {
        System.out.println("Clearing connections");
        connections.clear();
        connections = new ConnectionManager();
    }

    public void connectToGame(Session session, String authToken, int gameID) throws IOException {
        if (isAuthInvalid(authToken, session) || isGameInvalid(gameID, session)) {
            return;
        }
        GameData game;
        AuthData auth;
        try {
            game = gameDAO.getGame(gameID);
            auth = authDAO.getAuthByAuthToken(authToken);
        } catch (DataAccessException e) {
            send(session, errorString("database values disappeared"));
            return;
        }
        send(session, loadGameString(game));

        ArrayList<Connection> existingConnections = connections.getConnections(gameID);
        existingConnections = existingConnections == null ? new ArrayList<>() : existingConnections;
        Connection black = null;
        Connection white = null;
        for (Connection c : existingConnections) {
            if (c.auth == auth) {
                return;
            }
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
        if (black == null && Objects.equals(blackUsername, username)) {
            relation = Connection.Relation.BLACK;
            notification += "black";
        } else if (white == null && Objects.equals(whiteUsername, username)) {
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

    private boolean isAuthInvalid(String authToken, Session session) throws IOException {
        authData = null;
        try {
            authData = authDAO.getAuthByAuthToken(authToken);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return true;
        }
        if (authData == null) {
            send(session, errorString("Authorization is invalid"));
            return true;
        }
        return false;
    }

    private boolean isGameInvalid(int gameID, Session session) throws IOException {
        gameData = null;
        try {
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Game id is invalid"));
            return true;
        }
        if (gameData == null) {
            send(session, errorString("Game id is invalid"));
            return true;
        }
        return false;
    }


    public void makeMove(Session session, String authToken, int gameID, ChessMove move) throws IOException {
        if (isAuthInvalid(authToken, session) || isGameInvalid(gameID, session)) {
            return;
        }
        ChessGame game = new ChessGame(gameData.game());
        Connection connection = null;
        for (Connection c : connections.getConnections(gameID)) {
            if (Objects.equals(authData.authToken(), c.auth.authToken())) {
                connection = c;
                break;
            }
        }
        if (connection == null || connection.relation == Connection.Relation.OBSERVER) {
            String message = errorString("Non-players cannot make moves");
            send(session, message);
            return;
        }
        ChessGame.TeamColor turn = game.getTeamTurn();
        if ((turn == ChessGame.TeamColor.WHITE && connection.relation == Connection.Relation.WHITE) ||
                (turn == ChessGame.TeamColor.BLACK && connection.relation == Connection.Relation.BLACK)) {
            try {
                game.makeMove(move);
                String notify = null;
                if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    notify = notificationString(gameData.whiteUsername() + " wins by checkmate");
                } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    notify = notificationString(gameData.blackUsername() + " wins by checkmate");
                } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                    notify = notificationString("Stalemate");
                } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                    notify = notificationString(gameData.blackUsername() + " is in check");
                } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                    notify = notificationString(gameData.whiteUsername() + " is in check");
                }
                GameData updatedGame = new GameData(gameID, gameData.blackUsername(), gameData.whiteUsername(),
                        gameData.gameName(), game.toDense());
                gameDAO.updateGame(gameID, updatedGame);
                String message = loadGameString(updatedGame);
                connections.broadcast(gameID, null, message);
                if (notify == null) {
                    connections.broadcast(gameID, authData, notificationString(authData.username() + " " + move.toString()));
                } else {
                    connections.broadcast(gameID, null, notify);
                }
            } catch (InvalidMoveException e) {
                send(session, errorString("Invalid move"));
            } catch (DataAccessException e) {
                send(session, errorString("Couldn't update game"));
            }
        } else {
            String turnUsername = turn == ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
            send(session, errorString("It is " + turnUsername + "'s turn"));
        }
    }

    public void leaveGame(Session session, String authToken, int gameID) throws IOException {
        AuthData authData;
        GameData gameData;
        try {
            authData = authDAO.getAuthByAuthToken(authToken);
            gameData = gameDAO.getGame(gameID);
            String username = authData.username();
            String white = Objects.equals(gameData.whiteUsername(), username) ? null : gameData.whiteUsername();
            String black = Objects.equals(gameData.blackUsername(), username) ? null : gameData.blackUsername();
            gameDAO.updateGame(gameID, new GameData(gameID, black, white, gameData.gameName(), gameData.game()));
            connections.remove(gameID, authData);
            String message = notificationString("User '" + authData.username() + "' has left");
            connections.broadcast(gameID, authData, message);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
        }
    }

    public void resign(Session session, String authToken, int gameID) throws IOException {
        AuthData authData;
        GameData gameData;
        try {
            authData = authDAO.getAuthByAuthToken(authToken);
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        if (authData == null) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        if (gameData == null) {
            send(session, errorString("Game is invalid"));
            return;
        }
        Connection connection = null;
        for (Connection c : connections.getConnections(gameID)) {
            if (Objects.equals(authData.authToken(), c.auth.authToken())) {
                connection = c;
                break;
            }
        }
        System.out.println(connection);
        if (connection == null || connection.relation == Connection.Relation.OBSERVER) {
            String message = errorString("Non-players cannot resign");
            send(session, message);
            return;
        }
        ChessGame.TeamColor teamColor = connection.relation == Connection.Relation.WHITE ?
                ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        ChessGame game = new ChessGame(gameData.game());
        try {
            game.resign(teamColor);
            gameDAO.updateGame(gameID, new GameData(gameID, gameData.blackUsername(), gameData.whiteUsername(), gameData.gameName(), game.toDense()));
        } catch (InvalidMoveException | DataAccessException e) {
            send(session, errorString(e.getMessage()));
            return;
        }
        String notify = notificationString(authData.username() + " resigned");
        connections.broadcast(gameID, null, notify);
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
