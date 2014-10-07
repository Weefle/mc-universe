package com.octopod.switchcore.extensions;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface SwitchCoreExtension
{
	public String getName();

	public void onEnable();

	public void onDisable();
}
