package me.theblueducky.animalStarsMCAPI.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player killer;
    private Player victim;
    private String gameMode;
    private int killCount;

    public PlayerKillEvent(Player killer, Player victim, String gameMode, int killCount) {
        this.killer = killer;
        this.victim = victim;
        this.gameMode = gameMode;
        this.killCount = killCount;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getKillCount() {
        return killCount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}