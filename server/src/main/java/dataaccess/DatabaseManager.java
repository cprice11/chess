package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;
    private static boolean databaseInitialized = false;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
        try {
            createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static protected void createDatabase() throws DataAccessException {
        if (databaseInitialized) {
            return;
        }
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword); var createStatement = conn.prepareStatement(statement)) {
            createStatement.executeUpdate();
            var useStatement = conn.prepareStatement("USE chess;");
            useStatement.executeUpdate();
            String[] tables = {"auth (authToken varchar(36) PRIMARY KEY, username varchar(255));", "game (id int PRIMARY KEY, blackUsername varchar(255), whiteUsername varchar(255), gameName varchar(255), game JSON);", "user (username varchar(255) PRIMARY KEY, password varchar(60), email varchar(255));"};
            for (String table : tables) {
                conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + table).executeUpdate();
            }
            databaseInitialized = true;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
