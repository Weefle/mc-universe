package com.octopod.networkplus.extensions;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkExtension
{
	public String getName();

	public void onEnable();

	public void onDisable();
}
