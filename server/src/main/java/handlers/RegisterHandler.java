package handlers;

import Requests.RegisterReq;
import Responses.RegisterRes;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class RegisterHandler {

    private RegisterService registerService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.registerService = new RegisterService(userDAO, gameDAO, authDAO);
    }

    public Route handleCreateUser = (Request request, Response response) -> {

        try {
            RegisterReq userRequest = gson.fromJson(request.body(), RegisterReq.class);
            String authToken = registerService.createUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getEmail());

            RegisterRes registerResponse = new RegisterRes(userRequest.getUsername(), authToken);

            // Set the response type to JSON
            response.type("application/json");

            // Return the JSON response
            return gson.toJson(registerResponse);
        }

        catch (DataAccessException e) {
            response.status(403);
            return "{\"message\": \"Error: already taken\"}";
        }
        catch (IllegalArgumentException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\"}";
        }

    };
}
