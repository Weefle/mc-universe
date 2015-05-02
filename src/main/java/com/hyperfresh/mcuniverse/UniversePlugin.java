package com.hyperfresh.mcuniverse;

import com.hyperfresh.mcuniverse.lilypad.LilypadGetter;
import com.hyperfresh.mcuniverse.minecraft.MinecraftServer;

import java.io.File;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface UniversePlugin extends LilypadGetter
{
	public void enablePlugin();

	public void disablePlugin();

	public MinecraftServer getServerInterface();

	public InputStream getResource(String path);

	public File getPluginFolder();

	public String getPluginName();

	public String getPluginVersion();
}
