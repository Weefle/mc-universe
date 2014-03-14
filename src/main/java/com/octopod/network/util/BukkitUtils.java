package com.octopod.network.util;

import com.octopod.network.NetworkPlus;
import com.octopod.network.NetworkPlusPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class BukkitUtils {

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Sends a player (by name) a message.
     * It will automatically be colorized by '&'.
     * Nothing will be sent if the player is offline.
     * By default, the message will be prefixed by this plugin's prefix.
     * @param player The name of the player.
     * @param message The message to send.
     */
    public static void sendMessage(String player, String message) {
        sendMessage(player, message, "");
    }

    /**
     * Sends a player (by name) a message.
     * It will automatically be colorized by '&'.
     * Nothing will be sent if the player is offline.
     * Use prefix to prefix the message with something.
     * @param player The name of the player.
     * @param message The message to send.
     * @param prefix The prefix to put before the message.
     */
    public static void sendMessage(String player, String message, String prefix) {
        Player p = Bukkit.getPlayer(player);
        if(p != null) sendMessage(p, message, prefix);
    }

    /**

     * It will automatically be colorized by '&'.
     * Nothing will be sent if the player is offline.
     * By default, the message will be prefixed by this plugin's prefix.
     * @param sender The CommandSender.
     * @param message The message to send.
     */
    public static void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, "");
    }

    /**
     * Sends a CommandSender (by object) a message.
     * It will automatically be colorized by '&'.
     * Nothing will be sent if the player is offline.
     * Use prefix to prefix the message with something.
     * @param sender The CommandSender.
     * @param message The message to send.
     * @param prefix The prefix to put before the message.
     */
    public static void sendMessage(CommandSender sender, String message, String prefix) {
        sender.sendMessage(colorize((prefix == null ? "" : prefix) + message));
    }

    /**
     * Broadcasts a message across the server.
     * It will automatically be colorized by '&'.
     * @param message
     */
    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(colorize(message));
    }

    public static Player getPlayer(String player) {
        return Bukkit.getPlayer(player);
    }

    public static Player[] getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static String[] getPlayerNames() {
        List<String> players = new ArrayList<>();

        for(Player p: Bukkit.getOnlinePlayers())
            players.add(p.getName());

        return players.toArray(new String[players.size()]);
    }

    /**
     * Returns an array of player names that are whitelisted.
     * If the server doesn't have whitelist enabled, it will return an empty array.
     * @return
     */
    public static String[] getWhitelistedPlayerNames() {
        if(!Bukkit.hasWhitelist()) return new String[0];

        List<String> players = new ArrayList<>();

        for(OfflinePlayer p: Bukkit.getOfflinePlayers())
            players.add(p.getName());

        return players.toArray(new String[players.size()]);
    }

    public static boolean isPlayerOnline(String player) {
        return Bukkit.getPlayer(player) != null;
    }

    public static void console(String message) {
        console(message, NetworkPlus.prefix());
    }

    public static void console(String message, String prefix) {
        Bukkit.getConsoleSender().sendMessage(colorize(prefix + message));
    }

}
