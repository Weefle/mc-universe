package com.octopod.network;

import com.octopod.octal.common.IOUtils;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;
import com.octopod.octal.yaml.YamlConfiguration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NPConfig {

	/**
	 * The file in which to load the configuration from.
	 */
	private final static File configFile = new File(NetworkPlus.getDataFolder(), "config.yml");

	/**
	 * The currently active configuration.
	 */
	private static YamlConfiguration config = null;

	//These formats use String.format()
	public static String FORMAT_ALERT = 		"&8[&bAlert&8]&6 %s";
	public static String FORMAT_MSG_SENDER = 	"&8[&b%s&8]&7 [me -> %s]&6 %s";
	public static String FORMAT_MSG_TARGET = 	"&8[&b%s&8]&7 [%s -> me]&6 %s";

	private static Long    TIMEOUT = 500L;
	private static Integer DEBUG_MODE = 0;

	/**
	 * The current channel prefix.
	 * Only servers sharing the same prefix can communicate.
	 */
	private static String  CHANNEL_PREFIX = "network";
	private static Boolean HUB_ENABLED = false;
	private static Integer HUB_PRIORITY = 0;
	private static String  SERVER_NAME = "";
	private static Integer CONNECTION_ATTEMPTS_MAX = 10;
	private static Long    CONNECTION_ATTEMPTS_INTERVAL = 1000L;

	private static boolean objectsNull(Object... objects) {
		for(Object o: objects) {
			if(o == null) {
				return true;
			}
		}
		return false;
	}

	private static InputStream getDefaultConfig() {
		return NPConfig.class.getClassLoader().getResourceAsStream("src/config.yml");
	}

	private static boolean isConfigOld()
	{
		InputStream defaultConfigInput = getDefaultConfig();
		if(defaultConfigInput == null) return false;

		YamlConfiguration defaultConfig = new YamlConfiguration(defaultConfigInput);

		int defaultVersion = defaultConfig.getInteger("version", -1);
		int version = getConfig().getInteger("version", -1);

		return defaultVersion > version;
	}

	/**
	 * Writes the default configuration (resource) into configFile.
	 * Throws various errors.
	 * @throws NullPointerException, IOException
	 */
	private static void reset() throws NullPointerException, IOException {

		//Backup the old config.yml if it exists
		if(configFile.exists())
		{
			String fileName = "config-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-SS").format(new Date()) + ".yml";
			File backupConfigFile = new File(NetworkPlus.getDataFolder(), fileName);

			//Copy the old config to this new backup config.
			FileInputStream fileInputStream = new FileInputStream(configFile);
			Util.write(backupConfigFile, fileInputStream);
			IOUtils.closeSilent(fileInputStream);

			NetworkPlus.getLogger().i("Old configuration renamed to " + fileName);
		}

		InputStream defaultConfigInput = getDefaultConfig();

		NetworkPlus.getLogger().i("Writing default configuration to config.yml.");

		try {
			Util.write(configFile, defaultConfigInput);
		} catch (NullPointerException e) {
			NetworkPlus.getLogger().i("Couldn't find the internal configuration file.");
			throw e;
		} catch (FileNotFoundException e) {
			NetworkPlus.getLogger().i("The config.yml doesn't exist in your folder. If you see this message, I will personally give you 5$");
			throw e;
		} catch (IOException e) {
			NetworkPlus.getLogger().i("Couldn't write to the config.yml. Try checking the file's permissions?");
			throw e;
		} finally {
			IOUtils.closeSilent(defaultConfigInput);
		}
	}

	/**
	 * Loads the configuration.
	 * Each created instance of NetworkConfig will load a new config.
	 */
	public static void load() throws Exception
	{
		NetworkPlus.getLogger().i("Loading Net+ configuration...");

		//A config.yml doesn't exist, so create a new one.
		if(!configFile.exists()) {reset();}

		//Sets the current configuration from config.yml.
		setConfig(new YamlConfiguration(configFile));

		//Check if the version of the default config is newer, or the current configuration is missing keys.
		if (isConfigOld() || objectsNull(
				TIMEOUT = Long.valueOf(getConfig().getInteger("request-timeout")),
				DEBUG_MODE = getConfig().getInteger("debug-messages"),
				CHANNEL_PREFIX = getConfig().getString("channel-prefix"),
				HUB_ENABLED = getConfig().getBoolean("hub-enabled"),
				HUB_PRIORITY = getConfig().getInteger("hub-priority"),
				SERVER_NAME = getConfig().getString("name"),

				CONNECTION_ATTEMPTS_MAX = config.getInteger("connection-attempts-max"),
				CONNECTION_ATTEMPTS_INTERVAL = Long.valueOf(config.getInteger("connection-attempts-interval", 1000))
		)) {
			//Reset the config to the default and loads that up.
			reset();
			getConfig().load(configFile);
		}

		NetworkPlus.getLogger().i(ChatColor.GREEN + "Successfully loaded configuration!");
	}

	private static void setConfig(YamlConfiguration config) {
		NPConfig.config = config;
	}

	public static YamlConfiguration getConfig() {
		return config;
	}

	public static Boolean isHub() {return HUB_ENABLED;}

	public static Integer getHubPriority() {return HUB_PRIORITY;}

	public static Long getRequestTimeout() {return TIMEOUT;}

	public static Integer getDebugMode() {return DEBUG_MODE;}

	public static String getChannelPrefix() {return CHANNEL_PREFIX;}

	public static String getServerName() {
		if(SERVER_NAME.equals("")) {
			return NetworkPlus.getServerID();
		} else {
			return ChatUtils.colorize(SERVER_NAME);
		}
	}

	public static Integer getConnectionMaxAttempts() {
		return CONNECTION_ATTEMPTS_MAX;
	}

	public static Long getConnectionAttemptInterval() {
		return CONNECTION_ATTEMPTS_INTERVAL;
	}

}
