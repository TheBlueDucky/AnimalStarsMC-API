package me.theblueducky.animalStarsMCAPI.data;

import me.theblueducky.animalStarsMCAPI.game.GameArena;
import me.theblueducky.animalStarsMCAPI.player.PlayerStats;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Flatfile (YAML) persistence for player stats and arena configs.
 * Files live under the plugin's data folder: data/players.yml and data/arenas.yml.
 */
public class DataManager {

    private static DataManager instance;
    private JavaPlugin plugin;

    private File playersFile;
    private File arenasFile;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void init(JavaPlugin plugin) {
        this.plugin = plugin;
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) dataDir.mkdirs();
        playersFile = new File(dataDir, "players.yml");
        arenasFile = new File(dataDir, "arenas.yml");
    }

    // ---- Player stats ----

    public void savePlayerStats(Map<UUID, PlayerStats> stats) {
        if (plugin == null) return;
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerStats> entry : stats.entrySet()) {
            entry.getValue().serialize(config.createSection(entry.getKey().toString()));
        }
        try {
            config.save(playersFile);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save player stats: " + e.getMessage());
        }
    }

    public Map<UUID, PlayerStats> loadPlayerStats() {
        Map<UUID, PlayerStats> result = new HashMap<>();
        if (plugin == null || !playersFile.exists()) return result;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playersFile);
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                result.put(uuid, PlayerStats.deserialize(uuid, config.getConfigurationSection(key)));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return result;
    }

    // ---- Arenas ----

    public void saveArenas(Collection<GameArena> arenas) {
        if (plugin == null) return;
        YamlConfiguration config = new YamlConfiguration();
        for (GameArena arena : arenas) {
            arena.serialize(config.createSection(arena.getArenaName()));
        }
        try {
            config.save(arenasFile);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save arenas: " + e.getMessage());
        }
    }

    public Map<String, GameArena> loadArenas() {
        Map<String, GameArena> result = new HashMap<>();
        if (plugin == null || !arenasFile.exists()) return result;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(arenasFile);
        for (String key : config.getKeys(false)) {
            GameArena arena = GameArena.deserialize(config.getConfigurationSection(key));
            if (arena != null) result.put(key.toLowerCase(), arena);
        }
        return result;
    }

    /** Reload arenas from file (useful for hot-reloading arena configs). */
    public Map<String, GameArena> reload() {
        return loadArenas();
    }
}
