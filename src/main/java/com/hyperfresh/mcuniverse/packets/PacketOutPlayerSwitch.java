package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.PlayerSwitchResult;
import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutPlayerSwitch extends Packet
{
	PlayerSwitchResult result;
	String UUID;

	public PacketOutPlayerSwitch(String UUID)
	{
		this.UUID = UUID;
		if(UniverseAPI.getInstance().getInterface().isFull())
		{
			this.result = PlayerSwitchResult.SERVER_FULL;
		}
		else if(UniverseAPI.getInstance().getInterface().isBanned(UUID))
		{
			this.result = PlayerSwitchResult.SERVER_BANNED;
		}
		else if(UniverseAPI.getInstance().getInterface().isWhitelisted(UUID))
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

	public MinecraftUser getPlayer()
	{
		return UniverseAPI.getInstance().getServer().getOfflinePlayer(UUID);
	}

	public String getUUID()
	{
		return UUID;
	}
}
