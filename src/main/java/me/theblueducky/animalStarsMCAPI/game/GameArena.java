package me.theblueducky.animalStarsMCAPI.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * An arena (map) for a game session.
 * Supports per-team spawns, a world reference, a cuboid play-area bound,
 * and YAML serialization via Bukkit's ConfigurationSerializable-friendly API.
 */
public class GameArena {

    private String arenaName;
    private String worldName;
    private final Map<Integer, Location> teamSpawns;
    private Location boundMin;
    private Location boundMax;
    private int maxPlayers;
    private int minPlayers;
    private Location lobbySpawn;

    public GameArena(String arenaName, int maxPlayers, int minPlayers) {
        this.arenaName = arenaName;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.teamSpawns = new HashMap<>();
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public World getWorld() {
        return worldName == null ? null : Bukkit.getWorld(worldName);
    }

    public void setTeamSpawn(int teamId, Location location) {
        teamSpawns.put(teamId, location);
    }

    public Location getTeamSpawn(int teamId) {
        return teamSpawns.get(teamId);
    }

    public Map<Integer, Location> getTeamSpawns() {
        return new HashMap<>(teamSpawns);
    }

    public void setBounds(Location min, Location max) {
        this.boundMin = min;
        this.boundMax = max;
    }

    public Location getBoundMin() {
        return boundMin;
    }

    public Location getBoundMax() {
        return boundMax;
    }

    public boolean isWithinBounds(Location location) {
        if (boundMin == null || boundMax == null) return true; // no bounds = unrestricted
        if (location.getWorld() == null) return false;
        if (boundMin.getWorld() != null && !location.getWorld().getName().equals(boundMin.getWorld().getName())) {
            return false;
        }
        return location.getX() >= boundMin.getX() && location.getX() <= boundMax.getX()
                && location.getY() >= boundMin.getY() && location.getY() <= boundMax.getY()
                && location.getZ() >= boundMin.getZ() && location.getZ() <= boundMax.getZ();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    /** True when all team spawns are set and bounds are defined. */
    public boolean isFullyConfigured(int teamCount) {
        if (boundMin == null || boundMax == null) return false;
        for (int i = 0; i < teamCount; i++) {
            if (!teamSpawns.containsKey(i)) return false;
        }
        return true;
    }

    // ---- YAML serialization ----

    public void serialize(ConfigurationSection section) {
        section.set("name", arenaName);
        section.set("world", worldName);
        section.set("maxPlayers", maxPlayers);
        section.set("minPlayers", minPlayers);
        section.set("lobbySpawn", lobbySpawn);
        section.set("boundMin", boundMin);
        section.set("boundMax", boundMax);
        ConfigurationSection spawns = section.createSection("teamSpawns");
        for (Map.Entry<Integer, Location> entry : teamSpawns.entrySet()) {
            spawns.set(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    public static GameArena deserialize(ConfigurationSection section) {
        String name = section.getString("name", "arena");
        int max = section.getInt("maxPlayers", 8);
        int min = section.getInt("minPlayers", 1);
        GameArena arena = new GameArena(name, max, min);
        arena.setWorldName(section.getString("world"));
        arena.setLobbySpawn(section.getLocation("lobbySpawn"));
        arena.setBounds(section.getLocation("boundMin"), section.getLocation("boundMax"));
        ConfigurationSection spawns = section.getConfigurationSection("teamSpawns");
        if (spawns != null) {
            for (String key : spawns.getKeys(false)) {
                try {
                    int teamId = Integer.parseInt(key);
                    Location loc = spawns.getLocation(key);
                    if (loc != null) arena.setTeamSpawn(teamId, loc);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return arena;
    }
}
