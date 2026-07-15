package me.theblueducky.animalStarsMCAPI.game;

/**
 * The type of objective a gamemode uses to determine a winner.
 */
public enum GameObjectiveType {
    /** A team/player is eliminated when all members are dead (e.g. Knockout). */
    ELIMINATION,
    /** Collect/hold a resource to a threshold (e.g. Gem Grab). */
    COLLECT,
    /** Last player/team standing wins (e.g. Showdown). */
    LAST_STANDING,
    /** Score the most points in the time limit (e.g. Brawl Ball, Heist). */
    SCORE
}
