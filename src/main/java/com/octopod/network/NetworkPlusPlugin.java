package com.octopod.network;

import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.commands.*;
import com.octopod.network.connection.LilypadConnection;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.bukkit.BukkitListener;
import com.octopod.network.modules.signs.SignPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NetworkPlusPlugin extends JavaPlugin {

	//TODO: Add friends list system
	//TODO: > be able to join the server a friend is on

    /**
     * The current instance of NetworkPlusPlugin.
     */
    private static NetworkPlusPlugin instance;

    /**
     * The current instance of NetworkLogger.
     */
    private NetworkLogger logger;

    /**
     * The current instance of the NetworkPlus connection.
     */
	private NetworkConnection connection;

    /**
     * The current instance of listeners we use for Bukkit.
     */
	private BukkitListener bukkitListener = null;

    /**
     * The current instance of system listeners we use for this plugin.
     */
	private NetworkActions networkActions = null;

    public BukkitListener getBukkitListener() {
        return bukkitListener;
    }

    public NetworkActions getNetworkActions() {
        return networkActions;
    }

    /**
     * Gets this server's current username.
     * @return This server's username.
     */
    public String getUsername() {
        return connection.getUsername();
    }

    /**
     * Gets this plugin's version name.
     * @return This plugin's version name.
     */
    public String getPluginVersion() {
        return this.getDescription().getVersion();
    }

    /**
     * Gets the current NetworkLogger instance.
     * @return The NetworkLogger instance.
     */
    public NetworkLogger logger() {return logger;}

    /**
     * Returns the LilyPad connection.
     * @return The LilyPad connection.
     */
    public NetworkConnection getConnection() {
        return connection;
    }

    /**
     * Returns if LilyPad is connected or not.
     * @return true, if Lilypad is connected.
     */
    public boolean isConnected() {
        return (getConnection() != null && getConnection().isConnected());
    }

    public void disablePlugin() {
        this.onDisable();
        Bukkit.getPluginManager().disablePlugin(this);
    }

    /**
     * Reloads all the listeners, caches, and configurations of this plugin.
     */
	public void reload() {
        NetworkPlusPlugin plugin = NetworkPlusPlugin.instance;
        plugin.onDisable();
        plugin.onEnable();
    }

    @Override
    public void onDisable() {

        connection.disconnect();

        NetworkServerCache.reset();
        NetworkCommandCache.reset();

        NetworkPlus.getEventManager().unregisterAll();
        //TODO: Patch this server's flags across the network to tell them that this server is offline.

    }

	@Override
	public void onEnable() {

        instance = this;

        new NetworkPlus(this);

        logger = new NetworkLogger();

        connection = new LilypadConnection(this);

        //Configuration loading
        NetworkConfig.reloadConfig();

        //Register all the listeners
        networkActions = new NetworkActions();
        bukkitListener = new BukkitListener();

        NetworkPlus.getEventManager().registerListener(networkActions);
        Bukkit.getPluginManager().registerEvents(bukkitListener, this);

        NetworkCommandCache.registerCommand(

                new CommandMaster       ("/net"),
                new CommandReload       ("/greload"),
                new CommandHub          ("/core"),
                new CommandHelp         ("/ghelp"),
                new CommandServerList   ("/glist"),
                new CommandServerConnect("/server"),
                new CommandAlert        ("/alert"),
                new CommandServerPing   ("/ping"),
                new CommandFind         ("/find"),
                new CommandMessage      ("/msg"),
                new CommandServerSend   ("/send"),
                new CommandServerSendAll("/sendall"),
                new CommandScan         ("/gscan")

        );

        if(NetworkPlus.isTestBuild()) {
            NetworkPlus.getLogger().info("You are running a test build of &fNetworkPlus&7!");
        } else {
            NetworkPlus.getLogger().info("Successfully loaded &fNetworkPlus&7 &6" + getPluginVersion());
        }

        new SignPlugin();

        connection.connect();

	}

}
