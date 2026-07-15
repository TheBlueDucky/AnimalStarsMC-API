package me.theblueducky.animalStarsMCAPI.game;

public enum GameState {
    WAITING,      // Waiting for players to join
    STARTING,     // Game is starting, players spawning
    PLAYING,      // Game is actively running
    ENDING,       // Game is ending
    ENDED         // Game has ended
}