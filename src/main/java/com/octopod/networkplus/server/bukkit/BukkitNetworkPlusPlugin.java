package com.octopod.networkplus.server.bukkit;

import com.octopod.networkplus.NetworkPlusConfig;
import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.NetworkPlusPlugin;
import com.octopod.networkplus.network.lilypad.LilypadConnection;
import lilypad.client.connect.api.Connect;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitNetworkPlusPlugin extends JavaPlugin implements NetworkPlusPlugin
{
	@Override
	public void onEnable()
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();

		networkPlus.setServer(new BukkitServerInterface());
		networkPlus.setConfig(new NetworkPlusConfig(this, networkPlus.getLogger()));
		networkPlus.setConnection(new LilypadConnection(this, networkPlus.getLogger()));
	}

	@Override
	public void onDisable()
	{

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
