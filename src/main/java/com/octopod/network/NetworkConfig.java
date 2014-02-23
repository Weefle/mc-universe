package com.octopod.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.octopod.octolib.common.IOUtils;
import com.octopod.octolib.yaml.YamlConfiguration;

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
		
		Debug.info("&7Loading configuration...");
		
		try 
		{		
			if(!configFile.exists()) //Checks the existance of config.yml
			{
				//Generate a new config.yml
				Debug.info("&7Generating new configuration...");
				
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
			Debug.info("&cThe configuration could not be read into. Using defaults...");
			config.load(defaultConfig);
		}
		
		IOUtils.closeSilent(defaultConfig);
		
		Debug.info("&7Successfully loaded configuration!");
		
		TIMEOUT = Long.valueOf(config.getInt("request-timeout"));
		TIMEOUT = TIMEOUT == null ? 500L : TIMEOUT;
		
		Debug.info("&7Request Timeout: &a" + TIMEOUT);
		
		DEBUG_MODE = config.getInt("debug-messages");
		DEBUG_MODE = DEBUG_MODE == null ? 0 : DEBUG_MODE;
		
		Debug.info("&7Debug Mode: &a" + DEBUG_MODE);
		
		CHANNEL_PREFIX = config.getString("channel-prefix");
		CHANNEL_PREFIX = CHANNEL_PREFIX == null ? "network" : CHANNEL_PREFIX;
		
		Debug.info("&7Channel Prefix: &a\"" + CHANNEL_PREFIX + "\"");
		
		REQUEST_PLAYER_JOIN = 		CHANNEL_PREFIX + ".player.join";	
		REQUEST_PLAYER_REDIRECT = 	CHANNEL_PREFIX + ".player.redirect";
		REQUEST_PLAYER_LEAVE = 		CHANNEL_PREFIX + ".player.leave";	
		REQUEST_SENDALL = 			CHANNEL_PREFIX + ".sendall";
		REQUEST_MESSAGE = 			CHANNEL_PREFIX + ".message";
		REQUEST_BROADCAST = 		CHANNEL_PREFIX + ".broadcast";
		REQUEST_INFO = 				CHANNEL_PREFIX + ".info.request";
		REQUEST_INFO_RELAY = 		CHANNEL_PREFIX + ".info.relay";
		REQUEST_CACHE = 			CHANNEL_PREFIX + ".cache.request";
		REQUEST_CACHE_RELAY = 		CHANNEL_PREFIX + ".cache.relay";
		REQUEST_UNCACHE = 			CHANNEL_PREFIX + ".uncache.request";
		REQUEST_UNCACHE_RELAY =		CHANNEL_PREFIX + ".uncache.relay";
		REQUEST_FIND = 				CHANNEL_PREFIX + ".find";
		REQUEST_FIND_RELAY = 		CHANNEL_PREFIX + ".find.relay";
		
	}

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
		REQUEST_PLAYER_JOIN,	
		REQUEST_PLAYER_REDIRECT,
		REQUEST_PLAYER_LEAVE,
	
	//Used to tell a server to send all players on the server to another server.
		REQUEST_SENDALL,
		
	//Used to tell a server to send a player a message.
		REQUEST_MESSAGE,
	
	//Used to tell a server to broadcast a message.
		REQUEST_BROADCAST,

	//These channels are used when requesting information from servers.
		REQUEST_INFO,
		REQUEST_INFO_RELAY,
	
	//These channels are used when requesting a server to cache a server's name.
		REQUEST_CACHE,
		REQUEST_CACHE_RELAY,
	
	//These channels are used when requestinga server to uncache a server's name.
		REQUEST_UNCACHE,
		REQUEST_UNCACHE_RELAY,

	//These channels are used when requesting servers to find a player.
		REQUEST_FIND,
		REQUEST_FIND_RELAY
	;


}
