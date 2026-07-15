package me.theblueducky.animalStarsMCAPI.player;

import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerStats {
    private UUID playerUUID;
    private String playerName;
    private int kills;
    private int deaths;
    private int wins;
    private int losses;
    private int trophies;
    private int coins;

    public PlayerStats(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.kills = 0;
        this.deaths = 0;
        this.wins = 0;
        this.losses = 0;
        this.trophies = 0;
        this.coins = 0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public void addTrophies(int amount) {
        this.trophies += amount;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getCoins() {
        return coins;
    }

    public double getKD() {
        if (deaths == 0) return kills;
        return (double) kills / deaths;
    }

    public double getWinRate() {
        int totalGames = wins + losses;
        if (totalGames == 0) return 0;
        return (double) wins / totalGames * 100;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "playerName='" + playerName + '\'' +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", wins=" + wins +
                ", losses=" + losses +
                ", trophies=" + trophies +
                ", coins=" + coins +
                '}';
    }
}