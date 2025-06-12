package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MySqlAuth;
import dataaccess.MySqlGame;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebsocketHandler {
    private static final AuthDAO AUTH_DAO = new MySqlAuth();
    private static final GameDAO GAME_DAO = new MySqlGame();
    private static final GameService GAME_SERVICE = new GameService(AUTH_DAO, GAME_DAO);
    private static final Gson GSON = new Gson();

    public void handle(Session session, String command) {
        System.out.println("received websocket command " + command);
        UserGameCommand userGameCommand = GSON.fromJson(command, UserGameCommand.class);
        try {
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> handleConnect(session, userGameCommand);
                case MAKE_MOVE -> handleMakeMove(session, GSON.fromJson(command, MakeMoveCommand.class));
                case LEAVE -> handleLeave(userGameCommand);
                case RESIGN -> handleResign(userGameCommand);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException {
        GAME_SERVICE.connectToGame(session, command.getAuthToken(), command.getGameID());
    }

    private void handleMakeMove(Session session, MakeMoveCommand command) throws IOException {
        GAME_SERVICE.makeMove(session, command.getAuthToken(), command.getGameID(), command.getMove());
    }

    private ServerMessage handleLeave(UserGameCommand command) {
        throw new RuntimeException();
    }

    private ServerMessage handleResign(UserGameCommand command) {
        throw new RuntimeException();
    }
}
