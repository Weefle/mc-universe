package com.octopod.networkplus.compatable;

import com.octopod.networkplus.CachedServer;
import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.Server;
import com.octopod.networkplus.ServerValue;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.EventHandler;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.event.events.NetworkMessageSendEvent;
import net.minecraft.util.com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LilypadEssentialsCompatability implements CompatabilityLayer
{
	@Override
	public String getName()
	{
		return "LilypadEssentials";
	}

	@Override
	public void onEnable()
	{
		NetworkPlus.getEventManager().registerListener(this);
	}

	@Override
	public void onDisable()
	{
		NetworkPlus.getEventManager().unregisterListener(this);
	}

	@EventHandler
	public void onMessageSend(NetworkMessageSendEvent event)
	{

	}

	@EventHandler
	public void onMessageRecieve(NetworkMessageEvent event)
	{
		//This runs WAY too much, why??
		if(event.getChannel().equals("lilyessentials.sync"))
		{
			String[] split = event.getRawMessage().split("\0");
			String servername = split[0];
			List<String> players = Lists.newArrayList(split);
			players.remove(0);

			ServerDatabase database = NetworkPlus.getServerDatabase();
			if(!database.serverExists(event.getServer()))
			{
				//Create new server
				database.setServer(new CachedServer(event.getServer()));
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
