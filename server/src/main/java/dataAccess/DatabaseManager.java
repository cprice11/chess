package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;
    private static final int AUTH_TOKEN_LEN = 40;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName + " CHARACTER SET utf8mb4";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String[] resetString = {
            "TRUNCATE TABLE auth;",
            "TRUNCATE TABLE games;",
            "TRUNCATE TABLE users;"
    };

    public static void resetData() throws DataAccessException {
        for (var statement : resetString) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(String.format("Unable to reset database: %s", e.getMessage()));
            }
        }
    }

    private static final String[] dbConfig = {
            "USE " + databaseName + ";",

            "CREATE TABLE IF NOT EXISTS auth ( "
                    + "authToken char(40) NOT NULL, "
                    + "username char(255) NOT NULL, "
                    + "PRIMARY KEY (authToken) "
                    + ");",
            "CREATE TABLE IF NOT EXISTS games ("
                    + "gameID int NOT NULL,"
                    + "whiteUsername char(255) DEFAULT NULL,"
                    + "blackUsername char(255) DEFAULT NULL,"
                    + "gameName char(255),"
                    + "game longtext NOT NULL,"
                    + "PRIMARY KEY (gameID)"
                    + ");",
            "CREATE TABLE IF NOT EXISTS users ("
                    + "username char(255) NOT NULL,"
                    + "password char(255) NOT NULL,"
                    + "email char(255) NOT NULL,"
                    + "PRIMARY KEY (username)"
                    + ");"
    };

    public static void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        for (var statement : dbConfig) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
            }
        }
    }
}
