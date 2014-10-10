package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that this player has left the server.
 */
public class PacketInPlayerLeave extends SwitchPacket
{
	String UUID;

	public PacketInPlayerLeave(MinecraftPlayer player)
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
