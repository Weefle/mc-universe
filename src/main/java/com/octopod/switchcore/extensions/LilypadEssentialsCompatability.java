package com.octopod.switchcore.extensions;

import com.octopod.switchcore.CachedServer;
import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.Server;
import com.octopod.switchcore.ServerValue;
import com.octopod.switchcore.database.ServerDatabase;
import com.octopod.switchcore.event.EventHandler;
import com.octopod.switchcore.event.events.NetworkMessageInEvent;
import com.octopod.switchcore.event.events.NetworkPacketOutEvent;
import net.minecraft.util.com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LilypadEssentialsCompatability implements SwitchCoreExtension
{
	@Override
	public String getName()
	{
		return "LilypadEssentials Compatability";
	}

	@Override
	public void onEnable()
	{
		SwitchCore.getEventManager().registerListener(this);
	}

	@Override
	public void onDisable()
	{
		SwitchCore.getEventManager().unregisterListener(this);
	}

	@EventHandler
	public void onPacketOutbound(NetworkPacketOutEvent event)
	{

	}

	@EventHandler
	public void onMessageInbound(NetworkMessageInEvent event)
	{
		//This runs WAY too much, why??
		if(event.getChannel().equals("lilyessentials.sync"))
		{
			String[] split = event.getMessage().split("\0");
			String servername = split[0];
			List<String> players = Lists.newArrayList(split);
			players.remove(0);

			ServerDatabase database = SwitchCore.getServerDatabase();
			if(!database.serverExists(event.getServer()))
			{
				//Create new server
				database.setServer(new CachedServer(event.getServer(), true));
			}
			Server server = database.getServer(event.getServer());
			server.setValue(ServerValue.SERVER_NAME, servername);
			server.setValue(ServerValue.ONLINE_PLAYERS, players.toArray(new String[players.size()]));
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.dispatch"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.alert"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.find"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.sendall"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.blackmessage"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.messagesuccess"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.message"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.admin"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.glist"))
		{
			event.setCancelled(true);
		}

		if(event.getChannel().equals("lilyessentials.glistreturn"))
		{
			event.setCancelled(true);
		}

		//The redirect request?
		if(event.getChannel().equals("lilyessentials.send"))
		{
			event.setCancelled(true);
		}

		//Send back whether the player is welcomed on this server
		if(event.getChannel().equals("lilyessentials.sendrequest"))
		{
			event.setCancelled(true);
		}

		//This is the response to the above channel
		if(event.getChannel().equals("lilyessentials.sendresponse"))
		{
			event.setCancelled(true);
		}
	}
}
