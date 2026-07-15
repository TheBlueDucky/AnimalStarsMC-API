package me.theblueducky.animalStarsMCAPI;

import org.bukkit.plugin.java.JavaPlugin;
import me.theblueducky.animalStarsMCAPI.data.DataManager;
import me.theblueducky.animalStarsMCAPI.game.GameArena;
import me.theblueducky.animalStarsMCAPI.game.GameManager;
import me.theblueducky.animalStarsMCAPI.player.PlayerManager;
import me.theblueducky.animalStarsMCAPI.utils.Messages;

import java.util.Map;

public class AnimalStarsMCAPI extends JavaPlugin {
    private static AnimalStarsMCAPI instance;
    private GameManager gameManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;
        gameManager = GameManager.getInstance();
        gameManager.init(this);
        playerManager = PlayerManager.getInstance();

        DataManager.getInstance().init(this);
        // Load persisted data
        playerManager.loadAll(DataManager.getInstance().loadPlayerStats());
        for (Map.Entry<String, GameArena> entry : DataManager.getInstance().loadArenas().entrySet()) {
            gameManager.registerArena(entry.getKey(), entry.getValue());
        }

        getLogger().info(Messages.success("AnimalStarsMC API loaded successfully!"));
        getLogger().info("Framework ready for gamemodes to connect");
    }

    @Override
    public void onDisable() {
        // Persist data
        DataManager.getInstance().savePlayerStats(playerManager.getAllStats());
        DataManager.getInstance().saveArenas(gameManager.getArenas());

        if (gameManager != null) {
            gameManager.clearAll();
        }
        if (playerManager != null) {
            playerManager.clearAll();
        }
        getLogger().info(Messages.error("AnimalStarsMC API disabled!"));
    }

    public static AnimalStarsMCAPI getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
