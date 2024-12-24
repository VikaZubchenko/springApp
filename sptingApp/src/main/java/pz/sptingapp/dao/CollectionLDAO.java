package pz.sptingapp.dao;

import pz.sptingapp.Entity.*;
import pz.sptingapp.MyList.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CollectionLDAO implements IMyDAO {
    private MyListImpl<FootballMatchEntity> matches;

    public CollectionLDAO() {
        this.matches = new MyListImpl<>();
    }

    @Override
    public void addMatch(FootballMatchEntity match) {
        matches.add(match);
    }

    @Override
    public FootballMatchEntity getMatchByName(String matchName) {
        for (FootballMatchEntity match : matches) {
            if (match.getName().equals(matchName)) {
                return match;
            }
        }
        return null;
    }

    @Override
    public void updateMatchByName(FootballMatchEntity updatedMatch) {
        for (int i = 0; i < matches.size(); i++) {
            FootballMatchEntity match = (FootballMatchEntity) matches.toArray()[i];
            if (match.getName().equals(updatedMatch.getName())) {
                matches.remove(match);
                matches.add(updatedMatch);
                break;
            }
        }
    }

    @Override
    public MyListImpl<FootballMatchEntity> findMatchesByTeamName(String teamName) {
        MyListImpl<FootballMatchEntity> result = new MyListImpl<>();
        for (FootballMatchEntity match : matches) {
            if (match.getTeamName().contains(teamName)) {
                result.add(match);
            }
        }
        return result;
    }

    @Override
    public void deleteMatchByName(String matchName) {
        FootballMatchEntity toRemove = null;
        for (FootballMatchEntity entity : matches) {
            if (entity.getName().equals(matchName)) {
                toRemove = entity;
                break;
            }
        }
        if (toRemove != null) {
            matches.remove(toRemove);
            System.out.println("Deleted match with name: " + matchName);
        } else {
            System.out.println("No match with name: " + matchName);
        }
    }

    public MyListImpl<FootballMatchEntity> getAllMatches() {
        return matches;
    }

    @Override
    public void addPlayerToTeam(String teamName, String playerName) {
        for (FootballMatchEntity match : matches) {
            if (match.getTeamName().equals(teamName)) {
                if (!match.getPlayerName().isEmpty()) {
                    match.setPlayerName(match.getPlayerName() + ", " + playerName);
                } else {
                    match.setPlayerName(playerName);
                }
                return;
            }
        }
        throw new IllegalArgumentException("No team found with the given name: " + teamName);
    }

    @Override
    public MyListImpl<TeamEntity> getAllTeams() {
        MyListImpl<TeamEntity> teams = new MyListImpl<>();
        Map<Integer, TeamEntity> teamMap = new HashMap<>();

        for (FootballMatchEntity match : matches) {
            int teamID = match.getTeamID(); // Додайте відповідний метод у FootballMatchEntity, якщо потрібно
            String teamName = match.getTeamName();

            teamMap.putIfAbsent(teamID, new TeamEntity(teamID, teamName));
        }

        for (TeamEntity team : teamMap.values()) {
            teams.add(team);
        }

        return teams;
    }

    @Override
    public MyListImpl<TeamEntity> getAllTeamsWithPlayers() {
        MyListImpl<TeamEntity> teams = new MyListImpl<>();
        Map<Integer, TeamEntity> teamMap = new HashMap<>();

        for (FootballMatchEntity match : matches) {
            int teamID = match.getTeamID(); // Додайте відповідний метод у FootballMatchEntity, якщо потрібно
            String teamName = match.getTeamName();
            String playerNames = match.getPlayerName(); // Список гравців у форматі рядка

            TeamEntity team = teamMap.computeIfAbsent(teamID, id -> new TeamEntity(teamID, teamName));

            if (playerNames != null && !playerNames.isEmpty()) {
                for (String playerName : playerNames.split(",\\s*")) {
                    // Імітуємо створення PlayerEntity
                    team.getPlayers().add(new PlayerEntity(0, playerName)); // 0 як тимчасовий ID
                }
            }
        }

        for (TeamEntity team : teamMap.values()) {
            teams.add(team);
        }

        return teams;
    }


}
