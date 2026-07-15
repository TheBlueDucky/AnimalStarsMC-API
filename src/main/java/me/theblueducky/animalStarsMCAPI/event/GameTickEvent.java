package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired every game tick for a PLAYING session.
 */
public class GameTickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final GameSession session;
    private final int tick;

    public GameTickEvent(GameSession session, int tick) {
        this.session = session;
        this.tick = tick;
    }

    public GameSession getSession() {
        return session;
    }

    public int getTick() {
        return tick;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
