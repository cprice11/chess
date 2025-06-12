package service;

import datamodels.AuthData;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public AuthData auth;
    public int gameID;
    public Session session;
    public Relation relation;

    public enum Relation {
        BLACK,
        WHITE,
        OBSERVER
    }

    public Connection(AuthData auth, int gameID, Session session, Relation relation) {
        this.auth = auth;
        this.gameID = gameID;
        this.session = session;
        this.relation = relation;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
