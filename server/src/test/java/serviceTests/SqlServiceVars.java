package serviceTests;

import chess.ChessGame;
import dataAccess.GameDao;
import dataAccess.UserDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import model.UserData;
import server.request.*;
import server.result.ListGamesResult;
import server.result.LoginResult;

import java.util.Arrays;
import java.util.HashSet;

public class SqlServiceVars {
    // first 10 pseudo random outputs seed = 111
    static final String t0 = "f?p*$#3_RR]uRqgt!wcP:Zu|-r+&OFo$O5nOv>?1";
    static final String t1 = "KIt`}8e3FcN_]ib5iO?E6j}sS.Ul5wP1=~6u=ARh";
    static final String t2 = "-,7]mA~3r^\"-hDn2b(<?+2?MPTgB@%4=qC<a`Q@J";
    static final String t3 = "=KvKo6MeU4x*i%s=MLaU,<.Uo7_7zun>!7Yv\"(w_";
    static final String t4 = "CvgjPN6^e_\\3>>M)U1\\c@Mao<0%z/|[Y*Y[/*DzM";
    static final String t5 = "prRB4_<I24){Y?P%MIVO^*ON03OAcXe$%A)Mj->!";
    static final String t6 = "q%Rn46%z-\\zB3,22gAch-,b('4UN3)ix\"SBX0<_6";
    static final String t7 = "|,r]D8SSe\\=8~;lZu2#;'&<u`ZWH*HjIO(`P\"]fJ";
    static final String t8 = "v[KSAS1$^1!MX|{3JWwwS&xI+?jW,ASJ/pgSh]!t";
    static final String t9 = "|y[*R8\\r*8%|do5kp,2bvkga0W/W({-yc!k7(q7z";

    static final int id0 = 950830939;
    static final int id1 = 506326292;
    static final int id2 = 1639761914;
    static final int id3 = 1389273394;
    static final int id4 = 683937171;
    static final int id5 = 506057841;
    static final int id6 = 1092644569;
    static final int id7 = 412721225;
    static final int id8 = 902295195;
    static final int id9 = 886779931;

    static final AuthData a0 = new AuthData(t0, "myname123");
    static final AuthData a1 = new AuthData(t1, "antonius");
    static final AuthData a2 = new AuthData(t2, "death");

    static final AuthData aNew = new AuthData("3hu2^7T@YXtJhDuQ2%dg", "Magnus");

    static final GameData g0 = new GameData(0, "myname123", "Antonius", "firstmatch", new ChessGame());
    static final GameData g1 = new GameData(1, "antonius", "death", "chessgame", new ChessGame());
    static final GameData g2 = new GameData(2, "death", "myname123", "chessgame", new ChessGame());

    static final GameData gNew = new GameData(4, "Magnus", null, "partialMatch", new ChessGame());
    static final GameData gEmpty = new GameData(5, null, null, "partialMatch", new ChessGame());

    static final UserData u0 = new UserData("myname123", "p@ssword", "email@email.com");
    static final UserData u1 = new UserData("antonius", "G00dK!ght", "email@email.com");
    static final UserData u2 = new UserData("death", "humor0us", "email@email.com");

    static final UserData uNew = new UserData("Magnus", "1234", "mcarlson@chessPro.com");

    static final HashSet<AuthData> authData = new HashSet<>(Arrays.asList(a0, a1, a2));
    static final HashSet<GameData> gameData = new HashSet<>(Arrays.asList(g0, g1, g2));
    static final HashSet<UserData> userData = new HashSet<>(Arrays.asList(u0, u1, u2));

    static final GameSummary s0 = new GameSummary(g0.gameID(), g0.whiteUsername(), g0.blackUsername(), g0.gameName());
    static final GameSummary s1 = new GameSummary(g1.gameID(), g1.whiteUsername(), g1.blackUsername(), g1.gameName());
    static final GameSummary s2 = new GameSummary(g2.gameID(), g2.whiteUsername(), g2.blackUsername(), g2.gameName());
    static final HashSet<GameSummary> gameSummaries = new HashSet<>(Arrays.asList(s0, s1, s2));

    //protected static AuthDao auth = new SQLAuthDao();
    //protected static GameDao games = new SQLGameDao();
    //protected static UserDao users = new SQLUserDao();

    // register
    static final RegisterRequest goodRegisterRequest = new RegisterRequest(uNew.username(), uNew.password(), uNew.email());
    static final RegisterRequest badRegisterRequest = new RegisterRequest(u2.username(), uNew.password(), uNew.email());

    // login
    static final LoginRequest goodLoginRequest = new LoginRequest(u0.username(), u0.password());
    static final LoginRequest badLoginRequest = new LoginRequest(u0.username(), u2.password());
    static final LoginResult loginResult = new LoginResult(u0.username(), null);

    // logout
    static final LogoutRequest goodLogoutRequest = new LogoutRequest(a0.authToken());
    static final LogoutRequest badLogoutRequest = new LogoutRequest(t9);
    // no result

    // listGames
    static final ListGamesRequest goodListGamesRequest = new ListGamesRequest(a1.authToken());
    static final ListGamesRequest badListGamesRequest = new ListGamesRequest(aNew.authToken());
    static final ListGamesResult listGamesResult = new ListGamesResult(gameSummaries);

    // createGame
    static final CreateGameRequest goodCreateGameRequest = new CreateGameRequest(a0.authToken(), g1.gameName());
    static final CreateGameRequest badCreateGameRequest = new CreateGameRequest(aNew.authToken(), gNew.gameName());

    // joinGame
    static final JoinGameRequest goodJoinGameRequestWhite = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestBlack = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.BLACK, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestFullGameWhite = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, g2.gameID());
    static final JoinGameRequest goodJoinGameRequestFullGameSpectator = new JoinGameRequest(a1.authToken(), null, g2.gameID());
    static final JoinGameRequest goodJoinGameRequestSpectator = new JoinGameRequest(a1.authToken(), null, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestNewSpectator = new JoinGameRequest(a2.authToken(), null, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestNewPlayerBlack = new JoinGameRequest(a2.authToken(), ChessGame.TeamColor.BLACK, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestNewPlayerWhite = new JoinGameRequest(a2.authToken(), ChessGame.TeamColor.WHITE, gEmpty.gameID());
    static final JoinGameRequest goodJoinGameRequestSamePlayerBlack = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.BLACK, gEmpty.gameID());
    static final JoinGameRequest badJoinGameRequestWrongId = new JoinGameRequest(a1.authToken(), ChessGame.TeamColor.WHITE, -1);
    static final JoinGameRequest badJoinGameRequestBadToken = new JoinGameRequest(aNew.authToken(), ChessGame.TeamColor.WHITE, -1);

    // clear
    // no request or result
}




