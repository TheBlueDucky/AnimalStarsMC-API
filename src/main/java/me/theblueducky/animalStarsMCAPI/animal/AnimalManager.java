package me.theblueducky.animalStarsMCAPI.animal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of all available Animals. Concrete animals are registered by the
 * core/gamemode plugins at startup.
 */
public class AnimalManager {

    private static AnimalManager instance;
    private final Map<String, Animal> animals;

    private AnimalManager() {
        this.animals = new HashMap<>();
    }

    public static AnimalManager getInstance() {
        if (instance == null) {
            instance = new AnimalManager();
        }
        return instance;
    }

    public void registerAnimal(Animal animal) {
        animals.put(animal.getId().toLowerCase(), animal);
    }

    public Animal getAnimal(String id) {
        return animals.get(id.toLowerCase());
    }

    public boolean isRegistered(String id) {
        return animals.containsKey(id.toLowerCase());
    }

    public Collection<Animal> getAllAnimals() {
        return new ArrayList<>(animals.values());
    }

    public List<String> getAnimalIds() {
        return new ArrayList<>(animals.keySet());
    }

    public void clearAll() {
        animals.clear();
    }
}
