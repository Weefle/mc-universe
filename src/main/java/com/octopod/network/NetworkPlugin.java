package com.octopod.network;

import java.util.ArrayList;
import java.util.List;

import com.octopod.network.cache.PlayerCache;
import lilypad.client.connect.api.Connect;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.octopod.network.cache.CommandCache;
import com.octopod.network.cache.ServerCache;
import com.octopod.network.commands.CommandAlert;
import com.octopod.network.commands.CommandFind;
import com.octopod.network.commands.CommandHelp;
import com.octopod.network.commands.CommandMessage;
import com.octopod.network.commands.CommandServerList;
import com.octopod.network.commands.CommandMaster;
import com.octopod.network.commands.CommandServerPing;
import com.octopod.network.commands.CommandServerConnect;
import com.octopod.network.commands.CommandServerSend;
import com.octopod.network.commands.CommandServerSendAll;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.EventManager;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.listener.BukkitListeners;
import com.octopod.network.listener.DebugListener;
import com.octopod.network.listener.LilyPadListeners;
import com.octopod.network.listener.NetworkListener;

public class NetworkPlugin extends JavaPlugin {

	//TODO: Add friends list system
		//TODO: > be able to message friends across servers
		//TODO: > be able to join the server a friend is on
	
	public final static String PREFIX = "&8[&bNetwork&8] &f";
	
	public static Connect connect;
	public static JavaPlugin self;
	public static LilyPadListeners lilyPadListeners = null;
	public static BukkitListeners bukkitListeners = null;
	public static NetworkListener messageListener = null;
	public static DebugListener debugListener = null;

    public static boolean isConnected() {
        if(connect == null) {
            return false;
        } else {
            return connect.isConnected();
        }
    }
	
	public void reload() {

        disable();
        enable();

    }

    public void disable() {

        ServerCache.deleteCache();
        CommandCache.deleteCache();
        PlayerCache.deleteCache();

        if(lilyPadListeners != null)
            connect.unregisterEvents(lilyPadListeners);

        getEventManager().unregisterAll();

        LPRequestUtils.broadcastMessage(NetworkConfig.getConfig().REQUEST_UNCACHE, getServerName());

    }

    public void enable() {

        connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        self = this;

        messageListener = new NetworkListener();
        lilyPadListeners = new LilyPadListeners();
        bukkitListeners = new BukkitListeners();
        debugListener = new DebugListener();

        getEventManager().registerListener(messageListener);
        getEventManager().registerListener(debugListener);
        connect.registerEvents(lilyPadListeners);
        Bukkit.getPluginManager().registerEvents(bukkitListeners, this);

        CommandCache commandCache = CommandCache.getCache();

        commandCache.registerCommand(
                new CommandMaster(),
                new CommandHelp(),
                new CommandServerList(),
                new CommandServerConnect(),
                new CommandAlert(),
                new CommandServerPing(),
                new CommandFind(),
                new CommandMessage(),
                new CommandServerSend(),
                new CommandServerSendAll()
        );

        if(connect != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    if(!connect.isConnected()) {
                        console("Waiting for LilyPad to connect...");
                        int connectionAttempts = 1;

                        while(!connect.isConnected() && connectionAttempts <= 10) {
                            try {
                                console("Waiting for LilyPad connection... " + connectionAttempts);
                                synchronized(this) {
                                    wait(1000);
                                }
                            } catch (InterruptedException e) {}
                            connectionAttempts++;
                        }

                        if(!connect.isConnected()) {
                            console("&cGave up waiting for LilyPad to connect. Disabling plugin...");
                            Bukkit.getPluginManager().disablePlugin(NetworkPlugin.this);
                            return;
                        }
                    }

                    EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());

                }

            }).start();
        }

    }

	@Override
	public void onEnable() {
		enable();
	}
	
	@Override
	public void onDisable() {
		disable();
	}

	public static EventManager getEventManager() {
		return EventManager.getManager();
	}
	
	public static NetworkConfig getNetworkConfig() {
		return NetworkConfig.getConfig();
	}

	public static void console(String message) {
		console(message, PREFIX);
	}
	
	public static void console(String message, String prefix) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
	}

	public static String getServerName() {
		return connect.getSettings().getUsername();
	}

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

}
