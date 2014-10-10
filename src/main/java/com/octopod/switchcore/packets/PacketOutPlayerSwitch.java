package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.switchcore.PlayerSwitchResult;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutPlayerSwitch extends SwitchPacket
{
	PlayerSwitchResult result;
	String UUID;

	public PacketOutPlayerSwitch(String UUID)
	{
		this.UUID = UUID;
		if(SwitchCore.getInterface().isFull())
		{
			this.result = PlayerSwitchResult.SERVER_FULL;
		}
		else if(SwitchCore.getInterface().isBanned(UUID))
		{
			this.result = PlayerSwitchResult.SERVER_BANNED;
		}
		else if(SwitchCore.getInterface().isWhitelisted(UUID))
		{
			this.result = PlayerSwitchResult.SERVER_WHITELISTED;
		}
		else
		{
			this.result = PlayerSwitchResult.SUCCESS;
		}
	}

	/**
	 * This is for testing purposes only, don't use this normally.
	 */
	public PacketOutPlayerSwitch()
	{
		this.result = PlayerSwitchResult.SUCCESS;
		this.UUID = "test";
	}

	public PlayerSwitchResult getResult()
	{
		return result;
	}

	public MinecraftOfflinePlayer getPlayer()
	{
		return SwitchCore.getInterface().getOfflinePlayer(UUID);
	}

	public String getUUID()
	{
		return UUID;
	}
}
