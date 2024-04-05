package dataAccessTests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import model.UserData;

import java.util.Arrays;
import java.util.HashSet;

public class DataAccessVars {
    static final AuthData a0 = new AuthData("0hu2^7T@YXtJhDuQ2%dg", "myname123");
    static final AuthData a1 = new AuthData("1hu2^7T@YXtJhDuQ2%dg", "antonius");
    static final AuthData a2 = new AuthData("2hu2^7T@YXtJhDuQ2%dg", "death");

    static final GameData g0 = new GameData(1, "myname123", "Antonius", "firstmatch", new ChessGame());
    static final GameData g1 = new GameData(2, "antonius", "death", "chessgame", new ChessGame());
    static final GameData g2 = new GameData(3, "death", "myname123", "chessgame", new ChessGame());

    static final UserData u0 = new UserData("myname123", "p@ssword", "email@email.com");
    static final UserData u1 = new UserData("antonius", "G00dK!ght", "email@email.com");
    static final UserData u2 = new UserData("death", "humor0us", "email@email.com");

    static final HashSet<AuthData> authData = new HashSet<>(Arrays.asList(a0, a1, a2));
    static final HashSet<GameData> gameData = new HashSet<>(Arrays.asList(g0, g1, g2));
    static final HashSet<UserData> userData = new HashSet<>(Arrays.asList(u0, u1, u2));

    static final GameSummary s0 = new GameSummary(g0.gameID(), g0.whiteUsername(), g0.blackUsername(), g0.gameName());
    static final GameSummary s1 = new GameSummary(g1.gameID(), g1.whiteUsername(), g1.blackUsername(), g1.gameName());
    static final GameSummary s2 = new GameSummary(g2.gameID(), g2.whiteUsername(), g2.blackUsername(), g2.gameName());
    static final HashSet<GameSummary> gameSummaries = new HashSet<>(Arrays.asList(s0, s1, s2));
}
