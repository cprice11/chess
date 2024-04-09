package dataAccess.sqlDao;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.Connection;

public class SQLDaoHelpers {
    protected void executePreparedStatement(String statement) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
