package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import model.UserData;
import server.request.*;
import server.result.*;

import java.util.Arrays;
import java.util.HashSet;

public class ServiceVars {
    static final AuthData a0 = new AuthData("0hu2^7T@YXtJhDuQ2%dg", "myname123");
    static final AuthData a1 = new AuthData("1hu2^7T@YXtJhDuQ2%dg", "antonius");
    static final AuthData a2 = new AuthData("2hu2^7T@YXtJhDuQ2%dg", "death");

    static final AuthData aNew = new AuthData("3hu2^7T@YXtJhDuQ2%dg", "Magnus");

    static final GameData g0 = new GameData(1, "myname123", "Antonius", "firstmatch", new ChessGame());
    static final GameData g1 = new GameData(2, "antonius", "death", "chessgame", new ChessGame());
    static final GameData g2 = new GameData(3, "death", "myname123", "chessgame", new ChessGame());

    static final GameData gNew = new GameData(4, "Magnus", null, "partialMatch", new ChessGame());
    static final GameData gEmpty = new GameData(5, null, null, "partialMatch", new ChessGame());

    static final UserData u0 = new UserData("myname123", "p@ssword", "email@email.com");
    static final UserData u1 = new UserData("antonius", "G00dK!ght", "email@email.com");
    static final UserData u2 = new UserData("death", "humor0us", "email@email.com");

    static final UserData uNew = new UserData("Magnus", "1234", "mcarlson@chessPro.com");

    static final HashSet<AuthData> authData = new HashSet<>(Arrays.asList(a0, a1, a2));
    static final HashSet<GameData> gameData = new HashSet<>(Arrays.asList(g0, g1, g2));
    static final HashSet<UserData> userData = new HashSet<>(Arrays.asList(u0, u1, u2));

    static final HashSet<GameSummary> gameSummaries = new HashSet<>(Arrays.asList(
            new GameSummary(g0.gameID(), g0.whiteUsername(), g0.blackUsername(), g0.gameName()),
            new GameSummary(g1.gameID(), g1.whiteUsername(), g1.blackUsername(), g1.gameName()),
            new GameSummary(g2.gameID(), g2.whiteUsername(), g2.blackUsername(), g2.gameName())
    ));

    protected static AuthDAO auth = new MemoryAuthDAO();
    protected static GameDAO games = new MemoryGameDAO();
    protected static UserDAO users = new MemoryUserDAO();


    // register
    static final RegisterRequest goodRegisterRequest = new RegisterRequest(uNew.username(), uNew.password(), uNew.email());
    static final RegisterRequest badRegisterRequest = new RegisterRequest(u2.username(), uNew.password(), uNew.email());
    // static final RegisterResult goodRegisterResult = new RegisterResult(null, uNew.username()); // authToken shouldn't be created here
    // static final RegisterResult badRegisterResult = new RegisterResult();
    // login
    static final LoginRequest goodLoginRequest = new LoginRequest(u0.username(), u0.password());
    static final LoginRequest badLoginRequest = new LoginRequest(u0.username(), u2.password());
    static final LoginResult goodLoginResult = new LoginResult(u0.username(), null); // authToken shouldn't be created here
    //    static final LoginResult badLoginResult = new LoginResult();
    // logout
    static final LogoutRequest goodLogoutRequest = new LogoutRequest(a0.authToken());
    static final LogoutRequest badLogoutRequest = new LogoutRequest(aNew.authToken());
    // no result

    // listGames
    static final ListGamesRequest goodListGamesRequest = new ListGamesRequest(a1.authToken());
    static final ListGamesRequest badListGamesRequest = new ListGamesRequest(aNew.authToken());
    static final ListGamesResult goodListGamesResult = new ListGamesResult(gameSummaries.toArray(new GameSummary[0]));
    static final ListGamesResult badListGamesResult = new ListGamesResult(null);
    // createGame
    static final CreateGameRequest goodCreateGameRequest = new CreateGameRequest(a0.authToken(), g1.gameName());
    static final CreateGameRequest badCreateGameRequest = new CreateGameRequest(aNew.authToken(), gNew.gameName());
    // static final CreateGameResult goodCreateGameResult = new CreateGameResult(); // game ID shouldn't be created here
    static final CreateGameResult badCreateGameResult = new CreateGameResult(g1.gameID());
    // joinGame
    static final JoinGameRequest goodJoinGameRequest = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestFullGame = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, g2.gameID());
    static final JoinGameRequest goodJoinGameRequestNoColor = new JoinGameRequest(a1.authToken(), null, gEmpty.gameID());
    static final JoinGameRequest getGoodJoinGameRequestNewPlayerNoColor = new JoinGameRequest(a2.authToken(), null, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestNewPlayer = new JoinGameRequest(a2.authToken(), ChessGame.TeamColor.BLACK, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestNewPlayerSameColor = new JoinGameRequest(a2.authToken(), ChessGame.TeamColor.WHITE, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestSamePlayer = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, gEmpty.gameID());
    static final JoinGameRequest badJoinGameRequestWrongId = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, -1);
    static final JoinGameRequest badJoinGameRequestBadToken = new JoinGameRequest(aNew.authToken(), ChessGame.TeamColor.WHITE, -1);
    static final JoinGameRequest badJoinGameRequestFull = new JoinGameRequest(aNew.authToken(), ChessGame.TeamColor.WHITE, -1);
    // no result

    // clear
    // no request or result
    static final String t0 = "f?p*$#3_RR]uRqgt!wcP";
    static final String t1 = ":Zu|-r+&OFo$O5nOv>?1";
    static final String t2 = "KIt`}8e3FcN_]ib5iO?E";
    static final String t3 = "6j}sS.Ul5wP1=~6u=ARh";
    static final String t4 = "-,7]mA~3r^\"-hDn2b(<?";
    static final String t5 = "+2?MPTgB@%4=qC<a`Q@J";
    static final String t6 = "=KvKo6MeU4x*i%s=MLaU";
    static final String t7 = ",<.Uo7_7zun>!7Yv\"(w_";
    static final String t8 = "CvgjPN6^e_\\3>>M)U1\\c";
    static final String t9 = "@Mao<0%z/|[Y*Y[/*DzM";

    static final int id0 = 1431892720;
    static final int id1 = -1939181428;
    static final int id2 = 1229911139;
    static final int id3 = 399696808;
    static final int id4 = 740427237;
    static final int id5 = 458997467;
    static final int id6 = 672200581;
    static final int id7 = 107487293;
    static final int id8 = 2127624521;
    static final int id9 = 1996335810;

}
