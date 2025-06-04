package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTests extends DaoUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all user data")
    public void clearUser() {
        throw new RuntimeException("Not implemented yet");
    }

    // AddUser
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateUser() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedUser() {
        throw new RuntimeException("Not implemented yet");
    }

    // getUser
    @Test
    @DisplayName("Get user by username")
    public void goodGetUserCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Using bad username fails")
    public void badDataGetUserCalls() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    @DisplayName("Malformed get calls fail")
    public void malformedGetUser() {
        throw new RuntimeException("Not implemented yet");
    }
}
