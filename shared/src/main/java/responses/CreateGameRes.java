package responses;

public class CreateGameRes {
    protected String authToken;
    protected Integer gameID;

    public CreateGameRes(){}

    // Default constructor (required by Gson for serialization)
    public CreateGameRes(Integer gameID,String authToken){
        this.gameID = gameID;
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }
}
