package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octal.common.IOUtils;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.yaml.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NetworkConfig {

	private final static File configFile = new File(NetworkPlus.getDataFolder(), "config.yml");

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

	private static boolean nullCheck(Object... objects) {
		for(Object o: objects) {
			if(o == null) {
				return true;
			}
		}
		return false;
	}

	private static void writeConfig(CommandSender sender) throws IOException
	{
		//Backup the old config.yml if it exists
		if(configFile.exists())
		{
			String fileName = "config-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-SS").format(new Date()) + ".yml";
			File backupConfigFile = new File(NetworkPlus.getDataFolder(), fileName);

			//Copy the old config to this new backup config.
			FileInputStream fileInputStream = new FileInputStream(configFile);
			Util.write(backupConfigFile, fileInputStream);
			IOUtils.closeSilent(fileInputStream);

			BukkitUtils.sendMessage(sender, "&eBackup saved as " + fileName);
		}

		writeDefaultConfig(sender);
	}

	/**
	 * Writes the default configuration (resource) into configFile.
	 * Throws various errors.
	 * @param sender Who to send the messages to.
	 * @throws NullPointerException, IOException
	 */
	private static void writeDefaultConfig(CommandSender sender) throws NullPointerException, IOException {

		InputStream defaultConfigInput = NetworkConfig.class.getClassLoader().getResourceAsStream("config.yml");

		BukkitUtils.sendMessage(sender, "&eWriting default configuration to config.yml.");

		try {
			Util.write(configFile, defaultConfigInput);
		} catch (NullPointerException e) {
			BukkitUtils.sendMessage(sender, "&cCouldn't find the internal configuration file.");
			throw e;
		} catch (FileNotFoundException e) {
			BukkitUtils.sendMessage(sender, "&cThe config.yml doesn't exist in your folder. If you see this message, I will personally give you 5$");
			throw e;
		} catch (IOException e) {
			BukkitUtils.sendMessage(sender, "&cCouldn't write to the config.yml. Try checking the file's permissions?");
			throw e;
		} finally {
			IOUtils.closeSilent(defaultConfigInput);
		}
	}

	/**
	 * Loads the configuration.
	 * Each created instance of NetworkConfig will load a new config.
	 */
	public static void reloadConfig() {reloadConfig(Bukkit.getConsoleSender());}

	public static void reloadConfig(CommandSender sender) {

		BukkitUtils.sendMessage(sender, "&7Loading configuration...");

		InputStream defaultConfigInput = NetworkPlusPlugin.class.getClassLoader().getResourceAsStream("config.yml");

		//This is the single YAML configuration we should use.
		YamlConfiguration config;

		YamlConfiguration defaultConfig = null;
		if(defaultConfigInput != null) try {
			defaultConfig = new YamlConfiguration(defaultConfigInput);
		} catch (Exception e) {}

		try {

			//A config.yml doesn't exist, so create a new one.
			if(!configFile.exists()) {
				writeConfig(sender);
			}

			config = new YamlConfiguration(configFile);

			//Check if the version of the default config is newer, or the current configuration is missing keys.
			if (
				(defaultConfig != null && defaultConfig.getInteger("version", 0) > config.getInteger("version", -1)) ||
				nullCheck(
					TIMEOUT =           Long.valueOf(config.getInteger("request-timeout")),
					DEBUG_MODE =        config.getInteger("debug-messages"),
					CHANNEL_PREFIX =    config.getString("channel-prefix"),
					HUB_ENABLED =       config.getBoolean("hub-enabled"),
					HUB_PRIORITY =      config.getInteger("hub-priority"),
					SERVER_NAME =       config.getString("name"),

					CONNECTION_ATTEMPTS_MAX =       config.getInteger("connection-attempts-max"),
					CONNECTION_ATTEMPTS_INTERVAL =  Long.valueOf(config.getInteger("connection-attempts-interval", 1000))
				)
			) {
				writeConfig(sender);
				config.load(configFile);
			}

		//Something errored out while writing/reading the configuration.
		} catch (Exception e) {
			BukkitUtils.sendMessage(sender, "&cSomething went wrong while loading Network's configuration.");
			BukkitUtils.sendMessage(sender, "&cThis can usually happen if the plugin was loaded using unsafe methods.");
			BukkitUtils.sendMessage(sender, "&cIf a restart doesn't fix it, report the error in the console.");
			BukkitUtils.sendMessage(sender, "&cThe plugin will continue operating under default values.");
			e.printStackTrace();
		} finally {
			IOUtils.closeSilent(defaultConfigInput);
		}

		BukkitUtils.sendMessage(sender, "TIMEOUT: &a" + TIMEOUT);
		BukkitUtils.sendMessage(sender, "DEBUG_MODE: &a" + DEBUG_MODE);
		BukkitUtils.sendMessage(sender, "CHANNEL_PREFIX: &a\"" + CHANNEL_PREFIX + "\"");
		BukkitUtils.sendMessage(sender, "HUB_ENABLED: &a" + HUB_ENABLED);
		BukkitUtils.sendMessage(sender, "HUB_PRIORITY: &a" + HUB_PRIORITY);
		BukkitUtils.sendMessage(sender, "SERVER_NAME: &a\"" + getServerName() + "\"");

		BukkitUtils.sendMessage(sender, "&7Successfully loaded configuration!");

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
