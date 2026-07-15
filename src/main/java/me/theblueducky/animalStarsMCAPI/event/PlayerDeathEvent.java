package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player dies during a game (health reached 0 or eliminated).
 */
public class PlayerDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player victim;
    private final Player killer;
    private final GameSession session;

    public PlayerDeathEvent(Player victim, Player killer, GameSession session) {
        this.victim = victim;
        this.killer = killer;
        this.session = session;
    }

    public Player getVictim() {
        return victim;
    }

    public Player getKiller() {
        return killer;
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
