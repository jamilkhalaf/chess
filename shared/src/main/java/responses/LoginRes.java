package responses;

public class LoginRes {
    protected String authToken;
    protected String username;

    public LoginRes(){}

    // Default constructor (required by Gson for serialization)
    public LoginRes(String username,String authToken){
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
