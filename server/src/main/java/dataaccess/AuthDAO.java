package dataaccess;
import java.util.UUID;

import model.AuthData;

public interface AuthDAO {

    String getAuth(String authToken) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    void deleteAuth (String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
