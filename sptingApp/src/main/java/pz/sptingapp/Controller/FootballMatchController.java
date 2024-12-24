package pz.sptingapp.Controller;

import pz.sptingapp.dao.*;
import pz.sptingapp.Entity.*;
import pz.sptingapp.MyList.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class FootballMatchController {
    private final IMyDAO dao;

    public FootballMatchController() throws SQLException {
        this.dao = DAOFactory.getDAOInstance(TypeDAO.MySQL);
//        this.dao = DAOFactory.getDAOInstance(TypeDAO.MyCOLLECTION);
    }

    @RequestMapping(value = {"/", "/FootballMatches"}, method = RequestMethod.GET)
    public String showAllFootballMatches(Model model) throws SQLException {
        MyListImpl<FootballMatchEntity> footballMatches = dao.getAllMatches();
        model.addAttribute("allFootballMatches", footballMatches);
        return "FootballMatches";
    }

    @RequestMapping(value = {"/addFootballMatch"}, method = RequestMethod.GET)
    public String showAddFootballMatchView(Model model) {
        FootballMatchEntity footballMatch = new FootballMatchEntity();
        model.addAttribute("footballMatch", footballMatch);
        return "addFootballMatch";
    }

    @RequestMapping(value = {"/addFootballMatch"}, method = RequestMethod.POST)
    public String addFootballMatch(Model model, FootballMatchEntity footballMatch) throws SQLException {
        dao.addMatch(footballMatch);
        return "redirect:/FootballMatches";
    }

    @RequestMapping(value = {"/editFootballMatch"}, method = RequestMethod.GET)
    public String showEditFootballMatchView(Model model) {
        return "findFootballMatchToEdit";
    }

    @RequestMapping(value = {"/editFootballMatch"}, method = RequestMethod.POST)
    public String editFootballMatch(@RequestParam("name") String name, Model model) throws SQLException {
        FootballMatchEntity footballMatch = dao.getMatchByName(name);
        if (footballMatch == null) {
            model.addAttribute("errorMessage", "Football match not found");
            return "redirect:/FootballMatches";
        }
        model.addAttribute("footballMatch", footballMatch);
        return "editFootballMatch";
    }

    @RequestMapping(value = {"/editFootballMatch/save"}, method = RequestMethod.POST)
    public String saveFootballMatch(FootballMatchEntity footballMatch) throws SQLException {
        dao.updateMatchByName(footballMatch);
        return "redirect:/FootballMatches";
    }

    @RequestMapping(value = {"/deleteFootballMatch"}, method = RequestMethod.GET)
    public String showDeleteFootballMatchView(Model model) {
        return "deleteFootballMatch";
    }

    @RequestMapping(value = {"/deleteFootballMatch"}, method = RequestMethod.POST)
    public String deleteFootballMatch(@RequestParam("name") String name, Model model) throws SQLException {
        FootballMatchEntity footballMatch = dao.getMatchByName(name);
        if (footballMatch == null) {
            model.addAttribute("errorMessage", "Football match not found");
            return "deleteFootballMatch";
        }
        dao.deleteMatchByName(name);
        return "redirect:/FootballMatches";
    }

    @RequestMapping(value = {"/FootballMatchesByTeamName"}, method = RequestMethod.GET)
    public String showFindFootballMatchesByTeamNameView(Model model) {
        return "FootballMatchesByTeamName";
    }

    @RequestMapping(value = {"/FootballMatchesByTeamName"}, method = RequestMethod.POST)
    public String findFootballMatchesByTeamName(@RequestParam("teamName") String teamName, Model model) throws SQLException {
        MyListImpl<FootballMatchEntity> footballMatches = dao.findMatchesByTeamName(teamName);
        model.addAttribute("allFootballMatches", footballMatches);
        return "FootballMatchesByTeamName";
    }

    @RequestMapping(value = {"/addPlayer"}, method = RequestMethod.GET)
    public String showAddPlayerView(Model model) throws SQLException {
        // Створюємо новий об'єкт гравця
        FootballMatchEntity player = new FootballMatchEntity();
        model.addAttribute("player", player);
        // Передаємо список команд для вибору
        model.addAttribute("teams", dao.getAllTeams());
        return "addPlayer";
    }

    @RequestMapping(value = {"/addPlayer"}, method = RequestMethod.POST)
    public String addPlayer(@RequestParam("teamName") String teamName,
                            @RequestParam("playerName") String playerName, Model model) throws SQLException {
        try {
            dao.addPlayerToTeam(teamName, playerName);
            return "redirect:/TeamsWithPlayers";
        } catch (SQLException e) {
            model.addAttribute("errorMessage", "Failed to add player: " + e.getMessage());
            return "redirect:/TeamsWithPlayers";
        }
    }

    @RequestMapping(value = {"/TeamsWithPlayers"}, method = RequestMethod.GET)
    public String showTeamsWithPlayers(Model model) throws SQLException {
        MyListImpl<TeamEntity> teamsWithPlayers = dao.getAllTeamsWithPlayers();
        model.addAttribute("teamsWithPlayers", teamsWithPlayers);
        return "TeamsWithPlayers";
    }
}
