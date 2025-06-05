package dataaccess;

import datamodels.UserData;

public class MySqlUser extends MySqlDataAccess implements UserDAO {
    @Override
    public void addUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    public void clearAll() {
        String sql = "TRUNCATE user";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
