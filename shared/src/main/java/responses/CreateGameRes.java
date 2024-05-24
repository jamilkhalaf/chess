package responses;

public class CreateGameRes {
    protected Integer gameID;

    public CreateGameRes(){}

    // Default constructor (required by Gson for serialization)
    public CreateGameRes(Integer gameID){
        this.gameID = gameID;
    }



    public Integer getGameID() {
        return gameID;
    }
}
