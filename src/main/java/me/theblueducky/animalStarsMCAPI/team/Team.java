package me.theblueducky.animalStarsMCAPI.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a team within a game session.
 * Supports both FFA (teamSize 1, one player per team) and team modes (e.g. 3v3).
 */
public class Team {

    private final int teamId;
    private final String name;
    private final ChatColor color;
    private Location spawn;
    private final List<UUID> members;
    private int score;

    public Team(int teamId, String name, ChatColor color) {
        this.teamId = teamId;
        this.name = name;
        this.color = color;
        this.members = new ArrayList<>();
        this.score = 0;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    /** Name prefixed with the team color (e.g. for scoreboards / chat). */
    public String getColoredName() {
        return color + name + ChatColor.RESET;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void addPlayer(Player player) {
        if (!members.contains(player.getUniqueId())) {
            members.add(player.getUniqueId());
        }
    }

    public void removePlayer(Player player) {
        members.remove(player.getUniqueId());
    }

    public boolean hasPlayer(Player player) {
        return members.contains(player.getUniqueId());
    }

    public List<UUID> getMembers() {
        return new ArrayList<>(members);
    }

    public int getSize() {
        return members.size();
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /** True when the team has no living members (used by elimination gamemodes). */
    public boolean isEliminated(java.util.Set<UUID> alivePlayers) {
        for (UUID uuid : members) {
            if (alivePlayers.contains(uuid)) {
                return false;
            }
        }
        return members.isEmpty();
    }
}
