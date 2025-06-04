package dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess {
    public MySqlDataAccess() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected static ResultSet executeUpdate(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
                return preparedStatement.getGeneratedKeys();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }
}
