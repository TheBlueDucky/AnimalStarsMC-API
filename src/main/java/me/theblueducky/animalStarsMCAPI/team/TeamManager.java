package me.theblueducky.animalStarsMCAPI.team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages teams per game session and provides balancing/assignment helpers.
 * A "session" is identified by a String id (typically the GameSession id).
 *
 * FFA is modelled as teamSize 1 with one team per player.
 */
public class TeamManager {

    private static TeamManager instance;

    // sessionId -> teams
    private final Map<String, List<Team>> sessionTeams;
    // sessionId -> (playerUuid -> team)
    private final Map<String, Map<UUID, Team>> sessionPlayerTeam;

    // Default color palette (valid ChatColors only)
    private static final ChatColor[] DEFAULT_PALETTE = {
            ChatColor.RED, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW,
            ChatColor.AQUA, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.WHITE,
            ChatColor.DARK_PURPLE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED
    };

    private TeamManager() {
        this.sessionTeams = new HashMap<>();
        this.sessionPlayerTeam = new HashMap<>();
    }

    public static TeamManager getInstance() {
        if (instance == null) {
            instance = new TeamManager();
        }
        return instance;
    }

    /**
     * Create {@code teamCount} teams for a session.
     * For FFA pass teamCount = number of players and teamSize = 1.
     */
    public List<Team> createTeams(String sessionId, int teamCount) {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            ChatColor color = DEFAULT_PALETTE[i % DEFAULT_PALETTE.length];
            teams.add(new Team(i, "Team " + (i + 1), color));
        }
        sessionTeams.put(sessionId, teams);
        sessionPlayerTeam.put(sessionId, new HashMap<>());
        return teams;
    }

    /**
     * Distribute players across the given teams round-robin (balanced).
     * For FFA, pass one team per player so each team gets exactly one member.
     */
    public void assignPlayers(String sessionId, List<Player> players, List<Team> teams) {
        if (teams == null || teams.isEmpty() || players == null || players.isEmpty()) {
            return;
        }
        Map<UUID, Team> map = sessionPlayerTeam.computeIfAbsent(sessionId, k -> new HashMap<>());
        int idx = 0;
        for (Player p : players) {
            Team t = teams.get(idx % teams.size());
            t.addPlayer(p);
            map.put(p.getUniqueId(), t);
            idx++;
        }
    }

    public Team getTeamOfPlayer(String sessionId, Player player) {
        Map<UUID, Team> map = sessionPlayerTeam.get(sessionId);
        return map != null ? map.get(player.getUniqueId()) : null;
    }

    public List<Team> getTeams(String sessionId) {
        return new ArrayList<>(sessionTeams.getOrDefault(sessionId, new ArrayList<>()));
    }

    public void removeSession(String sessionId) {
        sessionTeams.remove(sessionId);
        sessionPlayerTeam.remove(sessionId);
    }

    public void clearAll() {
        sessionTeams.clear();
        sessionPlayerTeam.clear();
    }
}
