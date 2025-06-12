package handler;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import service.DevService;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebsocketHandler {
    private static final AuthDAO AUTH_DAO = new MySqlAuth();
    private static final GameDAO GAME_DAO = new MySqlGame();
    private static final UserDAO USER_DAO = new MySqlUser();
    private static final UserService USER_SERVICE = new UserService(AUTH_DAO, USER_DAO);
    private static final GameService GAME_SERVICE = new GameService(AUTH_DAO, GAME_DAO);
    private static final DevService DEV_SERVICE = new DevService(AUTH_DAO, GAME_DAO, USER_DAO);
    private static final Gson GSON = new Gson();

    public void handle(Session session, String command) {
        System.out.println("received websocket command " + command);
        UserGameCommand userGameCommand = GSON.fromJson(command, UserGameCommand.class);
        try {
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> handleConnect(session, userGameCommand);
                case MAKE_MOVE -> handleMakeMove(session, userGameCommand);
                case LEAVE -> handleLeave(userGameCommand);
                case RESIGN -> handleResign(userGameCommand);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();
        GAME_SERVICE.connectToGame(session, authToken, gameID);
    }

    private ServerMessage handleMakeMove(Session session, MakeMoveCommand command) throws IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();
        GAME_SERVICE.makeMove(session, authToken, gameID, );
    }

    private ServerMessage handleLeave(UserGameCommand command) {
        throw new RuntimeException();
    }

    private ServerMessage handleResign(UserGameCommand command) {
        throw new RuntimeException();
    }
}
