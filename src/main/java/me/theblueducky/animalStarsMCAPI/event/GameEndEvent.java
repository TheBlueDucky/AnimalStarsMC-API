package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.game.GameSession;
import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.List;

public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String gameMode;
    private final Player winner;
    private final Team winningTeam;
    private final List<Player> allPlayers;
    private final GameSession session;

    public GameEndEvent(String gameMode, Player winner, Team winningTeam, List<Player> allPlayers, GameSession session) {
        this.gameMode = gameMode;
        this.winner = winner;
        this.winningTeam = winningTeam;
        this.allPlayers = allPlayers;
        this.session = session;
    }

    public String getGameMode() {
        return gameMode;
    }

    public Player getWinner() {
        return winner;
    }

    /** Winning team for team modes, null for FFA. */
    public Team getWinningTeam() {
        return winningTeam;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
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
