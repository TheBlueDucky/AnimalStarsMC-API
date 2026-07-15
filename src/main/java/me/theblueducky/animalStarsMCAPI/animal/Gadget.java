package me.theblueducky.animalStarsMCAPI.animal;

import org.bukkit.entity.Player;

/**
 * A Gadget is an activatable ability tied to an Animal.
 * Concrete gadgets are implemented by the core/gamemode plugins.
 */
public abstract class Gadget {

    private final String id;
    private final String name;
    private final String description;
    private final long cooldownMillis;

    public Gadget(String id, String name, String description, long cooldownMillis) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cooldownMillis = cooldownMillis;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    /**
     * Called when the owning player activates this gadget.
     * @param user the player using the gadget
     * @param instance the in-game state of the animal the player is using
     */
    public abstract void onActivate(Player user, AnimalInstance instance);
}
