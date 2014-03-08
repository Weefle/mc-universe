package com.octopod.network;

import com.octopod.network.util.BukkitUtils;
import com.octopod.octolib.common.IOUtils;
import com.octopod.octolib.yaml.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NetworkConfig {

    private final static File configFile = new File("plugins/Network/config.yml");

    //These formats use String.format()
    public static String FORMAT_ALERT = 		"&8[&bAlert&8]&6 %s";
    public static String FORMAT_MSG_SENDER = 	"&8[&b%s&8]&7 [me -> %s]&6 %s";
    public static String FORMAT_MSG_TARGET = 	"&8[&b%s&8]&7 [%s -> me]&6 %s";

	private static Long    TIMEOUT = 500L;
	private static Integer DEBUG_MODE = 0;
	private static String  CHANNEL_PREFIX = "network";
    private static Boolean HUB_ENABLED = false;
    private static Integer HUB_PRIORITY = 0;
    private static String  SERVER_NAME = "New Server";
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

    private static void createFile(File file) throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    private static void writeFile(InputStream is, File file) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        IOUtils.copy(is, output);
        output.close();
    }

    private static void writeConfig(CommandSender sender) throws IOException {

        //Backup the old config.yml if it exists
        if(configFile.exists())
        {
            String fileName = "config-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-SS").format(new Date()) + ".yml";
            File backupConfigFile = new File("plugins/Network/" + fileName);
            createFile(backupConfigFile);
            //Copy the old config to this new backup config.
            writeFile(new FileInputStream(configFile), backupConfigFile);
            BukkitUtils.sendMessage(sender, "&eBackup saved as " + fileName);
        }
        //Generate a new config.yml
        else
        {
            BukkitUtils.sendMessage(sender, "&eWriting new configuration.");
            try {
                createFile(configFile);
            } catch (IOException e) {
                BukkitUtils.sendMessage(sender, "&cUnable to create a new config.yml file.");
                throw e;
            }
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
            writeFile(defaultConfigInput, configFile);
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

        InputStream defaultConfigInput = NetworkPlugin.class.getClassLoader().getResourceAsStream("config.yml");

		//This is the single YAML configuration we should use.
		YamlConfiguration config;

        YamlConfiguration defaultConfig = null;
        if(defaultConfigInput != null)
            try {
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

		CHANNEL_UNCACHE = 			CHANNEL_PREFIX + ".uncache.request";
		CHANNEL_UNCACHE_RELAY =		CHANNEL_PREFIX + ".uncache.relay";

        BukkitUtils.sendMessage(sender, "&7Successfully loaded configuration!");

	}

    public static Boolean isHub() {return HUB_ENABLED;}
    public static Integer getHubPriority() {return HUB_PRIORITY;}

	public static Long getRequestTimeout() {return TIMEOUT;}

	public static Integer getDebugMode() {return DEBUG_MODE;}

	public static String getRequestPrefix() {return CHANNEL_PREFIX;}

    public static String getServerName() {
        if(SERVER_NAME != null) {
            return SERVER_NAME;
        } else {
            return NetworkPlugin.self.getUsername();
        }
    }

    public static Integer getConnectionMaxAttempts() {
        return CONNECTION_ATTEMPTS_MAX;
    }

    public static Long getConnectionAttemptInterval() {
        return CONNECTION_ATTEMPTS_INTERVAL;
    }

    //Channel variables
	public static String
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

	//These channels are used when requestinga server to uncache a server's name.
		CHANNEL_UNCACHE,
		CHANNEL_UNCACHE_RELAY
	;
}
