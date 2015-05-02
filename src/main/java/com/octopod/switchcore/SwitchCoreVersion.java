package com.octopod.switchcore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum SwitchCoreVersion
{
	V0_0_1("0.0.1"); //Pre-alpha Stage

	public static SwitchCoreVersion LATEST;

	static
	{
		LATEST = SwitchCoreVersion.values()[SwitchCoreVersion.values().length - 1];
	}

	private String version;

	private SwitchCoreVersion(String version)
	{
		this.version = version;
	}

	@Override
	public String toString()
	{
		return version;
	}
}
