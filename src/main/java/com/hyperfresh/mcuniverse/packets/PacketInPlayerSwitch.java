package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Requests a server to tell us if this player is welcomed on the server or not.
 */
public class PacketInPlayerSwitch extends Packet
{
	String UUID;

	public PacketInPlayerSwitch(MinecraftPlayer player)
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
