package me.theblueducky.animalStarsMCAPI.utils;

import org.bukkit.ChatColor;

public class Messages {
    private static final String PREFIX = ChatColor.GOLD + "[AnimalStarsMC] " + ChatColor.RESET;

    public static String format(String message) {
        return PREFIX + message;
    }

    public static String success(String message) {
        return PREFIX + ChatColor.GREEN + message + ChatColor.RESET;
    }

    public static String error(String message) {
        return PREFIX + ChatColor.RED + message + ChatColor.RESET;
    }

    public static String info(String message) {
        return PREFIX + ChatColor.BLUE + message + ChatColor.RESET;
    }

    public static String warning(String message) {
        return PREFIX + ChatColor.YELLOW + message + ChatColor.RESET;
    }

    public static String getPrefix() {
        return PREFIX;
    }
}