package com.octopod.networkplus.messages;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server that a player has joined this server.
 */
public class MessageOutPlayerJoin extends NetworkMessage
{
	public MessageOutPlayerJoin(MinecraftPlayer player)
	{
		setChannel(StaticChannel.PLAYER_JOIN_SERVER);
		setMessage(player.getUUID());
	}
}
