package service;

import datamodels.AuthData;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(AuthData auth, int gameID, Session session, Connection.Relation relation) {
        var connection = new Connection(auth, gameID, session, relation);
        var gameListeners = connections.get(gameID);
        gameListeners = gameListeners == null ? new ArrayList<>() : gameListeners;
        gameListeners.add(connection);
        connections.put(gameID, gameListeners);
    }

    public ArrayList<Connection> getConnections(int gameID) {
        return connections.get(gameID);
    }

    public void remove(int gameID) {
        connections.remove(gameID);
    }

    public void remove(int gameID, AuthData auth) {
        var gameListeners = connections.get(gameID);
        var removeList = new ArrayList<Connection>();
        for (var c : gameListeners) {
            if (c.auth == auth) {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            gameListeners.remove(c);
        }
        connections.put(gameID, gameListeners);
    }

    public void remove(AuthData auth) {
        for (Map.Entry<Integer, ArrayList<Connection>> entry : connections.entrySet()) {
            entry.getValue().removeIf((Connection c) -> c.auth == auth);
            connections.put(entry.getKey(), entry.getValue());
        }
    }


    public void broadcast(int gameID, AuthData excludeMessageRoot, String message) throws IOException {
        var removeList = new ArrayList<Connection>();
        var listeners = connections.get(gameID);
        for (var c : listeners) {
            if (c.session.isOpen()) {
                if (!c.auth.equals(excludeMessageRoot)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var r : removeList) {
            listeners.remove(r);
        }
        connections.put(gameID, listeners);
    }
}
