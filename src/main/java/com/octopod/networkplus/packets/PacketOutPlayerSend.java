package com.octopod.networkplus.packets;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Requests a server to tell us if this player is welcomed on the server or not.
 */
public class PacketOutPlayerSend extends NetworkPacket
{
	String UUID;

	public PacketOutPlayerSend(MinecraftPlayer player)
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
		return StaticChannel.OUT_PLAYER_SEND_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_PLAYER_SEND_REQUEST.toString();
	}
}
