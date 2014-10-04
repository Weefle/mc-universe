package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;
import com.octopod.networkplus.PlayerSendResult;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInPlayerSend extends NetworkMessage
{
	public MessageInPlayerSend(String ID)
	{
		if(NetworkPlus.getInterface().isFull())
		{
			setMessage(ID, PlayerSendResult.SERVER_FULL.name());
		}
		else if(NetworkPlus.getInterface().isBanned(ID))
		{
			setMessage(ID, PlayerSendResult.SERVER_BANNED.name());
		}
		else if(NetworkPlus.getInterface().isWhitelisted(ID))
		{
			setMessage(ID, PlayerSendResult.SERVER_WHITELISTED.name());
		}
		else
		{
			setMessage(ID, PlayerSendResult.SUCCESS.name());
		}
		setChannel(StaticChannel.PLAYER_WELCOME_RETURN);
	}
}
