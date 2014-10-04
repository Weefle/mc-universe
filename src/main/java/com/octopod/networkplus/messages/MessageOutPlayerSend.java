package com.octopod.networkplus.messages;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Requests a server to tell us if this player is welcomed on the server or not.
 */
public class MessageOutPlayerSend extends NetworkMessage
{
	public MessageOutPlayerSend(MinecraftPlayer player)
	{
		setMessage(player.getUUID());
		setChannel(StaticChannel.PLAYER_WELCOME_REQUEST);
		setReturnChannel(StaticChannel.PLAYER_WELCOME_RETURN);
	}

}
