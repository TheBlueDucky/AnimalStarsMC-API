package me.theblueducky.animalStarsMCAPI.game;

import me.theblueducky.animalStarsMCAPI.animal.Animal;
import me.theblueducky.animalStarsMCAPI.event.MatchFoundEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerDequeueEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerQueueEvent;
import me.theblueducky.animalStarsMCAPI.team.Team;
import me.theblueducky.animalStarsMCAPI.team.TeamManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages per-gamemode lobbies and forms matches automatically when the
 * minimum player count is reached, balancing players into teams.
 */
public class QueueManager {

    private static QueueManager instance;
    private final Map<String, Lobby> lobbies;

    private QueueManager() {
        this.lobbies = new HashMap<>();
    }

    public static QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public void queuePlayer(String gamemodeId, String arenaName, Player player, Animal animal) {
        Gamemode gamemode = GameManager.getInstance().getGamemode(gamemodeId);
        if (gamemode == null) return;

        Lobby lobby = lobbies.computeIfAbsent(gamemodeId.toLowerCase(),
                k -> new Lobby(gamemodeId, arenaName));
        if (lobby.contains(player)) return;

        lobby.addPlayer(player, animal);
        Bukkit.getPluginManager().callEvent(new PlayerQueueEvent(player, lobby));

        if (lobby.isReady(gamemode.getMinPlayers())) {
            startMatch(lobby, gamemode);
        }
    }

    public void dequeuePlayer(String gamemodeId, Player player) {
        Lobby lobby = lobbies.get(gamemodeId.toLowerCase());
        if (lobby == null || !lobby.contains(player)) return;
        lobby.removePlayer(player);
        Bukkit.getPluginManager().callEvent(new PlayerDequeueEvent(player, lobby));
        if (lobby.getSize() == 0) {
            lobbies.remove(gamemodeId.toLowerCase());
        }
    }

    public Lobby getLobby(String gamemodeId) {
        return lobbies.get(gamemodeId.toLowerCase());
    }

    public boolean isQueued(String gamemodeId, Player player) {
        Lobby lobby = lobbies.get(gamemodeId.toLowerCase());
        return lobby != null && lobby.contains(player);
    }

    private void startMatch(Lobby lobby, Gamemode gamemode) {
        // Take up to maxPlayers; any extras remain in the lobby for the next match.
        List<Player> players = new ArrayList<>(lobby.getPlayers());
        int max = gamemode.getMaxPlayers();
        List<Player> participants = players.size() > max ? players.subList(0, max) : players;

        GameSession session = GameManager.getInstance().createSession(lobby.getGamemodeId(), lobby.getArenaName());
        if (session == null) return;

        List<Team> teams = session.getTeams();
        // For FFA (teamSize == 1), create one team per player so everyone gets a unique spawn.
        if (gamemode.getTeamSize() == 1 && participants.size() > 0) {
            teams = TeamManager.getInstance().createTeams(session.getId(), participants.size());
            session.setTeams(teams);
        }
        TeamManager.getInstance().assignPlayers(session.getId(), participants, teams);

        for (Player p : participants) {
            Animal animal = lobby.getChosenAnimal(p);
            Team team = TeamManager.getInstance().getTeamOfPlayer(session.getId(), p);
            GameManager.getInstance().addPlayerToSession(session, p, animal, team);
        }

        lobbies.remove(lobby.getGamemodeId().toLowerCase());
        // Fire MatchFoundEvent BEFORE starting the session so listeners can prepare players.
        Bukkit.getPluginManager().callEvent(new MatchFoundEvent(session));
        GameManager.getInstance().startSession(session);
    }

    public void clearAll() {
        lobbies.clear();
    }
}
