package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that this player has left the server.
 */
public class PacketInPlayerLeave extends Packet
{
	String UUID;

	public PacketInPlayerLeave(MinecraftPlayer player)
	{
		UUID = player.getUUID();
	}

	public MinecraftUser getPlayer()
	{
		return UniverseAPI.getInstance().getInterface().getOfflinePlayer(UUID);
	}

	public String getUUID()
	{
		return UUID;
	}
}
