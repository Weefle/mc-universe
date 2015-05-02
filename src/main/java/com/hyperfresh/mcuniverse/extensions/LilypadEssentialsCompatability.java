package com.hyperfresh.mcuniverse.extensions;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.database.ServerDatabase;
import com.hyperfresh.mcuniverse.event.EventSubscribe;
import com.hyperfresh.mcuniverse.event.events.NetworkMessageInEvent;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketOutEvent;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.hyperfresh.mcuniverse.server.networked.stored.StoredServer;
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
		UniverseAPI.getInstance().getEventBus().register(this);
	}

	@Override
	public void onDisable()
	{
		UniverseAPI.getInstance().getEventBus().unregister(this);
	}

	@EventSubscribe
	public void onPacketOutbound(NetworkPacketOutEvent event)
	{

	}

	@EventSubscribe
	public void onMessageInbound(NetworkMessageInEvent event)
	{
		//This runs so often that you'd might as well strangle your server with your hands right now.
		//Seriously, it would probably be less painful.
		if(event.getChannel().equals("lilyessentials.sync"))
		{
			String[] split = event.getMessage().split("\0");
			String servername = split[0];
			List<String> players = Lists.newArrayList(split);
			players.remove(0);

			ServerDatabase database = UniverseAPI.getInstance().getServerDatabase();
			if(!database.serverExists(event.getServer()))
			{
				//Create new server
				database.addServer(new StoredServer(event.getServer()));
			}
			UniverseServer server = database.getServer(event.getServer());
			if(server instanceof StoredServer)
			{
				StoredServer sserver = (StoredServer)server;
				sserver.setServerName(servername);
				sserver.setOnlinePlayers(players);
			}
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
