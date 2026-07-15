package me.theblueducky.animalStarsMCAPI.game;

import me.theblueducky.animalStarsMCAPI.team.Team;

import org.bukkit.entity.Player;

/**
 * Describes the outcome of a finished game.
 * For team modes, {@link #getWinningTeam()} is set; for FFA modes,
 * {@link #getWinningPlayer()} is set.
 */
public class WinResult {

    private final Team winningTeam;
    private final Player winningPlayer;
    private final String reason;

    private WinResult(Team winningTeam, Player winningPlayer, String reason) {
        this.winningTeam = winningTeam;
        this.winningPlayer = winningPlayer;
        this.reason = reason;
    }

    public static WinResult teamWin(Team team, String reason) {
        return new WinResult(team, null, reason);
    }

    public static WinResult playerWin(Player player, String reason) {
        return new WinResult(null, player, reason);
    }

    public static WinResult draw(String reason) {
        return new WinResult(null, null, reason);
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public String getReason() {
        return reason;
    }

    public boolean hasTeamWinner() {
        return winningTeam != null;
    }
}
