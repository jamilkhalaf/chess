package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class MemoryUserDAO implements UserDAO {
    protected static Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
//        UserData newUser = new UserData(username,password,email);
//        users.put(username, newUser);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE password = VALUES(password), email = VALUES(email)";

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, email);

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Registered user " + username);
                } else {
                    System.out.println("Failed to register " + username);
                }
            } catch (SQLException ex) {
                System.err.println("SQL Exception: " + ex.getMessage());
            }
            connection.commit();
        } catch (SQLException ex) {
            System.err.println("SQL Exception on connection: " + ex.getMessage());
        }

    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
//        if (users.containsKey(username)) {
//            return users.get(username);
//        }
//        return null;
        String sql = "SELECT username, password, email FROM user WHERE username = ? ";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                    return new UserData(
                            rs.getString("username"),
                            password,
                            rs.getString("email")
                    );
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Error getting user data: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL Exception on connection: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public void clear() throws DataAccessException {
//        users.clear();
        String sql = "DELETE FROM user";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error clearing user data: " + ex.getMessage());
        }
    }

    @Override
    public Integer getSize() {

        return users.size();
//        Integer databaseSize = 0;
//        String sql = "SELECT COUNT(*) FROM user";
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            throw new DataAccessException("Error clearing user data: " + ex.getMessage());
//        }
//
//        return databaseSize;
    }
}
