package Responses;

public class RegisterRes {
    protected String authToken;
    protected String username;

    public RegisterRes(){}

    // Default constructor (required by Gson for serialization)
    public RegisterRes(String username,String authToken){
        this.username = username;
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
