package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthTests extends DaoUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all auth")
    public void clearAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    // AddAuth
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    // getAuth
    @Test
    @DisplayName("Get by token and username")
    public void goodGetAuthCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Using bad token or username fails")
    public void badDataGetAuthCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Malformed get calls fail")
    public void malformedGetAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    // deleteAuth
    @Test
    @DisplayName("delete removes auth from database")
    public void goodDeleteAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Can't delete with bad authToken")
    public void badDeleteAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Can't delete twice")
    public void repeatDeleteAuth() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Malformed delete calls fail")
    public void malformedDeleteAuth() {
        throw new RuntimeException("Not implemented yet");
    }
}
