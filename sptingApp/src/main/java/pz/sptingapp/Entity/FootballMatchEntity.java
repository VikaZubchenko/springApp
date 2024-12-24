package pz.sptingapp.Entity;

import java.time.LocalDate;
import java.util.Objects;

public class FootballMatchEntity {
    // Attributes from Matches table
    private int matchID;
    private String name;
    private String location;
    private LocalDate matchDate;
    private int numberOfTeams;
    private int duration;

    // Attributes from Teams table
    private int teamID;
    private String teamName;

    // Attributes from Players table
    private int playerID;
    private String playerName = "";

    // Constructor
    public FootballMatchEntity(int matchID, String name, String location, LocalDate matchDate, int numberOfTeams,
                               int duration, int teamID, String teamName, int playerID, String playerName) {
        this.matchID = matchID;
        this.name = name;
        this.location = location;
        this.matchDate = matchDate;
        this.numberOfTeams = numberOfTeams;
        this.duration = duration;
        this.teamID = teamID;
        this.teamName = teamName;
        this.playerID = playerID;
        this.playerName = playerName;
    }

    public FootballMatchEntity(int matchID, String name, String location, LocalDate matchDate, int numberOfTeams,
                               int duration, String teamName) {
        this.matchID = matchID;
        this.name = name;
        this.location = location;
        this.matchDate = matchDate;
        this.numberOfTeams = numberOfTeams;
        this.duration = duration;
        this.teamName = teamName;
    }

    public FootballMatchEntity(String name, String location, LocalDate matchDate, int numberOfTeams,
                               int duration, String teamName) {
        this.name = name;
        this.location = location;
        this.matchDate = matchDate;
        this.numberOfTeams = numberOfTeams;
        this.duration = duration;
        this.teamName = teamName;
    }

    public FootballMatchEntity() {

    }

    // Getters and setters
    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    // Overriding hashCode
    @Override
    public int hashCode() {
        return Objects.hash(matchID, name, location, matchDate, numberOfTeams, duration, teamID, teamName, playerID, playerName);
    }

    // Overriding equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FootballMatchEntity that = (FootballMatchEntity) obj;
        return matchID == that.matchID &&
                numberOfTeams == that.numberOfTeams &&
                duration == that.duration &&
                teamID == that.teamID &&
                playerID == that.playerID &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(matchDate, that.matchDate) &&
                Objects.equals(teamName, that.teamName) &&
                Objects.equals(playerName, that.playerName);
    }

    // Overriding toString
    @Override
    public String toString() {
        return "FootballMatchEntity{" +
                "matchID=" + matchID +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", matchDate=" + matchDate +
                ", numberOfTeams=" + numberOfTeams +
                ", duration=" + duration +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
