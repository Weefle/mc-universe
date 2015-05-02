package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInPlayerChat extends Packet
{
	String UUID;
	String message;

	public PacketInPlayerChat(MinecraftPlayer player, String message)
	{
		this.UUID = player.getUUID();
		this.message = message;
	}

	public MinecraftUser getPlayer()
	{
		return UniverseAPI.getInstance().getInterface().getOfflinePlayer(UUID);
	}

	public String getMessage()
	{
		return message;
	}
}
