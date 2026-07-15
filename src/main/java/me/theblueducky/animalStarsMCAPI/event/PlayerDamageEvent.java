package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player takes damage during a game. Cancellable so core/gamemode
 * plugins can intercept or modify damage.
 */
public class PlayerDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player victim;
    private final Player attacker;
    private final GameSession session;
    private double amount;
    private boolean cancelled;

    public PlayerDamageEvent(Player victim, Player attacker, GameSession session, double amount) {
        this.victim = victim;
        this.attacker = attacker;
        this.session = session;
        this.amount = amount;
    }

    public Player getVictim() {
        return victim;
    }

    public Player getAttacker() {
        return attacker;
    }

    public GameSession getSession() {
        return session;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
