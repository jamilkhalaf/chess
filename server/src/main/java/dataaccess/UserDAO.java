package dataaccess;

import model.UserData;

public interface UserDAO {

    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username, String password) throws DataAccessException;

    void clear(UserData data) throws DataAccessException;
}
