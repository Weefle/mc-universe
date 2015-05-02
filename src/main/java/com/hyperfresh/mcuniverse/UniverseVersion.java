package com.hyperfresh.mcuniverse;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum UniverseVersion
{
	V0_0_1("0.0.1"); //Pre-alpha Stage

	public static UniverseVersion LATEST;

	static
	{
		LATEST = UniverseVersion.values()[UniverseVersion.values().length - 1];
	}

	private String version;

	private UniverseVersion(String version)
	{
		this.version = version;
	}

	@Override
	public String toString()
	{
		return version;
	}
}
