package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final Player victim;
    private final String gameMode;
    private final int killCount;
    private final GameSession session;

    public PlayerKillEvent(Player killer, Player victim, String gameMode, int killCount, GameSession session) {
        this.killer = killer;
        this.victim = victim;
        this.gameMode = gameMode;
        this.killCount = killCount;
        this.session = session;
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

    public GameSession getSession() {
        return session;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
