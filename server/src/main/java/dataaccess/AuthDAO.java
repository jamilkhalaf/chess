package dataaccess;
import java.util.UUID;

import model.AuthData;

public interface AuthDAO {

    String getAuth(String authToken) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    void deleteAuth () throws DataAccessException;
}
