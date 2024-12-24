package pz.sptingapp.dao;

import pz.sptingapp.Entity.*;
import pz.sptingapp.MyList.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLDAO implements IMyDAO {
    private Connection connection;

    public MySQLDAO() throws SQLException {
        this.connection = Connector.createConnection();
    }

    // Add a new football match and related records
    @Override
    public void addMatch(FootballMatchEntity match) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Insert into Matches table
            String matchQuery = "INSERT INTO Matches (Name, Location, MatchDate, NumberOfTeams, Duration) VALUES (?, ?, ?, ?, ?)";
            int matchID;
            try (PreparedStatement matchStmt = connection.prepareStatement(matchQuery, Statement.RETURN_GENERATED_KEYS)) {
                matchStmt.setString(1, match.getName());
                matchStmt.setString(2, match.getLocation());
                matchStmt.setDate(3, Date.valueOf(match.getMatchDate()));
                matchStmt.setInt(4, match.getNumberOfTeams());
                matchStmt.setInt(5, match.getDuration());
                matchStmt.executeUpdate();

                try (ResultSet generatedKeys = matchStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        matchID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve MatchID.");
                    }
                }
            }

            // Insert into Teams table
            String teamQuery = "INSERT INTO Teams (TeamName, MatchID) VALUES (?, ?)";
            int teamID;
            try (PreparedStatement teamStmt = connection.prepareStatement(teamQuery, Statement.RETURN_GENERATED_KEYS)) {
                teamStmt.setString(1, match.getTeamName());
                teamStmt.setInt(2, matchID);
                teamStmt.executeUpdate();

                try (ResultSet generatedKeys = teamStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        teamID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve TeamID.");
                    }
                }
            }

            // Insert into Players table
//            String playerQuery = "INSERT INTO Players (PlayerName, TeamID) VALUES (?, ?)";
//            try (PreparedStatement playerStmt = connection.prepareStatement(playerQuery)) {
//                playerStmt.setString(1, match.getPlayerName());
//                playerStmt.setInt(2, teamID);
//                playerStmt.executeUpdate();
//            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Retrieve a single match by name
    @Override
    public FootballMatchEntity getMatchByName(String matchName) throws SQLException {
        String query = "SELECT m.MatchID, m.Name, m.Location, m.MatchDate, m.NumberOfTeams, m.Duration, " +
                "t.TeamName " +
                "FROM Matches m " +
                "JOIN Teams t ON m.MatchID = t.MatchID " +
                "WHERE m.Name = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, matchName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FootballMatchEntity(
                            rs.getInt("MatchID"),
                            rs.getString("Name"),
                            rs.getString("Location"),
                            rs.getDate("MatchDate").toLocalDate(),
                            rs.getInt("NumberOfTeams"),
                            rs.getInt("Duration"),
                            rs.getString("TeamName")
                    );
                }
            }
        }
        return null;
    }

    // Update match and related records by match name
    @Override
    public void updateMatchByName(FootballMatchEntity match) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Update Teams table
            String teamQuery = "UPDATE Teams SET TeamName = ? WHERE MatchID = (SELECT MatchID FROM Matches WHERE Name = ? LIMIT 1)";
            try (PreparedStatement teamStmt = connection.prepareStatement(teamQuery)) {
                teamStmt.setString(1, match.getTeamName());
                teamStmt.setString(2, match.getName());
                teamStmt.executeUpdate();
            }

            // Update Matches table
            String matchQuery = "UPDATE Matches SET Name = ?, Location = ?, MatchDate = ?, NumberOfTeams = ?, Duration = ? WHERE Name = ?";
            try (PreparedStatement matchStmt = connection.prepareStatement(matchQuery)) {
                matchStmt.setString(1, match.getName());
                matchStmt.setString(2, match.getLocation());
                matchStmt.setDate(3, Date.valueOf(match.getMatchDate()));
                matchStmt.setInt(4, match.getNumberOfTeams());
                matchStmt.setInt(5, match.getDuration());
                matchStmt.setString(6, match.getName());
                matchStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Search for matches by team name
    @Override
    public MyListImpl<FootballMatchEntity> findMatchesByTeamName(String teamName) throws SQLException {
        MyListImpl<FootballMatchEntity> matches = new MyListImpl<>();
        String query = "SELECT m.MatchID, m.Name, m.Location, m.MatchDate, m.NumberOfTeams, m.Duration, t.TeamName " +
                "FROM Matches m " +
                "JOIN Teams t ON m.MatchID = t.MatchID " +
                "WHERE t.TeamName LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + teamName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(new FootballMatchEntity(
                            rs.getInt("MatchID"),
                            rs.getString("Name"),
                            rs.getString("Location"),
                            rs.getDate("MatchDate").toLocalDate(),
                            rs.getInt("NumberOfTeams"),
                            rs.getInt("Duration"),
                            rs.getString("TeamName")
                    ));
                }
            }
        }
        return matches;
    }

    // Delete a match and related records
    @Override
    public void deleteMatchByName(String matchName) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Delete from Matches table (cascades to Teams and Players)
            String matchQuery = "DELETE FROM Matches WHERE Name = ?";
            try (PreparedStatement matchStmt = connection.prepareStatement(matchQuery)) {
                matchStmt.setString(1, matchName);
                matchStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Retrieve all matches with team names
    public MyListImpl<FootballMatchEntity> getAllMatches() throws SQLException {
        MyListImpl<FootballMatchEntity> matches = new MyListImpl<>();
        String query = "SELECT m.MatchID, m.Name, m.Location, m.MatchDate, m.NumberOfTeams, m.Duration, t.TeamName " +
                "FROM Matches m " +
                "JOIN Teams t ON m.MatchID = t.MatchID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                matches.add(new FootballMatchEntity(
                        rs.getInt("MatchID"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getDate("MatchDate").toLocalDate(),
                        rs.getInt("NumberOfTeams"),
                        rs.getInt("Duration"),
                        rs.getString("TeamName")
                ));
            }
        }
        return matches;
    }

    @Override
    public void addPlayerToTeam(String teamName, String playerName) throws SQLException {
        String query = "INSERT INTO Players (PlayerName, TeamID) " +
                "VALUES (?, (SELECT TeamID FROM Teams WHERE TeamName = ? LIMIT 1))";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setString(2, teamName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No team found with the given name: " + teamName);
            }
        }
    }

    @Override
    public MyListImpl<TeamEntity> getAllTeams() throws SQLException {
        MyListImpl<TeamEntity> teams = new MyListImpl<>();
        String query = "SELECT TeamID, TeamName FROM Teams";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                teams.add(new TeamEntity(rs.getInt("TeamID"), rs.getString("TeamName")));
            }
        }
        return teams;
    }

    @Override
    public MyListImpl<TeamEntity> getAllTeamsWithPlayers() throws SQLException {
        MyListImpl<TeamEntity> teams = new MyListImpl<>();
        String query = "SELECT t.TeamID, t.TeamName, p.PlayerID, p.PlayerName " +
                "FROM Teams t LEFT JOIN Players p ON t.TeamID = p.TeamID";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            Map<Integer, TeamEntity> teamMap = new HashMap<>();
            while (rs.next()) {
                int teamID = rs.getInt("TeamID");
                String teamName = rs.getString("TeamName");
                int playerID = rs.getInt("PlayerID");
                String playerName = rs.getString("PlayerName");

                // Додаємо команду до мапи, якщо її ще немає
                TeamEntity team = teamMap.computeIfAbsent(teamID, id -> new TeamEntity(id, teamName));
                if (playerName != null) {
                    team.getPlayers().add(new PlayerEntity(playerID, playerName));
                }
            }
            for (TeamEntity team : teamMap.values()) {
                teams.add(team);
            }
        }
        return teams;
    }


}