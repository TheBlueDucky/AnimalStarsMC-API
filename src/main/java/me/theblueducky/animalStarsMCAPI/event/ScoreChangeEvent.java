package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;
import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a team's objective score changes (gems collected, goals scored, etc.).
 */
public class ScoreChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Team team;
    private final GameSession session;
    private final int oldScore;
    private final int newScore;

    public ScoreChangeEvent(Team team, GameSession session, int oldScore, int newScore) {
        this.team = team;
        this.session = session;
        this.oldScore = oldScore;
        this.newScore = newScore;
    }

    public Team getTeam() {
        return team;
    }

    public GameSession getSession() {
        return session;
    }

    public int getOldScore() {
        return oldScore;
    }

    public int getNewScore() {
        return newScore;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
