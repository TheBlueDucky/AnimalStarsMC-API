package me.theblueducky.animalStarsMCAPI.animal;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract playable character ("Animal") for Animal Stars.
 * Concrete animals are implemented by the core/gamemode plugins and registered
 * via {@link AnimalManager}.
 */
public abstract class Animal {

    private final String id;
    private final String name;
    private final Material icon;
    private final Rarity rarity;

    private final double maxHealth;
    private final double moveSpeed;
    private final int maxAmmo;
    private final int superChargeMax;
    private final double attackDamage;
    private final double attackRange;
    private final long attackCooldownMillis;

    private final List<Gadget> gadgets;
    private final List<StarPower> starPowers;

    protected Animal(String id, String name, Material icon, Rarity rarity,
                     double maxHealth, double moveSpeed, int maxAmmo, int superChargeMax,
                     double attackDamage, double attackRange, long attackCooldownMillis) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.rarity = rarity;
        this.maxHealth = maxHealth;
        this.moveSpeed = moveSpeed;
        this.maxAmmo = maxAmmo;
        this.superChargeMax = superChargeMax;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
        this.attackCooldownMillis = attackCooldownMillis;
        this.gadgets = new ArrayList<>();
        this.starPowers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    /** Movement speed multiplier (1.0 = normal). */
    public double getMoveSpeed() {
        return moveSpeed;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getSuperChargeMax() {
        return superChargeMax;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public long getAttackCooldownMillis() {
        return attackCooldownMillis;
    }

    public void registerGadget(Gadget gadget) {
        if (!gadgets.contains(gadget)) gadgets.add(gadget);
    }

    public List<Gadget> getGadgets() {
        return new ArrayList<>(gadgets);
    }

    public void registerStarPower(StarPower starPower) {
        if (!starPowers.contains(starPower)) starPowers.add(starPower);
    }

    public List<StarPower> getStarPowers() {
        return new ArrayList<>(starPowers);
    }

    /** Icon as an ItemStack (useful for selection GUIs). */
    public ItemStack getIconItem() {
        return new ItemStack(icon);
    }

    /** Kit items given to the player when the game starts. Override to provide a custom kit. */
    public List<ItemStack> getKitItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getIconItem());
        return items;
    }

    /** Full combat kit: animal icon + attack + super + gadget items. Used by core plugin. */
    public List<ItemStack> getFullKit() {
        List<ItemStack> items = new ArrayList<>();
        // Animal icon as identifier
        items.add(getIconItem());
        // Combat items with animal-specific appearance
        items.add(createKitItem(icon, "§cMain Attack", "§7Left-click to use main attack", "§8" + name));
        items.add(createKitItem(icon, "§6Super", "§7Right-click to use super", "§8" + name));
        items.add(createKitItem(icon, "§aGadget", "§7Drop (Q) to use gadget", "§8" + name));
        return items;
    }

    /** Helper to create a named item for the kit. */
    private ItemStack createKitItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                java.util.List<String> loreList = new java.util.ArrayList<>();
                for (String line : lore) {
                    if (line != null && !line.isEmpty()) {
                        loreList.add(line);
                    }
                }
                if (!loreList.isEmpty()) {
                    meta.setLore(loreList);
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    /** Perform the basic attack. Implemented by concrete animals. */
    public abstract void performAttack(Player user, AnimalInstance instance);

    /** Perform the super/ultimate ability. Implemented by concrete animals. */
    public abstract void performSuper(Player user, AnimalInstance instance);
}
