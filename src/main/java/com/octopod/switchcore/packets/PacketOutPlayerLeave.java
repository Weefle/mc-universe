package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that this player has left the server.
 */
public class PacketOutPlayerLeave extends SwitchPacket
{
	String UUID;

	public PacketOutPlayerLeave(MinecraftPlayer player)
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

	@Override
	public String[] getMessage()
	{
		return new String[]{UUID};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_PLAYER_LEAVE_SERVER.toString();
	}
}
