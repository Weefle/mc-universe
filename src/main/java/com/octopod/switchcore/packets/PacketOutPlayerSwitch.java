package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Requests a server to tell us if this player is welcomed on the server or not.
 */
public class PacketOutPlayerSwitch extends SwitchPacket
{
	String UUID;

	public PacketOutPlayerSwitch(MinecraftPlayer player)
	{
		UUID = player.getUUID();
	}

	public MinecraftOfflinePlayer getPlayer()
	{
		return SwitchCore.getInterface().getOfflinePlayer(UUID);
	}

	public String getUUID()
	{
		return UUID;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{UUID};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_PLAYER_SEND_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_PLAYER_SEND_REQUEST.toString();
	}
}
