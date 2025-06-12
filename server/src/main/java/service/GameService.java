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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class GameService extends Service {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int gameIndex;

    private record GameSessions(Session black, Session white, HashSet<Session> observers) {
    }

    private static final HashMap<Integer, GameSessions> sessions = new HashMap<>();
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
        GameData newGame = new GameData(gameIndex, null, null, gameName, new ChessGame());
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
        AuthData auth; // TODO: Let multiple sessions from the same user connect to a game, but only let one session play.
        GameData game;
        try {
            auth = authDAO.getAuthByAuthToken(authToken);
            game = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        send(session, loadGameString(game));

        String username = auth.username();
        String white = game.whiteUsername();
        String black = game.blackUsername();
        GameSessions connectedSessions = sessions.get(gameID);
        if (connectedSessions == null) {
            Session blackSession = Objects.equals(username, black) ? session : null;
            Session whiteSession = Objects.equals(username, white) ? session : null;
            HashSet<Session> observers = new HashSet<>();
            if (!Objects.equals(username, black) && !Objects.equals(username, white)) {
                observers.add(session);
            }
            sessions.put(gameID, new GameSessions(blackSession, whiteSession, observers));
        }
        connectedSessions = sessions.get(gameID);

        HashSet<Session> observers = connectedSessions.observers;
        String notification = "user '" + username + "' has joined as ";
        try (Session whiteSession = connectedSessions.white;
             Session blackSession = connectedSessions.black) {

            if ((whiteSession == null) && Objects.equals(white, username)) {
                sessions.put(gameID, new GameSessions(session, blackSession, observers));
                notification += "white";
            } else if (Objects.equals(black, username)) {
                sessions.put(gameID, new GameSessions(whiteSession, session, observers));
                notification += "black";
            } else {
                observers.add(session);
                sessions.put(gameID, new GameSessions(whiteSession, blackSession, observers));
                notification += "observer";
            }
        }
        notification = notificationString(notification);
        sendToOtherConnectedSessions(gameID, notification, session);
    }

    public void makeMove(Session session, String authToken, int gameID, ChessMove move) throws IOException {
        GameData gameData;
        try {
            authDAO.getAuthByAuthToken(authToken);
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            send(session, errorString("Authorization is invalid"));
            return;
        }
        GameSessions connectedSessions = sessions.get(gameID);
        ChessGame game = gameData.game();
        if ((game.getTeamTurn() == ChessGame.TeamColor.WHITE && session == connectedSessions.white) ||
                (game.getTeamTurn() == ChessGame.TeamColor.BLACK && session == connectedSessions.black)) {
            // Correct turn and session
            try {
                game.makeMove(move);
                GameData updatedGame = new GameData(
                        gameID, gameData.blackUsername(), gameData.whiteUsername(), gameData.gameName(), game);
                gameDAO.updateGame(gameID, updatedGame);
                String message = loadGameString(updatedGame);
                sendToAllConnectedSessions(gameID, message);
            } catch (InvalidMoveException e) {
                send(session, errorString("Invalid move"));
            } catch (DataAccessException e) {
                send(session, errorString("Couldn't update game"));
            }
        } else if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && connectedSessions.black == session) {
            send(session, errorString("It is " + gameData.whiteUsername() + "'s turn"));
        } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && connectedSessions.white == session) {
            send(session, errorString("It is " + gameData.blackUsername() + "'s turn"));
        } else {
            send(session, errorString("Observers cannot make moves"));
        }
    }

    private void sendToOtherConnectedSessions(int gameID, String message, Session session) throws IOException {
        HashSet<Session> otherConnections = getSessions(gameID);
        for (Session s : otherConnections) {
            if (s != session) {
                s.getRemote().sendString(message);
            }
        }
    }

    private void sendToAllConnectedSessions(int gameID, String message) throws IOException {
        HashSet<Session> otherConnections = getSessions(gameID);
        for (Session s : otherConnections) {
            s.getRemote().sendString(message);
        }
    }

    private HashSet<Session> getSessions(int gameID) {
        GameSessions connections = sessions.get(gameID);
        HashSet<Session> otherConnections = new HashSet<>();
        if (connections == null) {
            return otherConnections;
        }
        otherConnections.add(connections.white);
        otherConnections.add(connections.black);
        connections.observers.removeIf((Session s) -> !s.isOpen());
        otherConnections.addAll(connections.observers);
        otherConnections.removeIf(Objects::isNull);
        otherConnections.removeIf((Session s) -> !s.isOpen());
        sessions.put(gameID, new GameSessions(connections.black, connections.white, connections.observers));
        return otherConnections;
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
