package dataaccess;

import org.junit.jupiter.api.*;

import service.ClearService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClearSQLTest {
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @DisplayName("SQL Clear Data Test")
    public void testClearSQLData() throws DataAccessException {
        // Clear existing data
        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();

        // Add test data
        String user1 = "alex";
        String pass1 = "pass123";
        String email1 = "alex@byu.edu";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "sam";
        String pass2 = "pass456";
        String email2 = "sam@edu.com";
        userDAO.createUser(user2, pass2, email2);

        gameDAO.createGame("alex's adventure");
        authDAO.createAuth("alex");

        // Clear data again
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();

        // Verify data is cleared
        assertEquals(0, authDAO.getSize());
        assertEquals(0, userDAO.getSize());
        assertEquals(0, gameDAO.getSize());
    }

    @Test
    @DisplayName("SQL Clear Data Test")
    public void testClearSQLData2() throws DataAccessException {
        // Clear existing data
        ClearService clearService1 = new ClearService(userDAO, gameDAO, authDAO);
        clearService1.clearUser();

        // Add test data
        String user1 = "haifa";
        String pass1 = "wahbe";
        String email1 = "fdjfbje";
        userDAO.createUser(user1, pass1, email1);

        String user2 = "dzeko";
        String pass2 = "muller";
        String email2 = "pop@edu";
        userDAO.createUser(user2, pass2, email2);

        gameDAO.createGame("alex's adventure");
        authDAO.createAuth("dzeko");

        // Clear data again
        ClearService clearService2 = new ClearService(userDAO, gameDAO, authDAO);
        clearService2.clearUser();

        // Verify data is cleared
    }
}
