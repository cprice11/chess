package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTests extends DbUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all games")
    public void clearGame() {
        throw new RuntimeException("Not implemented yet");
    }

    // AddGame
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateGame() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedGame() {
        throw new RuntimeException("Not implemented yet");
    }

    // getGame
    @Test
    @DisplayName("Get game by id")
    public void goodGetGameCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Using bad id fails")
    public void badDataGetGameCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Malformed get calls fail")
    public void malformedGetGame() {
        throw new RuntimeException("Not implemented yet");
    }

    // updateGame
    @Test
    @DisplayName("Update game one move at a time")
    public void goodUpdateGameCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Using bad id fails")
    public void badUpdateGameCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Malformed update calls fail")
    public void malformedUpdateGame() {
        throw new RuntimeException("Not implemented yet");
    }

    // Get game summaries
    @Test
    @DisplayName("Good get summaries call")
    public void getGameSummaries() {
        throw new RuntimeException("Not implemented yet");
    }


    @Test
    @DisplayName("Malformed summaries call")
    public void malformedGetGameSummaries() {
        throw new RuntimeException("Not implemented yet");
    }
}
