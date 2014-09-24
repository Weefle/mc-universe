package com.octopod.networkplus.server.bukkit;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.NetworkPlusPlugin;
import com.octopod.networkplus.command.CommandGetInfo;
import com.octopod.networkplus.command.CommandPing;
import com.octopod.networkplus.server.ServerInterface;
import lilypad.client.connect.api.Connect;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitNetworkPlusPlugin extends JavaPlugin implements NetworkPlusPlugin
{
	@Override
	public ServerInterface getServerInterface()
	{
		return new BukkitServerInterface();
	}

	@Override
	public void onEnable()
	{
		NetworkPlus.init(this);

		NetworkPlus.getCommandManager().registerCommand(
			new CommandPing("/ping"),
			new CommandGetInfo("/serverinfo")
		);

		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);

		try {
			NetworkPlus.reloadConfig();
		} catch (IOException e) {
			NetworkPlus.getServer().console("Error Loading Config: " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}

		NetworkPlus.getConnection().connect();
	}

	@Override
	public void onDisable()
	{
		NetworkPlus.getConnection().disconnect();
	}

	@Override
	public void enablePlugin()
	{
		onEnable();
		Bukkit.getPluginManager().enablePlugin(this);
	}

	@Override
	public void disablePlugin()
	{
		Bukkit.getPluginManager().disablePlugin(this);
		onDisable();
	}

	@Override
	public InputStream getResource(String path)
	{
		return this.getClassLoader().getResourceAsStream(path);
	}

	@Override
	public File getPluginFolder()
	{
		return this.getDataFolder();
	}

	@Override
	public String getPluginName()
	{
		return this.getDescription().getName();
	}

	@Override
	public String getPluginVersion()
	{
		return this.getDescription().getVersion();
	}

	@Override
	public Connect getConnect()
	{
		return this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
	}
}
