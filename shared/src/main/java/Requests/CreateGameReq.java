package Requests;

public class CreateGameReq {
    private String gameName;



    public CreateGameReq(){}

    public CreateGameReq(String gameName){
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
