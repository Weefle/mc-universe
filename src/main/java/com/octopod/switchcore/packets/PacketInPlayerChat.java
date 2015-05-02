package com.octopod.switchcore.packets;

import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInPlayerChat extends SwitchPacket
{
	String UUID;
	String message;

	public PacketInPlayerChat(MinecraftPlayer player, String message)
	{
		this.UUID = player.getUUID();
		this.message = message;
	}

	public MinecraftOfflinePlayer getPlayer()
	{
		return SwitchCore.getInstance().getInterface().getOfflinePlayer(UUID);
	}

	public String getMessage()
	{
		return message;
	}
}
