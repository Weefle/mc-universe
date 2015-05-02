package com.hyperfresh.mcuniverse;

import com.hyperfresh.mcuniverse.commands.ServerCommands;
import com.octopod.minecraft.BukkitServer;
import com.octopod.minecraft.MinecraftServer;
import lilypad.client.connect.api.Connect;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitUniversePlugin extends JavaPlugin implements UniversePlugin
{
	@Override
	public MinecraftServer getServerInterface()
	{
		return new BukkitServer();
	}

	@Override
	public void onEnable()
	{
		UniverseAPI.init(this);

		UniverseAPI.getInstance().getCommandManager().registerCommands(new ServerCommands());

		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);

		try
		{
			UniverseAPI.getInstance().reloadConfig();
		}
		catch (IOException e)
		{
			UniverseAPI.getInstance().getInterface().console("Error Loading Config: " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}

		UniverseAPI.getInstance().getConnection().connect();
	}

	@Override
	public void onDisable()
	{
		UniverseAPI.getInstance().getConnection().disconnect();
		UniverseAPI.dinit();
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
