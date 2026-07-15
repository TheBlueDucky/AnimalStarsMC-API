package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;
import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player leaves a game session (quit, disconnect, or removed).
 */
public class PlayerLeaveGameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final GameSession session;
    private final Team team;

    public PlayerLeaveGameEvent(Player player, GameSession session, Team team) {
        this.player = player;
        this.session = session;
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public GameSession getSession() {
        return session;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
