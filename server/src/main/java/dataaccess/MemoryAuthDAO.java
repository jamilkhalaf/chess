package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    protected static Map<String, AuthData> auths = new HashMap<>();

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        if (MemoryUserDAO.users.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        auths.put(authToken, auth);
        return authToken;
    }

    @Override
    public void deleteAuth () throws DataAccessException {
        auths.clear();
    }
}
