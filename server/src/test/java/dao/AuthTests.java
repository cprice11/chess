package dao;

import dataaccess.DataAccessException;
import datamodels.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthTests extends DbUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all auth")
    public void clearAuth() {
        addTwoAuth();
        authDAO.clearAll();
        try {
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // AddAuth
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        try {
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
            authDAO.addAuth(authA);
            Assertions.assertEquals(authA, authDAO.getAuthByAuthToken(authA.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateAuth() {
        try {
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
            authDAO.addAuth(authA);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.addAuth(authA);
        });
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.addAuth(null);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.addAuth(new AuthData("username", null));
        });
    }

    // getAuth
    @Test
    @DisplayName("Get by token and username")
    public void goodGetAuthCalls() {
        addTwoAuth();
        try {
            Assertions.assertEquals(authA, authDAO.getAuthByAuthToken(authA.authToken()));
            Assertions.assertEquals(authA.username(), authDAO.getAuthByUsername(authA.username()).username());
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Using bad token or username fails")
    public void badDataGetAuthCalls() {
        addTwoAuth();
        try {
            Assertions.assertNull(authDAO.getAuthByAuthToken(authC.authToken()));
            Assertions.assertNull(authDAO.getAuthByUsername(authC.username()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // I could be convinced that this should just return null
    @Test
    @DisplayName("Malformed get calls fail")
    public void malformedGetAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuthByAuthToken(null);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuthByUsername(null);
        });
    }

    // deleteAuth
    @Test
    @DisplayName("delete removes auth from database")
    public void goodDeleteAuth() {
        addTwoAuth();
        try {
            Assertions.assertEquals(authA, authDAO.getAuthByAuthToken(authA.authToken()));
            authDAO.deleteAuthByAuthToken(authA.authToken());
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // This may not work inside the try-catch block
    @Test
    @DisplayName("Can't delete with bad authToken")
    public void badDeleteAuth() {
        addTwoAuth();
        try {
            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.deleteAuthByAuthToken(authC.authToken());
            });
            Assertions.assertEquals(authA, authDAO.getAuthByAuthToken(authA.authToken()));
            Assertions.assertEquals(authB, authDAO.getAuthByAuthToken(authB.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Can delete twice")
    public void repeatDeleteAuth() {
        addTwoAuth();
        try {
            Assertions.assertEquals(authA, authDAO.getAuthByAuthToken(authA.authToken()));
            authDAO.deleteAuthByAuthToken(authA.authToken());
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
            authDAO.deleteAuthByAuthToken(authA.authToken());
            // The database doesn't need to care if what it's deleting isn't there
            // The logic for the 401 error is done by the service.
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Malformed delete calls fail")
    public void malformedDeleteAuth() {
        addTwoAuth();
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuthByAuthToken(null);
        });
    }

    public void addTwoAuth() {
        try {
            authDAO.addAuth(authA);
            authDAO.addAuth(authB);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }
}
