package me.theblueducky.animalStarsMCAPI.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private static PlayerManager instance;
    private Map<UUID, PlayerStats> playerStats;

    private PlayerManager() {
        this.playerStats = new HashMap<>();
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public PlayerStats getPlayerStats(Player player) {
        return playerStats.getOrDefault(player.getUniqueId(), null);
    }

    public PlayerStats getOrCreatePlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerStats.containsKey(uuid)) {
            playerStats.put(uuid, new PlayerStats(player));
        }
        return playerStats.get(uuid);
    }

    public void removePlayerStats(Player player) {
        playerStats.remove(player.getUniqueId());
    }

    public void resetPlayerStats(Player player) {
        playerStats.put(player.getUniqueId(), new PlayerStats(player));
    }

    public void clearAll() {
        playerStats.clear();
    }

    public int getPlayerCount() {
        return playerStats.size();
    }

    /** Bulk-load persisted stats (used on plugin enable). */
    public void loadAll(java.util.Map<java.util.UUID, PlayerStats> loaded) {
        playerStats.putAll(loaded);
    }

    /** All current stats, for persistence. */
    public java.util.Map<java.util.UUID, PlayerStats> getAllStats() {
        return new java.util.HashMap<>(playerStats);
    }
}