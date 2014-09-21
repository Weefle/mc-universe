package com.octopod.networkplus;

import com.octopod.networkplus.server.ServerLogger;
import com.octopod.util.common.FileUtil;
import com.octopod.util.configuration.yaml.YamlConfiguration;
import com.octopod.util.minecraft.ChatUtils.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPlusConfig
{
	/**
	 * The currently active configuration.
	 */
	private YamlConfiguration config = null;

	private NetworkPlusPlugin plugin = null;

	private ServerLogger logger = null;

	private File file;

	public NetworkPlusConfig(NetworkPlusPlugin plugin, ServerLogger logger)
	{
		this.logger = logger;
		this.plugin = plugin;
		this.file = new File(plugin.getPluginFolder(), "config.yml");
	}

	private InputStream readInternalConfig()
	{
		return plugin.getResource("config.yml");
	}

	private InputStream readLocalConfig()
	{
		try {
			return new FileInputStream(file);
		} catch (IOException e) {
			return null;
		}
	}

	private boolean isConfigOld()
	{
		try(InputStream defaultConfigInput = readInternalConfig())
		{
			YamlConfiguration config = new YamlConfiguration(defaultConfigInput);
			return config.getInt("version", 0) > getConfig().getInt("version", -1);
		}
		catch(Exception e) {return false;}
	}

	/**
	 * Writes the default configuration (resource) into configFile.
	 * Throws various errors.
	 *
	 * @throws NullPointerException, IOException
	 */
	private YamlConfiguration writeInternalConfig() throws IOException
	{
		//Backup the old config.yml if it exists
		if (file.exists())
		{
			String fileName = "config.yml.old";
			File backupConfigFile = new File(plugin.getPluginFolder(), fileName);

			//Copy the old config to this new backup config.
			try (InputStream is = readLocalConfig()) {
				FileUtil.write(backupConfigFile, is);
			}
			logger.i("Old configuration renamed to " + fileName);
		}
		try (InputStream is = readInternalConfig())
		{
			if (is == null) throw new IOException("Couldn't find the internal configuration file.");

			YamlConfiguration config = new YamlConfiguration(is);

			logger.i("Writing default configuration to config.yml, version " + config.getInt("version"));

			FileUtil.write(file, is);

			return config;
		}
	}

	/**
	 * Loads the configuration.
	 * Each created instance of NetworkPlusConfig will load a new config.
	 */
	public void load() throws IOException
	{
		logger.i("Loading Net+ configuration...");

		if (!file.exists())
		{
			//Sets the config from the internal file
			setConfig(writeInternalConfig());
		}
		else
		{
			//Sets the current configuration from config.yml.
			setConfig(new YamlConfiguration(readLocalConfig()));
			if (isConfigOld())
			{
				setConfig(writeInternalConfig());
			}
		}

		logger.i(ChatColor.GREEN + "Successfully loaded configuration!");
	}

	private void setConfig(YamlConfiguration config)
	{
		this.config = config;
	}

	public YamlConfiguration getConfig()
	{
		return this.config;
	}
}
