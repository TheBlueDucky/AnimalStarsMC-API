package me.theblueducky.animalStarsMCAPI.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent helper for building side-bar scoreboards (HUD) showing timers,
 * scores, team status, and animal info during a match.
 */
public class ScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<String> lines;

    public ScoreboardBuilder(String title) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("asmc_hud", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(title);
        this.lines = new ArrayList<>();
    }

    public ScoreboardBuilder setTitle(String title) {
        objective.setDisplayName(title);
        return this;
    }

    public ScoreboardBuilder addLine(String text) {
        lines.add(text);
        return this;
    }

    public ScoreboardBuilder addBlank() {
        lines.add("");
        return this;
    }

    /** Build the scoreboard. Duplicate lines are de-duplicated with trailing spaces. */
    public Scoreboard build() {
        // Clear any previous entries
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        int score = lines.size();
        for (String line : lines) {
            String entry = makeUnique(line);
            objective.getScore(entry).setScore(score);
            score--;
        }
        return scoreboard;
    }

    /** Build and apply to a player. */
    public void send(Player player) {
        player.setScoreboard(build());
    }

    /** Reset a player's scoreboard to the server default. */
    public static void clear(Player player) {
        if (Bukkit.getScoreboardManager() != null) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    private String makeUnique(String line) {
        String base = ChatColor.translateAlternateColorCodes('&', line);
        if (base.length() > 40) base = base.substring(0, 40);
        // Scoreboard entries must be unique; pad duplicates with spaces.
        String candidate = base;
        while (scoreboard.getEntries().contains(candidate)) {
            candidate = candidate + " ";
            if (candidate.length() > 40) candidate = base + " " + System.nanoTime() % 1000;
        }
        return candidate;
    }
}
