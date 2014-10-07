package com.octopod.networkplus.packets;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.PlayerSendResult;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInPlayerSend extends NetworkPacket
{
	PlayerSendResult result;
	String UUID;

	public PacketInPlayerSend(String UUID)
	{
		if(NetworkPlus.getInterface().isFull())
		{
			this.result = PlayerSendResult.SERVER_FULL;
		}
		else if(NetworkPlus.getInterface().isBanned(UUID))
		{
			this.result = PlayerSendResult.SERVER_BANNED;
		}
		else if(NetworkPlus.getInterface().isWhitelisted(UUID))
		{
			this.result = PlayerSendResult.SERVER_WHITELISTED;
		}
		else
		{
			this.result = PlayerSendResult.SUCCESS;
		}
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{UUID, result.name()};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.IN_PLAYER_SEND_REQUEST.toString();
	}
}
