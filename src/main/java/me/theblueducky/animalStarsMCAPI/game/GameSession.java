package me.theblueducky.animalStarsMCAPI.game;

import me.theblueducky.animalStarsMCAPI.animal.Animal;
import me.theblueducky.animalStarsMCAPI.animal.AnimalInstance;
import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A running instance of a gamemode: holds the arena, teams, players,
 * their in-game Animal state, the timer, and gamemode-specific objective data.
 */
public class GameSession {

    private final String id;
    private final Gamemode gamemode;
    private final GameArena arena;

    private final List<Team> teams;
    private final Map<UUID, Team> playerTeam;
    private final Map<UUID, AnimalInstance> playerAnimals;
    private final Set<UUID> alivePlayers;

    private GameState state;
    private int tick;
    private final int durationTicks; // 0 = no time limit

    // Free-form storage for gamemode objective state (e.g. gems collected per team).
    private final Map<String, Object> objectiveData;

    public GameSession(String id, Gamemode gamemode, GameArena arena, int durationTicks) {
        this.id = id;
        this.gamemode = gamemode;
        this.arena = arena;
        this.teams = new ArrayList<>();
        this.playerTeam = new HashMap<>();
        this.playerAnimals = new HashMap<>();
        this.alivePlayers = new HashSet<>();
        this.state = GameState.WAITING;
        this.tick = 0;
        this.durationTicks = durationTicks;
        this.objectiveData = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public GameArena getArena() {
        return arena;
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public void setTeams(List<Team> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
        // assign arena spawns to teams
        for (Team team : teams) {
            if (arena != null) {
                team.setSpawn(arena.getTeamSpawn(team.getTeamId()));
            }
        }
    }

    public Team getTeamOfPlayer(Player player) {
        return playerTeam.get(player.getUniqueId());
    }

    public void addPlayer(Player player, Animal animal, Team team) {
        team.addPlayer(player);
        playerTeam.put(player.getUniqueId(), team);
        playerAnimals.put(player.getUniqueId(), new AnimalInstance(animal, player));
        alivePlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        Team team = playerTeam.remove(player.getUniqueId());
        if (team != null) team.removePlayer(player);
        playerAnimals.remove(player.getUniqueId());
        alivePlayers.remove(player.getUniqueId());
    }

    public AnimalInstance getAnimalInstance(Player player) {
        return playerAnimals.get(player.getUniqueId());
    }

    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();
        for (UUID uuid : playerTeam.keySet()) {
            Player p = org.bukkit.Bukkit.getPlayer(uuid);
            if (p != null) result.add(p);
        }
        return result;
    }

    public int getPlayerCount() {
        return playerTeam.size();
    }

    public Set<UUID> getAlivePlayers() {
        return new HashSet<>(alivePlayers);
    }

    public boolean isPlayerAlive(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    public void markDead(Player player) {
        alivePlayers.remove(player.getUniqueId());
        AnimalInstance instance = playerAnimals.get(player.getUniqueId());
        if (instance != null) instance.setAlive(false);
    }

    public void markAlive(Player player) {
        alivePlayers.add(player.getUniqueId());
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getTick() {
        return tick;
    }

    public void incrementTick() {
        this.tick++;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getRemainingTicks() {
        if (durationTicks <= 0) return -1;
        return Math.max(0, durationTicks - tick);
    }

    /** Get elapsed ticks since session started (useful for gamemodes like Gem Grab). */
    public int getElapsedTicks() {
        return tick;
    }

    public void setObjectiveData(String key, Object value) {
        objectiveData.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjectiveData(String key) {
        return (T) objectiveData.get(key);
    }

    public Map<String, Object> getObjectiveDataMap() {
        return objectiveData;
    }

    /** Convenience: total alive players across all teams. */
    public int getAliveCount() {
        return alivePlayers.size();
    }
}
