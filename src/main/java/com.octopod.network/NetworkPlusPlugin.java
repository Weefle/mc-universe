package com.octopod.network;

import com.octopod.network.commands.*;
import com.octopod.network.connection.LilypadConnection;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.bukkit.BukkitListener;
import com.octopod.network.modules.signs.SignPlugin;
import com.octopod.network.server.ServerManager;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;
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

        ServerManager.reset();
        CommandManager.reset();

        NetworkPlus.getEventManager().unregisterAll();

		if(connection != null)
		{
			if(connection.isConnected())
			{
				ServerFlags flags = new ServerFlags();
				flags.setFlag("serverLastOnline", System.currentTimeMillis());

				NetworkPlus.broadcastServerFlags(flags);
			}
			connection.disconnect();
		}

    }

	@Override
	public void onEnable() {

        instance = this;

        new NetworkPlus(this);

        logger = new NetworkLogger();

		//Configuration loading
		try {
			NetworkConfig.load();
		} catch (Exception e) {
			NetworkPlus.getLogger().info(
					ChatColor.RED + "Something went wrong while loading Network's configuration.",
					ChatColor.RED + "This can usually happen if the plugin was loaded using unsafe methods.",
					ChatColor.RED + "The plugin will now be disabled.",
					ChatColor.RED + "If a restart doesn't fix it, report the error in the console."
			);
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}

        //Register all the listeners
        networkActions = new NetworkActions();
        bukkitListener = new BukkitListener();

        NetworkPlus.getEventManager().registerListener(networkActions);
        Bukkit.getPluginManager().registerEvents(bukkitListener, this);

        CommandManager.registerCommand(

                new CommandMaster("/net"),
                new CommandReload("/greload"),
                new CommandHub("/hub", "/core"),
                new CommandHelp("/ghelp"),
                new CommandServerList("/servers", "/glist"),
                new CommandServerConnect("/server", "/connect"),
                new CommandAlert("/alert"),
                new CommandServerPing("/ping"),
                new CommandFind("/find"),
                new CommandMessage("/msg"),
                new CommandServerSend("/send"),
                new CommandServerSendAll("/sendall"),
                new CommandScan("/gscan")

        );

        if(NetworkPlus.isTestBuild()) {
            NetworkPlus.getLogger().log(0, "You are running a test build of " + ChatColor.GOLD + "NetworkPlus" + ChatColor.GRAY + "!");
        } else {
            NetworkPlus.getLogger().log(0, "Successfully loaded " + ChatColor.GOLD + "NetworkPlus " + getPluginVersion());
        }

        new SignPlugin();

		connection = new LilypadConnection(this);
		connection.connect();

	}

}
