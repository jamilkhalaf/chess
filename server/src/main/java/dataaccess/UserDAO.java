package dataaccess;

public interface UserDAO {
    void getUser(String username, String password) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    void clear() throws DataAccessException;
}
