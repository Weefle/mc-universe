package com.octopod.networkplus.server.bukkit;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.Permission;
import com.octopod.networkplus.server.ServerPlayer;
import com.octopod.util.minecraft.ChatUtils;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;


/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitPlayer implements ServerPlayer
{
	Player player;

	public BukkitPlayer(Player player)
	{
		this.player = player;
	}

	@Override
	public String getName()
	{
		return player.getName();
	}

	@Override
	public void sendMessage(String message)
	{
		player.sendMessage(ChatUtils.colorize(message));
	}

	@Override
	public void sendJsonMessage(String json)
	{
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public boolean hasPermission(Permission permission)
	{
		return player.hasPermission(permission.getNode());
	}

	@Override
	public String getID()
	{
		return player.getUniqueId().toString();
	}

	@Override
	public void redirect(String serverID)
	{
		NetworkPlus.getConnection().redirectPlayer(this, serverID);
	}
}
