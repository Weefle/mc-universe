package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.networkplus.lilypad.LilypadGetter;

import java.io.File;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkPlusPlugin extends LilypadGetter
{
	public void enablePlugin();

	public void disablePlugin();

	public MinecraftServerInterface getServerInterface();

	public InputStream getResource(String path);

	public File getPluginFolder();

	public String getPluginName();

	public String getPluginVersion();
}
