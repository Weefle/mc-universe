package com.octopod.network;

import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.commands.*;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.listener.BukkitListeners;
import com.octopod.network.listener.DebugListener;
import com.octopod.network.listener.LilyPadListeners;
import com.octopod.network.listener.NetworkListener;
import com.octopod.network.modules.signs.SignPlugin;
import com.octopod.network.util.BukkitUtils;
import com.octopod.octolib.common.StringUtils;
import lilypad.client.connect.api.Connect;
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
     * The current instance of the LilyPad connection.
     */
	private Connect connect;

	LilyPadListeners lilyPadListeners = null;
	BukkitListeners bukkitListeners = null;
	NetworkListener messageListener = null;
	DebugListener debugListener = null;

    /**
     * Gets this server's current username.
     * @return This server's username.
     */
    public String getUsername() {
        return getConnection().getSettings().getUsername();
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
    public Connect getConnection() {
        return connect;
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

        if(lilyPadListeners != null)
            connect.unregisterEvents(lilyPadListeners);

        NetworkPlus.getEventManager().unregisterAll();
        NetworkPlus.getInstance().requestUncache(getUsername());

    }

    private void enable(boolean startup) {

        instance = this;
        new NetworkPlus(this);

        connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        logger = new NetworkLogger();

        //Register all the listeners
        messageListener = new NetworkListener();
        lilyPadListeners = new LilyPadListeners();
        bukkitListeners = new BukkitListeners();
        debugListener = new DebugListener();

        NetworkPlus.getEventManager().registerListener(messageListener);
        NetworkPlus.getEventManager().registerListener(debugListener);
        connect.registerEvents(lilyPadListeners);
        Bukkit.getPluginManager().registerEvents(bukkitListeners, this);

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

        if(connect != null) {
            if(startup) {
                new Thread(new Runnable() {

                    @Override
                    public void run()
                    {
                        if(!connect.isConnected()) {
                            BukkitUtils.console("Waiting for LilyPad to connect...");
                            int connectionAttempts = 0;

                            //Waits for LilyPad to be connected
                            while(!connect.isConnected() && connectionAttempts < 10) {
                                try {
                                    BukkitUtils.console("Waiting for LilyPad connection... " + connectionAttempts);
                                    synchronized(this) {
                                        wait(1000);
                                    }
                                } catch (InterruptedException e) {}
                                connectionAttempts++;
                            }

                            //If LilyPad still isn't connected, then most commands should be disabled.
                            if(!connect.isConnected())
                            {
                                NetworkPlus.getLogger().info("&cLilypad could not connect!");
                                Bukkit.getPluginManager().disablePlugin(NetworkPlusPlugin.this);
                                return;
                            }
                        }
                        EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
                    }

                }).start();
            } else {
                if(isConnected()) {
                    EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
                }
            }
        }

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
