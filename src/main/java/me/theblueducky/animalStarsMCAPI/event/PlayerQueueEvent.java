package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.Lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player joins the queue/lobby for a gamemode.
 */
public class PlayerQueueEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Lobby lobby;

    public PlayerQueueEvent(Player player, Lobby lobby) {
        this.player = player;
        this.lobby = lobby;
    }

    public Player getPlayer() {
        return player;
    }

    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
