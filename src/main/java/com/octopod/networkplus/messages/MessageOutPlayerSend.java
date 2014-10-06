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
	String UUID;

	public MessageOutPlayerSend(MinecraftPlayer player)
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
		return StaticChannel.PLAYER_WELCOME_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.PLAYER_WELCOME_RETURN.toString();
	}
}
