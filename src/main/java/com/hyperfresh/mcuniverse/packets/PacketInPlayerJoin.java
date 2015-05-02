package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that a player has joined this server.
 */
public class PacketInPlayerJoin extends Packet
{
	String UUID;

	public PacketInPlayerJoin(MinecraftPlayer player)
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
