package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a game session transitions to PLAYING state.
 */
public class GameStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String gameMode;
    private final GameSession session;

    public GameStartEvent(String gameMode, GameSession session) {
        this.gameMode = gameMode;
        this.session = session;
    }

    public String getGameMode() {
        return gameMode;
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
