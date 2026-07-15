package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a match is formed from the queue and a GameSession is started.
 */
public class MatchFoundEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final GameSession session;

    public MatchFoundEvent(GameSession session) {
        this.session = session;
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
