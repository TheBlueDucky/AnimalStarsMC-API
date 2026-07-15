package me.theblueducky.animalStarsMCAPI.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameArena {
    private String arenaName;
    private Location spawn1;
    private Location spawn2;
    private Location spawn3;
    private Location spawn4;
    private int maxPlayers;
    private int minPlayers;

    public GameArena(String arenaName, int maxPlayers, int minPlayers) {
        this.arenaName = arenaName;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setSpawn(int number, Location location) {
        switch (number) {
            case 1:
                this.spawn1 = location;
                break;
            case 2:
                this.spawn2 = location;
                break;
            case 3:
                this.spawn3 = location;
                break;
            case 4:
                this.spawn4 = location;
                break;
        }
    }

    public Location getSpawn(int number) {
        switch (number) {
            case 1:
                return spawn1;
            case 2:
                return spawn2;
            case 3:
                return spawn3;
            case 4:
                return spawn4;
            default:
                return null;
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public boolean isFullyConfigured() {
        return spawn1 != null && spawn2 != null && spawn3 != null && spawn4 != null;
    }
}