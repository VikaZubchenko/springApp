package pz.sptingapp.Entity;

import pz.sptingapp.MyList.MyListImpl;

public class TeamEntity {
    private int teamID;
    private String teamName;
    private MyListImpl<PlayerEntity> players = new MyListImpl<>();

    public TeamEntity(int teamID, String teamName) {
        this.teamID = teamID;
        this.teamName = teamName;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public MyListImpl<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(MyListImpl<PlayerEntity> players) {
        this.players = players;
    }
}

