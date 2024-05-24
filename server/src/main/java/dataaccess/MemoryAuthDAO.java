package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    protected static Map<String, AuthData> auths = new HashMap<>();

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        if (auths.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    @Override
    public String getUsername(String authToken) {
        if (auths.containsKey(authToken)) {
            return auths.get(authToken).username();
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
    public void deleteAuth (String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear(); // Clear the entire map
    }

    @Override
    public Integer getSize() {
        return auths.size();
    }
}
