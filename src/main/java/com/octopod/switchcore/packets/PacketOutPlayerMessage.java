package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftCommandSource;
import com.octopod.switchcore.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Sending this message to a server will cause it to look for any player matching
 * the name and sending that player a message.
 */
public class PacketOutPlayerMessage extends SwitchPacket
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

	public String getRecipientName()
	{
		return to;
	}

	public String getSenderName()
	{
		return from;
	}

	public String getPrivateMessage()
	{
		return message;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{from, to, message};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_PLAYER_MESSAGE.toString();
	}
}
