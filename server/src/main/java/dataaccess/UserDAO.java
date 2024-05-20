package dataaccess;

import model.UserData;

public class UserDAO {

    void createUser(String username, String password, String email) throws DataAccessException {
    }

    String getUser(String username, String password) throws DataAccessException {
        return username;
    }

    void clear(UserData data) throws DataAccessException {
    }
}
