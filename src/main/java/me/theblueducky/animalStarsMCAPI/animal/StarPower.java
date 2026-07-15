package me.theblueducky.animalStarsMCAPI.animal;

/**
 * A Star Power is a passive ability tied to an Animal.
 * Concrete star powers are implemented by the core/gamemode plugins and applied
 * when the animal is equipped by a player (see {@link AnimalInstance#setStarPower(StarPower)}).
 */
public abstract class StarPower {

    private final String id;
    private final String name;
    private final String description;

    public StarPower(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    /**
     * Called once when this star power is equipped on an in-game animal instance.
     * Use it to apply passive modifiers (e.g. increase max health, faster reload).
     */
    public abstract void onEquip(AnimalInstance instance);
}
