package service.service;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.LoginReq;
import requests.RegisterReq;
import responses.LoginRes;
import responses.RegisterRes;
import service.LoginService;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterServiceTest {


        private UserDAO userDAO = new MemoryUserDAO();
        private GameDAO gameDAO = new MemoryGameDAO();
        private AuthDAO authDAO = new MemoryAuthDAO();

        @Test
        @DisplayName("Register success")
        public void registerSuccess() throws DataAccessException {
            // Ensure initial data is present
            String user1 = "jamil";
            String pass1 = "123";
            String email1 = "jamil@byu";
            userDAO.createUser(user1, pass1, email1);

            String user2 = "khalaf";
            String pass2 = "24343";
            String email2 = "jamil@edu";
            userDAO.createUser(user2, pass2, email2);




           assertEquals(2,userDAO.getSize());


        }

        @Test
        @DisplayName("Register failure")
        public void registerFailure() throws DataAccessException {


            String user1 = "jamil";
            String pass1 = "123";
            String email1 = "jamil@byu";
            userDAO.createUser(user1, pass1, email1);

            String user2 = "khalaf";
            String pass2 = "24343";
            String email2 = "jamil@edu";
            userDAO.createUser(user2, pass2, email2);

            RegisterReq registerRequest1 = new RegisterReq("jamil", "123", "adam@gmail");
            RegisterReq registerRequest2 = new RegisterReq("khalaf", "12345", "adam@gmail.com");


            RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

            DataAccessException exception1 = assertThrows(DataAccessException.class, () -> {
                registerService.createUser(registerRequest1.getUsername(), registerRequest1.getPassword(), registerRequest1.getEmail());
            });
            assertEquals("User already exists", exception1.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

            DataAccessException exception2 = assertThrows(DataAccessException.class, () -> {
                registerService.createUser(registerRequest2.getUsername(), registerRequest2.getPassword(), registerRequest2.getEmail());
            });
            assertEquals("User already exists", exception2.getMessage(), "Exception message should be 'Error: unauthorized' for user1");

        }
}


