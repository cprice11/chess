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
                // PetShop has this whole deal going on, I don't think I need it right now
//                if (returnStatement.next()) {
//                    return returnStatement.getInt(1);
//                }
//                return 0;
            }
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Attempting to access data incorrectly:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", sqlStatement, e.getMessage()));
        }
    }
}
