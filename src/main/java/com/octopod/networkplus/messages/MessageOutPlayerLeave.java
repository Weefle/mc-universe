package com.octopod.networkplus.messages;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that this player has left the server.
 */
public class MessageOutPlayerLeave extends NetworkMessage
{
	public MessageOutPlayerLeave(MinecraftPlayer player)
	{
		setChannel(StaticChannel.PLAYER_LEAVE_SERVER);
		setMessage(player.getUUID());
	}
}
