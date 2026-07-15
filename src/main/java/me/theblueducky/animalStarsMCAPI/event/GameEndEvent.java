package me.theblueducky.animalStarsMCAPI.event;

import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.List;

public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String gameMode;
    private Player winner;
    private Team winningTeam;
    private List<Player> allPlayers;

    public GameEndEvent(String gameMode, Player winner, Team winningTeam, List<Player> allPlayers) {
        this.gameMode = gameMode;
        this.winner = winner;
        this.winningTeam = winningTeam;
        this.allPlayers = allPlayers;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}