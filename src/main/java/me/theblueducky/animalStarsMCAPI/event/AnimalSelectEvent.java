package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.animal.Animal;
import me.theblueducky.animalStarsMCAPI.game.GameSession;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player selects an Animal (e.g. in the pre-game lobby/queue).
 * Cancellable so a plugin can restrict animal choice.
 */
public class AnimalSelectEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Animal animal;
    private final GameSession session; // null when selecting before a match
    private boolean cancelled;

    public AnimalSelectEvent(Player player, Animal animal, GameSession session) {
        this.player = player;
        this.animal = animal;
        this.session = session;
    }

    public Player getPlayer() {
        return player;
    }

    public Animal getAnimal() {
        return animal;
    }

    public GameSession getSession() {
        return session;
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
