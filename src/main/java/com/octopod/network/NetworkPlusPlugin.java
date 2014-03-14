package com.octopod.network;

import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.commands.*;
import com.octopod.network.connection.LilypadConnection;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.listener.BukkitListener;
import com.octopod.network.listener.NetworkListener;
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
     * The ServerInfo object holding this server's information.
     */
    private ServerInfo serverInfo;

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
	private NetworkListener networkListener = null;

    public BukkitListener getBukkitListener() {
        return bukkitListener;
    }

    public NetworkListener getNetworkListener() {
        return networkListener;
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

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * Requests server information and playerlists from connected servers.
     */
    public void scan() {
        NetworkPlus.getInstance().requestServerInfo();
        NetworkPlus.getInstance().requestPlayerList();
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
        plugin.disable();
        plugin.enable(false);
    }

    private void disable() {

        NetworkServerCache.reset();
        NetworkCommandCache.reset();
        NetworkPlayerCache.reset();

        NetworkPlus.getEventManager().unregisterAll();
        NetworkPlus.getInstance().requestUncache(getUsername());

    }

    private void enable(boolean startup) {

        instance = this;

        new NetworkPlus(this);

        connection = new LilypadConnection(this);
        logger = new NetworkLogger();

        //Register all the listeners
        networkListener = new NetworkListener();
        bukkitListener = new BukkitListener();

        NetworkPlus.getEventManager().registerListener(networkListener);
        Bukkit.getPluginManager().registerEvents(bukkitListener, this);

        //Configuration loading
        NetworkConfig.reloadConfig();

        //Sets this server's ServerInfo instance
        serverInfo = new ServerInfo();

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

    }

	@Override
	public void onEnable() {
		enable(true);
	}

	@Override
	public void onDisable() {
		disable();
	}

}
