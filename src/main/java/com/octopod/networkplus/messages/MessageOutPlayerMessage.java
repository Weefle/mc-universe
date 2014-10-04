package com.octopod.networkplus.messages;

import com.octopod.minecraft.MinecraftCommandSource;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Sending this message to a server will cause it to look for any player matching
 * the name and sending that player a message.
 */
public class MessageOutPlayerMessage extends NetworkMessage
{
	public MessageOutPlayerMessage(MinecraftCommandSource source, String name, String message)
	{
		setChannel(StaticChannel.PLAYER_MESSAGE);
		setMessage(name, message, source.getName());
	}
}
