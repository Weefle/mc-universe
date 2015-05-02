package com.hyperfresh.mcuniverse.minecraft;

public interface MinecraftUser
{
	public String getUUID();

	public void setWhitelisted(boolean whitelist);

	public boolean isWhitelisted();

	public void setBanned(boolean banned);

	public boolean isBanned();

	public MinecraftPlayer getPlayer();
}
