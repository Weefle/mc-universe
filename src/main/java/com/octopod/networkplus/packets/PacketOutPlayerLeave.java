package com.octopod.networkplus.packets;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that this player has left the server.
 */
public class PacketOutPlayerLeave extends NetworkPacket
{
	String UUID;

	public PacketOutPlayerLeave(MinecraftPlayer player)
	{
		UUID = player.getUUID();
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
