package com.octopod.network.util;

import com.octopod.network.NetworkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class BukkitUtils {


    public static void sendMessage(String player, String message) {
        Player p = Bukkit.getPlayer(player);
        if(p != null) {sendMessage(p, message);}
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static Player[] getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static List<String> getPlayerNames() {
        List<String> players = new ArrayList<String>();
        for(Player p: Bukkit.getOnlinePlayers()) {
            players.add(p.getName());
        }
        return players;
    }

    public static boolean isPlayerOnline(String player) {
        return Bukkit.getPlayer(player) != null;
    }

    public static void console(String message) {
        console(message, NetworkPlugin.PREFIX);
    }

    public static void console(String message, String prefix) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

}
