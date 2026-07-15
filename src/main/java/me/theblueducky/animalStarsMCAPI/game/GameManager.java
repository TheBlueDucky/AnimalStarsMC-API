package me.theblueducky.animalStarsMCAPI.game;

import me.theblueducky.animalStarsMCAPI.AnimalStarsMCAPI;
import me.theblueducky.animalStarsMCAPI.animal.Animal;
import me.theblueducky.animalStarsMCAPI.event.GameEndEvent;
import me.theblueducky.animalStarsMCAPI.event.GameStartEvent;
import me.theblueducky.animalStarsMCAPI.event.GameTickEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerJoinGameEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerKillEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerLeaveGameEvent;
import me.theblueducky.animalStarsMCAPI.player.PlayerManager;
import me.theblueducky.animalStarsMCAPI.player.PlayerStats;
import me.theblueducky.animalStarsMCAPI.team.Team;
import me.theblueducky.animalStarsMCAPI.team.TeamManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private static GameManager instance;
    private AnimalStarsMCAPI plugin;

    private final Map<String, Gamemode> gamemodes;
    private final Map<String, GameArena> arenas;
    private final Map<String, GameSession> sessions;
    private final Map<String, BukkitTask> sessionTasks;
    private final Map<UUID, GameSession> playerSession;

    private int sessionCounter;

    private GameManager() {
        this.gamemodes = new HashMap<>();
        this.arenas = new HashMap<>();
        this.sessions = new HashMap<>();
        this.sessionTasks = new HashMap<>();
        this.playerSession = new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void init(AnimalStarsMCAPI plugin) {
        this.plugin = plugin;
    }

    // ---- Gamemode registry ----

    public void registerGamemode(Gamemode gamemode) {
        gamemodes.put(gamemode.getId().toLowerCase(), gamemode);
    }

    public Gamemode getGamemode(String id) {
        return gamemodes.get(id.toLowerCase());
    }

    public boolean isGamemodeRegistered(String id) {
        return gamemodes.containsKey(id.toLowerCase());
    }

    public Collection<Gamemode> getGamemodes() {
        return new ArrayList<>(gamemodes.values());
    }

    // ---- Arena registry ----

    public void registerArena(String name, GameArena arena) {
        arenas.put(name.toLowerCase(), arena);
    }

    public GameArena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Collection<GameArena> getArenas() {
        return new ArrayList<>(arenas.values());
    }

    // ---- Session lifecycle ----

    public GameSession createSession(String gamemodeId, String arenaName) {
        Gamemode gamemode = getGamemode(gamemodeId);
        GameArena arena = getArena(arenaName);
        if (gamemode == null || arena == null) return null;

        String sessionId = gamemodeId.toLowerCase() + "_" + (++sessionCounter);
        GameSession session = new GameSession(sessionId, gamemode, arena, gamemode.getDurationTicks());
        sessions.put(sessionId, session);

        List<Team> teams = TeamManager.getInstance().createTeams(sessionId, gamemode.getTeamCount());
        session.setTeams(teams);

        gamemode.onInit(session);
        return session;
    }

    public void addPlayerToSession(GameSession session, Player player, Animal animal, Team team) {
        if (session == null || player == null) return;
        session.addPlayer(player, animal, team);
        playerSession.put(player.getUniqueId(), session);
        Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(player, session, team));
    }

    public void removePlayerFromSession(GameSession session, Player player) {
        if (session == null || player == null) return;
        Team team = session.getTeamOfPlayer(player);
        session.removePlayer(player);
        playerSession.remove(player.getUniqueId());
        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent(player, session, team));
    }

    public void startSession(GameSession session) {
        if (session == null || session.getState() == GameState.PLAYING) return;
        session.setState(GameState.STARTING);
        session.getGamemode().onStart(session);
        session.setState(GameState.PLAYING);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(session.getGamemode().getId()));
        startTicker(session);
    }

    private void startTicker(GameSession session) {
        if (plugin == null) return;
        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (session.getState() != GameState.PLAYING) return;
            session.incrementTick();
            session.getGamemode().onTick(session, session.getTick());
            Bukkit.getPluginManager().callEvent(new GameTickEvent(session, session.getTick()));

            WinResult result = session.getGamemode().checkWinCondition(session);
            if (result != null) {
                endSession(session, result);
            } else if (session.getDurationTicks() > 0 && session.getRemainingTicks() <= 0) {
                endSession(session, defaultWinner(session));
            }
        }, 1L, 1L);
        sessionTasks.put(session.getId(), task);
    }

    public void endSession(GameSession session, WinResult result) {
        if (session == null || session.getState() == GameState.ENDED) return;
        session.setState(GameState.ENDING);
        session.getGamemode().onEnd(session, result);

        BukkitTask task = sessionTasks.remove(session.getId());
        if (task != null) task.cancel();

        Player winner = result.getWinningPlayer();
        List<Player> players = session.getPlayers();
        Bukkit.getPluginManager().callEvent(
                new GameEndEvent(session.getGamemode().getId(), winner, result.getWinningTeam(), players));

        session.setState(GameState.ENDED);
        TeamManager.getInstance().removeSession(session.getId());
        for (Player p : players) {
            playerSession.remove(p.getUniqueId());
        }
        sessions.remove(session.getId());
    }

    /** Default winner when time runs out: highest-score team (or its member for FFA). */
    private WinResult defaultWinner(GameSession session) {
        Team best = null;
        int bestScore = Integer.MIN_VALUE;
        for (Team team : session.getTeams()) {
            if (team.getScore() > bestScore) {
                bestScore = team.getScore();
                best = team;
            }
        }
        if (best == null) best = session.getTeams().isEmpty() ? null : session.getTeams().get(0);
        if (best == null) return WinResult.playerWin(null, "no_contest");
        if (session.getGamemode().getTeamSize() == 1 && !best.getMembers().isEmpty()) {
            Player p = Bukkit.getPlayer(best.getMembers().get(0));
            return WinResult.playerWin(p, "time_up");
        }
        return WinResult.teamWin(best, "time_up");
    }

    // ---- Combat / stats ----

    public void recordKill(GameSession session, Player killer, Player victim) {
        PlayerManager pm = PlayerManager.getInstance();
        PlayerStats killerStats = pm.getPlayerStats(killer);
        PlayerStats victimStats = pm.getPlayerStats(victim);
        if (killerStats != null) killerStats.addKill();
        if (victimStats != null) victimStats.addDeath();

        if (session != null && victim != null) {
            session.markDead(victim);
        }

        Bukkit.getPluginManager().callEvent(new PlayerKillEvent(
                killer, victim, session != null ? session.getGamemode().getId() : "unknown",
                killerStats != null ? killerStats.getKills() : 0));
    }

    // ---- Lookups ----

    public GameSession getSession(String id) {
        return sessions.get(id);
    }

    public GameSession getSessionOfPlayer(Player player) {
        return playerSession.get(player.getUniqueId());
    }

    public Collection<GameSession> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    public void clearAll() {
        for (BukkitTask task : sessionTasks.values()) {
            if (task != null) task.cancel();
        }
        sessionTasks.clear();
        sessions.clear();
        playerSession.clear();
        TeamManager.getInstance().clearAll();
    }
}
