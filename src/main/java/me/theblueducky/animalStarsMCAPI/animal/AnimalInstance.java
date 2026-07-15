package me.theblueducky.animalStarsMCAPI.animal;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-player in-game state of an Animal: current health, attack ammo,
 * super charge, equipped gadget/star power and cooldowns.
 */
public class AnimalInstance {

    private final Animal animal;
    private final Player owner;

    private double health;
    private double maxHealth;
    private int ammo;
    private final int maxAmmo;
    private int superCharge;
    private final int superChargeMax;

    private boolean alive;
    private StarPower starPower;
    private Gadget gadget;

    // gadgetId -> system millis when the gadget becomes ready again
    private final Map<String, Long> gadgetCooldowns;

    public AnimalInstance(Animal animal, Player owner) {
        this.animal = animal;
        this.owner = owner;
        this.maxHealth = animal.getMaxHealth();
        this.health = maxHealth;
        this.maxAmmo = animal.getMaxAmmo();
        this.ammo = maxAmmo;
        this.superChargeMax = animal.getSuperChargeMax();
        this.superCharge = 0;
        this.alive = true;
        this.gadgetCooldowns = new HashMap<>();
    }

    public Animal getAnimal() {
        return animal;
    }

    public Player getOwner() {
        return owner;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    /** Apply damage. Returns the amount actually dealt. */
    public double damage(double amount) {
        if (!alive) return 0;
        double dealt = Math.min(health, amount);
        health -= dealt;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
        return dealt;
    }

    public void heal(double amount) {
        if (!alive) return;
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        if (!alive) this.health = 0;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public double getAmmoRatio() {
        return maxAmmo == 0 ? 0 : (double) ammo / maxAmmo;
    }

    /** Consume one ammo if available (used by performAttack). */
    public boolean consumeAmmo() {
        if (ammo <= 0) return false;
        ammo--;
        return true;
    }

    /** Regenerate one ammo (call on a tick interval). */
    public void regenerateAmmo() {
        if (ammo < maxAmmo) ammo++;
    }

    public int getSuperCharge() {
        return superCharge;
    }

    public int getSuperChargeMax() {
        return superChargeMax;
    }

    public double getSuperChargeRatio() {
        return superChargeMax == 0 ? 0 : (double) superCharge / superChargeMax;
    }

    public void addSuperCharge(int amount) {
        superCharge = Math.min(superChargeMax, superCharge + amount);
    }

    public boolean canUseSuper() {
        return alive && superCharge >= superChargeMax;
    }

    /** Trigger the animal's super ability (resets charge). */
    public void useSuper() {
        if (!canUseSuper()) return;
        animal.performSuper(owner, this);
        superCharge = 0;
    }

    /** Trigger the animal's basic attack (consumes ammo). */
    public void attack() {
        if (!alive) return;
        if (!consumeAmmo()) return;
        animal.performAttack(owner, this);
    }

    public StarPower getStarPower() {
        return starPower;
    }

    public void setStarPower(StarPower starPower) {
        this.starPower = starPower;
        if (starPower != null) {
            starPower.onEquip(this);
        }
    }

    public Gadget getGadget() {
        return gadget;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public boolean isGadgetReady() {
        if (gadget == null) return false;
        Long ready = gadgetCooldowns.get(gadget.getId());
        return ready == null || System.currentTimeMillis() >= ready;
    }

    public long getGadgetCooldownRemainingMillis() {
        if (gadget == null) return 0;
        Long ready = gadgetCooldowns.get(gadget.getId());
        if (ready == null) return 0;
        return Math.max(0, ready - System.currentTimeMillis());
    }

    /** Activate the equipped gadget if off cooldown. */
    public void useGadget() {
        if (gadget == null || !isGadgetReady() || !alive) return;
        gadget.onActivate(owner, this);
        gadgetCooldowns.put(gadget.getId(), System.currentTimeMillis() + gadget.getCooldownMillis());
    }
}
