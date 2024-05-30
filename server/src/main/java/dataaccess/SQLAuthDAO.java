package dataaccess;

import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    protected static Map<String, AuthData> auths = new HashMap<>();

    @Override
    public String getAuth(String authToken) throws DataAccessException {
//        if (auths.containsKey(authToken)) {
//            return authToken;
//        }
//        return null;
        String sql = "SELECT authToken FROM auth WHERE authToken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("authToken");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting auth token: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
//        if (auths.containsKey(authToken)) {
//            return auths.get(authToken).username();
//        }
//        return null;
        String sql = "SELECT username FROM auth WHERE authToken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error getting username by auth token: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
//        AuthData auth = new AuthData(authToken, username);
//        auths.put(authToken, auth);
//        return authToken;
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE authToken = VALUES(authToken)";

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, authToken);
                stmt.setString(2, username);

                if (stmt.executeUpdate() == 1) {
                    System.out.println("created auth " + username);
                } else {
                    System.out.println("Failed to create auth " + username);
                }
            } catch (SQLException ex) {
                System.err.println("SQL Exception: " + ex.getMessage());
            }
            connection.commit();
        } catch (SQLException ex) {
            System.err.println("SQL Exception on connection: " + ex.getMessage());
        }
        return authToken;
    }

    @Override
    public void deleteAuth (String authToken) throws DataAccessException {
//        auths.remove(authToken);
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error deleting auth token: " + ex.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
//        auths.clear(); // Clear the entire map
        String sql = "DELETE FROM auth";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error clearing auth tokens: " + ex.getMessage());
        }
    }

    @Override
    public Integer getSize() throws DataAccessException {
//        return auths.size();
        Integer databaseSize = 0;

        String sql = "SELECT COUNT(*) FROM auth";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                databaseSize = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting auth size: " + ex.getMessage());
        }

        return databaseSize;
    }
}
