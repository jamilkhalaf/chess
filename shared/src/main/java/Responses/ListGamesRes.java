package Responses;

import model.GameData;
import java.util.List;

public class ListGamesRes {
    private List<GameData> games;

    public ListGamesRes(List<GameData> games) {
        this.games = games;
    }

    public List<GameData> getGames() {
        return games;
    }

    public void setGames(List<GameData> games) {
        this.games = games;
    }
}
