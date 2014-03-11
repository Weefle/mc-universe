package com.octopod.network;

import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.commands.*;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.EventManager;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.listener.BukkitListeners;
import com.octopod.network.listener.DebugListener;
import com.octopod.network.listener.LilyPadListeners;
import com.octopod.network.listener.NetworkListener;
import com.octopod.network.modules.signs.SignPlugin;
import com.octopod.network.util.BukkitUtils;
import com.octopod.network.util.RequestUtils;
import com.octopod.octolib.common.StringUtils;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworkPlugin extends JavaPlugin {

	//TODO: Add friends list system
	//TODO: > be able to join the server a friend is on

    //Used before most messages
	public final static String PREFIX = "&8[&6Net+&8] &f";

    public static NetworkPlugin self;

    protected boolean connected = false;
	private Connect connect;

	LilyPadListeners lilyPadListeners = null;
	BukkitListeners bukkitListeners = null;
	NetworkListener messageListener = null;
	DebugListener debugListener = null;

    /**
     * Gets all the players on the network as a Set.
     * @return The Set containing all the players, or an empty Set if the request somehow fails.
     */
    public Set<String> getNetworkedPlayers() {
        GetPlayersResult result = (GetPlayersResult)RequestUtils.request(new GetPlayersRequest(true));
        if(result.getStatusCode() == StatusCode.SUCCESS) {
            return result.getPlayers();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Gets if the player is online (on the entire network)
     * @param player The name of the player.
     * @return If the player is online.
     */
    public boolean isPlayerOnline(String player) {
        return getNetworkedPlayers().contains(player);
    }

    /**
     * Gets if the server with this username is online.
     * @param server The username of the server.
     * @return If the server is online.
     */
    public boolean isServerOnline(String server) {
        return RequestUtils.sendMessage(server, "", "").getStatusCode() == StatusCode.SUCCESS;
    }

    public boolean sendPlayer(String player, String server) {
        return RequestUtils.request(new RedirectRequest(server, player)).getStatusCode() == StatusCode.SUCCESS;
    }

    /**
     * Tells a server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public void broadcastNetworkMessage(String server, String message) {
        RequestUtils.sendMessage(server, NetworkConfig.CHANNEL_BROADCAST, message);
    }

    /**
     * Tells every server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public void broadcastNetworkMessage(String message) {
        RequestUtils.broadcastMessage(NetworkConfig.CHANNEL_BROADCAST, message);
    }

    /**
     * Sends a raw message to a player. Works cross-server.
     * @param player The name of the player.
     * @param message The message to send.
     */
    public void sendNetworkMessage(String player, String message) {
        if(BukkitUtils.isPlayerOnline(player)) {
            BukkitUtils.sendMessage(player, message);
        } else {
            RequestUtils.broadcastMessage(NetworkConfig.CHANNEL_MESSAGE, encodeString(player, message));
        }
    }

    /**
     * Broadcasts a message to all servers telling them to send back their info.
     * This method should only be called only when absolutely needed, as the info returned never changes.
     * This might cause messages to be recieved on the CHANNEL_INFO_RESPONSE channel.
     */
    public void requestServerInfo() {
        NetworkDebug.verbose("Requesting info from all servers");
        RequestUtils.broadcastMessage(NetworkConfig.CHANNEL_INFO_REQUEST, encodeServerInfo());
    }

    /**
     * Broadcasts a message to a list of servers telling them to send back their info.
     * This method should only be called only when absolutely needed, as the info returned never changes.
     * This might cause messages to be recieved on the CHANNEL_INFO_RESPONSE channel.
     * @param servers The list of servers to message.
     */
    public void requestServerInfo(List<String> servers) {
        NetworkDebug.verbose("Requesting info from: &a" + servers);
        RequestUtils.sendMessage(servers, NetworkConfig.CHANNEL_INFO_REQUEST, encodeServerInfo());
    }

    /**
     * Broadcasts a message to a list of servers telling them to send back a list of their players.
     * This method should only be called only when absolutely needed, as the PlayerCache should automatically change it.
     * This might cause messages to be recieved on the CHANNEL_PLAYERLIST_RESPONSE channel.
     */
    public void requestPlayerList() {
        NetworkDebug.verbose("Requesting playerlist from all servers");
        RequestUtils.broadcastMessage(NetworkConfig.CHANNEL_PLAYERLIST_REQUEST, encodePlayerList());
    }

    /**
     * Returns if LilyPad is connected or not.
     * @return true, if Lilypad is connected.
     */
    public boolean isLilypadConnected() {
        return connected;
    }

    /**
     * Returns the LilyPad connection.
     * @return The LilyPad connection.
     */
    public Connect getConnection() {
        return connect;
    }
    /**
     * Gets this plugin's event manager, which is used to register custom events.
     * @return Network's EventManager.
     */
    public EventManager getEventManager() {
        return EventManager.getManager();
    }

    /**
     * Gets this plugin's username on LilyPad.
     * @return This plugin's username.
     */
    public String getUsername() {
        return connect.getSettings().getUsername();
    }

    public boolean isTestBuild() {
        return getPluginVersion().equals("TEST_BUILD");
    }

    public String getPluginVersion() {
        return self.getDescription().getVersion();
    }

    public int getBuildNumber() {
        return getBuildNumber(getPluginVersion());
    }

    public int getBuildNumber(String version) {
        try {
            return Integer.valueOf(version.split("-")[0]);
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
    }

    /**
     * @param args
     * @return
     */
    public String encodeString(Object... args) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            String argument = args[i].toString();
            if(argument.contains(" ")) {
                sb.append("\"").append(argument.replace("\"", "\\\"")).append("\"");
            } else {
                sb.append(argument);
            }
            if(i != args.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * The ServerInfo object.
     * IF YOU EVER CHANGE THIS PROTOCOL, ADD IT TO THE END, DON'T MIX IT UP
     * Protocol:
     *  - Username
     *  - Server name (or username)
     *  - Description (or MOTD)
     *  - Max players
     *  - Whitelisted Players, or nothing if whitelist is off. (space separated)
     */
    public static class ServerInfo {

        //Tries to get a String from the index, and returns "" if it doesn't exist.
        private String getIndex(int n) {
            try {
                return arguments.get(n);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }

        //Tries to get an integer from the screen, but returns 0 if it doesn't
        private Integer getInt(String n, int def) {
            try {
                return Integer.valueOf(n);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        private List<String> arguments;

        public ServerInfo(String encodedString) {arguments = StringUtils.parseArgs(encodedString);}

        public String   getUsername()           {return getIndex(0);}
        public String   getServerName()         {return getIndex(1);}
        public String   getDescription()        {return getIndex(2);}
        public Integer  getMaxPlayers()         {return getInt(getIndex(3), 0);}
        public String[] getWhitelistedPlayers() {return getIndex(4).equals("") ? new String[0] : getIndex(4).split(" ");}
        public Integer  getHubPriority()        {return getInt(getIndex(5), -1);}
        public String   getPluginVersion()      {return getIndex(6);}

    }

    /**
     * Encodes this server's information into a message that can be sent across servers.
     * When recieving this message, parse it using my StringUtils class.
     * See ServerInfo class above for needed protocol.
     * @return This server's information put into a string.
     */
    public String encodeServerInfo() {
        return encodeString(
            getUsername(), //Server's username
            NetworkConfig.getServerName(), //Server's config name
            getServer().getMotd(), //Server's MOTD
            getServer().getMaxPlayers(), //Server's max players
            StringUtils.implode(BukkitUtils.getWhitelistedPlayerNames(), " "), //Server's whitelisted players
            NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1, //Server's hub priority, or -1 if is not a hub.
            getPluginVersion() //Server's plugin version. (<build>-<commit>)
        );
    }

    /**
     * Encodes the players online on this server into a message that can be sent across servers.
     * When recieving this message, just split on ",".
     * @return The players online put into a string.
     */
    public String encodePlayerList() {
        return encodeString(BukkitUtils.getPlayerNames());
    }

    /**
     * Requests server information and playerlists from connected servers.
     */
    public void scan() {
        requestServerInfo();
        requestPlayerList();
    }





    /**
     * Reloads all the listeners, caches, and configurations of this plugin.
     */
	public void reload() {
        NetworkPlugin plugin = NetworkPlugin.self;
        plugin.disable();
        plugin.enable(false);
    }

    private void disable() {

        NetworkServerCache.reset();
        NetworkCommandCache.reset();
        NetworkPlayerCache.reset();

        if(lilyPadListeners != null)
            connect.unregisterEvents(lilyPadListeners);

        getEventManager().unregisterAll();

        RequestUtils.broadcastMessage(NetworkConfig.CHANNEL_UNCACHE, getUsername());

    }

    private void enable(boolean startup) {

        connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        self = this;

        //Register all the listeners
        messageListener = new NetworkListener();
        lilyPadListeners = new LilyPadListeners();
        bukkitListeners = new BukkitListeners();
        debugListener = new DebugListener();

        getEventManager().registerListener(messageListener);
        getEventManager().registerListener(debugListener);
        connect.registerEvents(lilyPadListeners);
        Bukkit.getPluginManager().registerEvents(bukkitListeners, this);

        //Configuration loading
        NetworkConfig.reloadConfig();

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
                                NetworkDebug.info("&cLilypad could not connect!");
                                Bukkit.getPluginManager().disablePlugin(NetworkPlugin.this);
                                return;
                            }
                        }
                        NetworkPlugin.this.connected = true;
                        EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
                    }

                }).start();
            } else {
                this.connected = connect.isConnected();
                if(isLilypadConnected()) {
                    EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
                }
            }
        }

        if(isTestBuild()) {
            NetworkDebug.info("You are running a test build of &fNetworkPlus&7!");
        } else {
            NetworkDebug.info("Successfully loaded &fNetworkPlus&7 &6" + getPluginVersion());
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
