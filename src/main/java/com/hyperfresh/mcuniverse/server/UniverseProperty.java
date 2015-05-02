package com.hyperfresh.mcuniverse.server;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.util.configuration.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public enum UniverseProperty implements ServerProperty
{
	NAME(new ServerProperty<String>()
	{
		@Override
		public String getName()
		{
			return "Name";
		}

		@Override
		public Class<String> getType()
		{
			return String.class;
		}

		public String nextValue()
		{
			YamlConfiguration config = com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getConfig();
			String name = config.getString("name");
			return name == null || name.equals("") ? com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getServerIdentifier() : name;
		}
	}),	
	
	STATUS(new ServerProperty<ServerStatus>()
	{
		public Class<ServerStatus> getType() {return ServerStatus.class;}
	
		public String getName()
		{
			return "Status";
		}
	
		public ServerStatus nextValue()
		{
			return ServerStatus.ONLINE;
		}
	}),

	LAST_ONLINE(new ServerProperty<Long>()
	{
		public Class<Long> getType() {return Long.class;}

		public String getName()
		{
			return "Last Online";
		}

		public Long nextValue()
		{
			return System.currentTimeMillis();
		}
	}),

	LAST_PACKET(new ServerProperty<Long>()
	{
		public Class<Long> getType() {return Long.class;}

		public String getName()
		{
			return "Last Packet";
		}

		public Long nextValue()
		{
			return System.currentTimeMillis();
		}
	}),

	ONLINE_PLAYERS(new ServerProperty<List<String>>()
	{
		@SuppressWarnings("unchecked")
		public Class<List<String>> getType() {return (Class<List<String>>)(Class<?>)List.class;}

		public String getName()
		{
			return "Online Players";
		}

		public List<String> nextValue()
		{
			List<String> players = new ArrayList<>();
			for(MinecraftPlayer player: com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getInterface().getOnlinePlayers())
			{
				players.add(player.getUUID());
			}
			return players;
		}
	}),

	MAX_PLAYERS(new ServerProperty<Integer>()
	{
		public Class<Integer> getType() {return Integer.class;}

		public String getName()
		{
			return "Max Players";
		}

		public Integer nextValue()
		{
			return com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getInterface().getMaxPlayers();
		}
	}),

	WHITELIST_ENABLED(new ServerProperty<Boolean>()
	{
		public Class<Boolean> getType() {return Boolean.class;}

		public String getName()
		{
			return "Whitelist";
		}

		public Boolean nextValue()
		{
			return com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getInterface().getWhitelistEnabled();
		}
	}),

	WHITELISTED_PLAYERS(new ServerProperty<List<String>>()
	{
		@SuppressWarnings("unchecked")
		public Class<List<String>> getType() {return (Class<List<String>>)(Class<?>)List.class;}

		public String getName()
		{
			return "Whitelisted Players";
		}

		public List<String> nextValue()
		{
			return new ArrayList<>(Arrays.asList(com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getInterface().getWhitelistedPlayers()));
		}
	}),

	/**
	 * Gets the hub priority of this server. If the priority is -1, then it isn't enabled.
	 */
	HUB_PRIORITY(new ServerProperty<Integer>()
	{
		public Class<Integer> getType() {return Integer.class;}

		public String getName()
		{
			return "Hub Priority";
		}

		public Integer nextValue()
		{
			YamlConfiguration config = com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getConfig();
			return config.getBoolean("hub-enabled", false) ? config.getInt("hub-priority", 0) : -1;
		}
	});

	private UniverseProperty(ServerProperty<?> handle)
	{
		this.handle = handle;
	}

	private ServerProperty<?> handle;

	@Override
	public String getName()
	{
		return handle.getName();
	}

	@Override
	public Class<?> getType()
	{
		return handle.getType();
	}

	@Override
	public Object nextValue()
	{
		return handle.nextValue();
	}
}
