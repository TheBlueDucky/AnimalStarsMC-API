package me.theblueducky.animalStarsMCAPI.game;

import me.theblueducky.animalStarsMCAPI.animal.Animal;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A waiting room for a single gamemode. Holds queued players and the Animal
 * each selected before queuing.
 */
public class Lobby {

    private final String gamemodeId;
    private final String arenaName;
    private final List<Player> players;
    private final Map<UUID, Animal> chosenAnimals;

    public Lobby(String gamemodeId, String arenaName) {
        this.gamemodeId = gamemodeId;
        this.arenaName = arenaName;
        this.players = new ArrayList<>();
        this.chosenAnimals = new HashMap<>();
    }

    public String getGamemodeId() {
        return gamemodeId;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void addPlayer(Player player, Animal animal) {
        if (!players.contains(player)) {
            players.add(player);
            chosenAnimals.put(player.getUniqueId(), animal);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        chosenAnimals.remove(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public Animal getChosenAnimal(Player player) {
        return chosenAnimals.get(player.getUniqueId());
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getSize() {
        return players.size();
    }

    public boolean isReady(int minPlayers) {
        return players.size() >= minPlayers;
    }
}
