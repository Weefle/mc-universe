package com.octopod.switchcore;

import com.octopod.minecraft.BukkitServer;
import com.octopod.minecraft.MinecraftServer;
import com.octopod.switchcore.commands.ServerCommands;
import lilypad.client.connect.api.Connect;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitSwitchCorePlugin extends JavaPlugin implements SwitchCorePlugin
{
	@Override
	public MinecraftServer getServerInterface()
	{
		return new BukkitServer();
	}

	@Override
	public void onEnable()
	{
		SwitchCore.init(this);

		SwitchCore.getInstance().getCommandManager().registerCommands(new ServerCommands());

		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);

		try
		{
			SwitchCore.getInstance().reloadConfig();
		}
		catch (IOException e)
		{
			SwitchCore.getInstance().getInterface().console("Error Loading Config: " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}

		SwitchCore.getInstance().getConnection().connect();
	}

	@Override
	public void onDisable()
	{
		SwitchCore.getInstance().getConnection().disconnect();
		SwitchCore.dinit();
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
