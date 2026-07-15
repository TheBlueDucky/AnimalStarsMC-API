package me.theblueducky.animalStarsMCAPI.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.List;

public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String gameMode;
    private Player winner;
    private List<Player> allPlayers;

    public GameEndEvent(String gameMode, Player winner, List<Player> allPlayers) {
        this.gameMode = gameMode;
        this.winner = winner;
        this.allPlayers = allPlayers;
    }

    public String getGameMode() {
        return gameMode;
    }

    public Player getWinner() {
        return winner;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}