package dataAccessTests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import model.UserData;

import java.util.Collection;
import java.util.List;

public class DataAccessVars {
    static final AuthData a0 = new AuthData("0hu2^7T@YXtJhDuQ2%dg", "myname123");
    static final AuthData a1 = new AuthData("1hu2^7T@YXtJhDuQ2%dg", "antonius");
    static final AuthData a2 = new AuthData("2hu2^7T@YXtJhDuQ2%dg", "death");

    static final GameData g0 = new GameData(1, "myname123", "Antonius", "firstmatch", new ChessGame());
    static final GameData g1 = new GameData(1, "antonius", "death", "chessgame", new ChessGame());
    static final GameData g2 = new GameData(1, "death", "myname123", "chessgame", new ChessGame());

    static final UserData u0 = new UserData("myname123", "p@ssword", "email@email.com");
    static final UserData u1 = new UserData("antonius", "G00dK!ght", "email@email.com");
    static final UserData u2 = new UserData("death", "humor0us", "email@email.com");

    static final Collection<AuthData> authData = List.of(new AuthData[]{a0, a1, a2});
    static final Collection<GameData> gameData = List.of(new GameData[]{g0, g1, g2});
    static final Collection<UserData> userData = List.of(new UserData[]{u0, u1, u2});

    static final Collection<GameSummary> gameSummaries = List.of(new GameSummary[]{
            new GameSummary(g0.gameID(), g0.whiteUsername(), g0.blackUsername(), g0.gameName()),
            new GameSummary(g1.gameID(), g1.whiteUsername(), g1.blackUsername(), g1.gameName()),
            new GameSummary(g2.gameID(), g2.whiteUsername(), g2.blackUsername(), g2.gameName())
    });
}
