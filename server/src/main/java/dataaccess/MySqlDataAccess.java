package dataaccess;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess {
    protected static Object executeUpdate(String sqlStatement, Object expectedObject) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
                var returnStatement = preparedStatement.getGeneratedKeys();
                if (returnStatement.next()) {
                    return returnStatement.getInt(1);
                }
                return 0;
            }
        } catch (DataAccessException e) {
            throw new ResponseException(500, String.format("Attempting to access data incorrectly:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", sqlStatement, e.getMessage()));
        }
    }
}
