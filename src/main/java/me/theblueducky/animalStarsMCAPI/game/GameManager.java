package me.theblueducky.animalStarsMCAPI.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.theblueducky.animalStarsMCAPI.event.GameStartEvent;
import me.theblueducky.animalStarsMCAPI.event.GameEndEvent;
import me.theblueducky.animalStarsMCAPI.event.PlayerKillEvent;
import me.theblueducky.animalStarsMCAPI.player.PlayerManager;
import me.theblueducky.animalStarsMCAPI.player.PlayerStats;

import java.util.*;

public class GameManager {
    private static GameManager instance;
    private Map<String, GameState> gameModes;
    private Map<String, List<Player>> gameModePlayers;
    private Map<String, GameArena> gameArenas;
    private PlayerManager playerManager;

    private GameManager() {
        this.gameModes = new HashMap<>();
        this.gameModePlayers = new HashMap<>();
        this.gameArenas = new HashMap<>();
        this.playerManager = PlayerManager.getInstance();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void registerGameMode(String gameMode) {
        gameModes.put(gameMode, GameState.WAITING);
        gameModePlayers.put(gameMode, new ArrayList<>());
    }

    public void registerArena(String gameMode, GameArena arena) {
        gameArenas.put(gameMode, arena);
    }

    public GameArena getArena(String gameMode) {
        return gameArenas.get(gameMode);
    }

    public void addPlayerToGame(String gameMode, Player player) {
        if (!gameModePlayers.containsKey(gameMode)) {
            gameModePlayers.put(gameMode, new ArrayList<>());
        }
        gameModePlayers.get(gameMode).add(player);
        playerManager.getOrCreatePlayerStats(player);
    }

    public void removePlayerFromGame(String gameMode, Player player) {
        if (gameModePlayers.containsKey(gameMode)) {
            gameModePlayers.get(gameMode).remove(player);
        }
    }

    public List<Player> getGamePlayers(String gameMode) {
        return gameModePlayers.getOrDefault(gameMode, new ArrayList<>());
    }

    public int getPlayerCount(String gameMode) {
        return gameModePlayers.getOrDefault(gameMode, new ArrayList<>()).size();
    }

    public void setGameState(String gameMode, GameState state) {
        gameModes.put(gameMode, state);

        if (state == GameState.PLAYING) {
            GameStartEvent event = new GameStartEvent(gameMode);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    public GameState getGameState(String gameMode) {
        return gameModes.getOrDefault(gameMode, GameState.WAITING);
    }

    public void endGame(String gameMode, Player winner) {
        List<Player> players = new ArrayList<>(getGamePlayers(gameMode));

        GameEndEvent event = new GameEndEvent(gameMode, winner, players);
        Bukkit.getPluginManager().callEvent(event);

        setGameState(gameMode, GameState.ENDED);

        // Clean up
        for (Player p : players) {
            removePlayerFromGame(gameMode, p);
        }
    }

    public void recordKill(String gameMode, Player killer, Player victim) {
        PlayerStats killerStats = playerManager.getPlayerStats(killer);
        PlayerStats victimStats = playerManager.getPlayerStats(victim);

        if (killerStats != null) {
            killerStats.addKill();
        }
        if (victimStats != null) {
            victimStats.addDeath();
        }

        PlayerKillEvent event = new PlayerKillEvent(
                killer,
                victim,
                gameMode,
                killerStats != null ? killerStats.getKills() : 0
        );
        Bukkit.getPluginManager().callEvent(event);
    }

    public void resetGameMode(String gameMode) {
        gameModes.put(gameMode, GameState.WAITING);
        gameModePlayers.put(gameMode, new ArrayList<>());
    }

    public void clearAll() {
        gameModes.clear();
        gameModePlayers.clear();
        gameArenas.clear();
        playerManager.clearAll();
    }
}