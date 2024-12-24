package pz.sptingapp.dao;

import pz.sptingapp.Entity.*;
import pz.sptingapp.MyList.MyListImpl;

import java.sql.SQLException;

public interface IMyDAO {
    void addMatch(FootballMatchEntity match) throws SQLException;
    FootballMatchEntity getMatchByName(String matchName) throws SQLException;
    void updateMatchByName(FootballMatchEntity match) throws SQLException;
    void deleteMatchByName(String matchName) throws SQLException;
    MyListImpl<FootballMatchEntity> findMatchesByTeamName(String teamName) throws SQLException;
    MyListImpl<FootballMatchEntity> getAllMatches() throws SQLException;
    void addPlayerToTeam(String teamName, String playerName) throws SQLException;
    MyListImpl<TeamEntity> getAllTeams() throws SQLException;
    MyListImpl<TeamEntity> getAllTeamsWithPlayers() throws SQLException;

}
