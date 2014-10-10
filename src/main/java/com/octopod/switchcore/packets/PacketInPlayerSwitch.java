package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Requests a server to tell us if this player is welcomed on the server or not.
 */
public class PacketInPlayerSwitch extends SwitchPacket
{
	String UUID;

	public PacketInPlayerSwitch(MinecraftPlayer player)
	{
		UUID = player.getUUID();
	}

	public MinecraftOfflinePlayer getPlayer()
	{
		return SwitchCore.getInterface().getOfflinePlayer(UUID);
	}

	public String getUUID()
	{
		return UUID;
	}
}
