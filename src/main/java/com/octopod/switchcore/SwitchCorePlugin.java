package com.octopod.switchcore;

import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.switchcore.lilypad.LilypadGetter;

import java.io.File;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface SwitchCorePlugin extends LilypadGetter
{
	public void enablePlugin();

	public void disablePlugin();

	public MinecraftServerInterface getServerInterface();

	public InputStream getResource(String path);

	public File getPluginFolder();

	public String getPluginName();

	public String getPluginVersion();
}
