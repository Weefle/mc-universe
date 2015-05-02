package com.hyperfresh.mcuniverse.packets;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

import com.hyperfresh.mcuniverse.minecraft.MinecraftCommandSource;

/**
 * Sending this message to a server will cause it to look for any player matching
 * the name and sending that player a message.
 */
public class PacketInPlayerMessage extends Packet
{
	String from;
	String to;
	String message;

	public PacketInPlayerMessage(MinecraftCommandSource source, String to, String message)
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
}
