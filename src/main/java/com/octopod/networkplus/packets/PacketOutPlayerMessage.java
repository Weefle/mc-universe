package com.octopod.networkplus.packets;

import com.octopod.minecraft.MinecraftCommandSource;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Sending this message to a server will cause it to look for any player matching
 * the name and sending that player a message.
 */
public class PacketOutPlayerMessage extends NetworkPacket
{
	String from;
	String to;
	String message;

	public PacketOutPlayerMessage(MinecraftCommandSource source, String to, String message)
	{
		this.from = source.getName();
		this.to = to;
		this.message = message;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{from, to, message};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.PLAYER_MESSAGE.toString();
	}
}
