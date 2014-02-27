package com.octopod.network;

import com.octopod.octolib.common.IOUtils;
import com.octopod.octolib.yaml.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetworkConfig {

	/**
	 * Returns the current config instance,
	 * or creates a new one if one doesn't exist.
	 * @return The current NetworkConfig instance, or a new instance.
	 */
	public static NetworkConfig getConfig() {
		if(config != null) {
			return config;
		} else {
			return (config = new NetworkConfig());
		}
	}

	/**
	 * Sets the current config to a new NetworkConfig instance.
	 */
	public static void reloadConfig() {
		config = new NetworkConfig();
	}

	private static NetworkConfig config;
	private static File configFile = new File("plugins/Network/config.yml");

	private Long TIMEOUT;
	private Integer DEBUG_MODE;
	private String CHANNEL_PREFIX;
    private Boolean HUB_ENABLED = false;
    private Integer HUB_PRIORITY = 0;

	/**
	 * Loads the configuration.
	 * Each created instance of NetworkConfig will load a new config.
	 */
	private NetworkConfig() {
		//TODO: load a config into the below variables

		//This is the single YAML configuration we should use.
		YamlConfiguration config = new YamlConfiguration();

		//Grabs the default configuration from our resources.
		InputStream defaultConfig = this.getClass().getClassLoader().getResourceAsStream("config.yml");

		try
		{
			if(!configFile.exists()) //Checks the existance of config.yml
			{
				//Generate a new config.yml
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();

				FileOutputStream output = new FileOutputStream(configFile);

				IOUtils.copy(defaultConfig, output);
				output.close();
			}

			//Loads the file into the configuration
			config.load(configFile);

		//Something went wrong, so use the default config.
		} catch (IOException e) {
			config.load(defaultConfig);
		}

		IOUtils.closeSilent(defaultConfig);

		TIMEOUT =           Long.valueOf(config.getInteger("request-timeout", 500));
		DEBUG_MODE =        config.getInteger("debug-messages", 0);
		CHANNEL_PREFIX =    config.getString("channel-prefix", "network");

		CHANNEL_PLAYER_JOIN = 		CHANNEL_PREFIX + ".player.join";
		CHANNEL_PLAYER_REDIRECT = 	CHANNEL_PREFIX + ".player.redirect";
		CHANNEL_PLAYER_LEAVE = 		CHANNEL_PREFIX + ".player.leave";
		CHANNEL_SENDALL = 			CHANNEL_PREFIX + ".sendall";
		CHANNEL_MESSAGE = 			CHANNEL_PREFIX + ".message";
		CHANNEL_BROADCAST = 		CHANNEL_PREFIX + ".broadcast";

		CHANNEL_INFO_RESPONSE =     CHANNEL_PREFIX + ".info";
        CHANNEL_INFO_REQUEST =      CHANNEL_PREFIX + ".info.request";

        CHANNEL_PLAYERLIST_REQUEST =    CHANNEL_PREFIX + ".playerlist.request";
        CHANNEL_PLAYERLIST_RESPONSE =   CHANNEL_PREFIX + ".playerlist.response";

		CHANNEL_CACHE = 			CHANNEL_PREFIX + ".cache.request";
		CHANNEL_CACHE_RELAY = 		CHANNEL_PREFIX + ".cache.relay";
		CHANNEL_UNCACHE = 			CHANNEL_PREFIX + ".uncache.request";
		CHANNEL_UNCACHE_RELAY =		CHANNEL_PREFIX + ".uncache.relay";

	}

    public Boolean isHub() {return HUB_ENABLED;}
    public Integer getHubPriority() {return HUB_PRIORITY;}

	public Long getRequestTimeout() {return TIMEOUT;}

	public Integer getDebugMode() {return DEBUG_MODE;}
	public void setDebugMode(int i) {DEBUG_MODE = i;}

	public String getRequestPrefix() {return CHANNEL_PREFIX;}

	//These formats use String.format()
	public String FORMAT_ALERT = 		"&8[&bAlert&8]&6 %s";
	public String FORMAT_MSG_SENDER = 	"&8[&b%s&8]&7 [me -> %s]&6 %s";
	public String FORMAT_MSG_TARGET = 	"&8[&b%s&8]&7 [%s -> me]&6 %s";

	public String
	//Used to tell a server that a player has joined/left/changed servers that the message has been sent from.
		CHANNEL_PLAYER_JOIN,
		CHANNEL_PLAYER_REDIRECT,
		CHANNEL_PLAYER_LEAVE,

	//Used to tell a server to send all players on the server to another server.
		CHANNEL_SENDALL,

	//Used to tell a server to send a player a message.
		CHANNEL_MESSAGE,

	//Used to tell a server to broadcast a message.
		CHANNEL_BROADCAST,

        CHANNEL_PLAYERLIST_REQUEST,
        CHANNEL_PLAYERLIST_RESPONSE,

	//These channels are used when requesting information from servers.
        CHANNEL_INFO_REQUEST,
        CHANNEL_INFO_RESPONSE,

	//These channels are used when requesting a server to cache a server's name.
		CHANNEL_CACHE,
		CHANNEL_CACHE_RELAY,

	//These channels are used when requestinga server to uncache a server's name.
		CHANNEL_UNCACHE,
		CHANNEL_UNCACHE_RELAY,

	//These channels are used when requesting servers to find a player.
		CHANNEL_FIND,
		CHANNEL_FIND_RELAY
	;


}
