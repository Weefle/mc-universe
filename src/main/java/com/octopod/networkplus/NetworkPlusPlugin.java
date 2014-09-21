package com.octopod.networkplus;

import com.octopod.networkplus.network.lilypad.LilypadInterface;

import java.io.File;
import java.io.InputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkPlusPlugin extends LilypadInterface
{
	public void enablePlugin();

	public void disablePlugin();

	public InputStream getResource(String path);

	public File getPluginFolder();

	public String getPluginName();

	public String getPluginVersion();
}
