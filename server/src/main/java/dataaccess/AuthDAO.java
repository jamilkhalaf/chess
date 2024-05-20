package dataaccess;

public interface AuthDAO {
    String getUsername(String authToken) throws DataAccessException;
    void deleteAuth (String authToken) throws DataAccessException;
    String getAuth(String authToken) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;
    void clear () throws DataAccessException;
}
