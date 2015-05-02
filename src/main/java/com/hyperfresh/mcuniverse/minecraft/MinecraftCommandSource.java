package com.hyperfresh.mcuniverse.minecraft;

public interface MinecraftCommandSource
{
	public String getName();

	public void sendMessage(String message);

	public void dispatchCommand(String command);

	public boolean hasPermission(String permission);
}
