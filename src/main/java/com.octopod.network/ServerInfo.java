package com.octopod.network;

import java.util.*;

import org.bukkit.Bukkit;

import com.octopod.network.bukkit.BukkitUtils;
import static com.octopod.network.Property.*;

/**
 * @author Octopod
 *         Created on 3/13/14
 */

/**
 * ServerFlags is pretty much just a wrapper around a HashMap.
 * The HashMap is what will contain all the server's information.
 */
public class ServerInfo {

    /**
     * The map of flags for this ServerInfo instance.
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * Returns a ServerFlags object from a JSON string.
     * @param json The JSON string.
     * @return A ServerFlags object.
     */
    public static ServerInfo fromJson(String json) {
        return NetworkPlus.gson().fromJson(json, ServerInfo.class);
    }

    /**
     * An interface that will generate the default ServerFlags object
     */
    public static interface Generator {
        public ServerInfo generate();
    }

    /**
     * The current generator that creates the default ServerFlags object
     */
    private static Generator currentGenerator = new Generator() {

        public ServerInfo generate()
        {
            ServerInfo info = new ServerInfo();

            //Server's config name (or ID if blank)
            info.setProperty(SERVER_NAME, NPConfig.getServerName());

            //Server's config description (or MOTD if blank)
            info.setProperty(SERVER_DESCRIPTION, Bukkit.getServer().getMotd());

            //Server's maximum players
            info.setProperty(MAX_PLAYERS, Bukkit.getServer().getMaxPlayers());

            //If the server's whitelist is enabled
            info.setProperty(IS_WHITELISTED, Bukkit.getServer().hasWhitelist());

            //Server's list of whitelisted players (empty list if whitelist is disabled)
            info.setProperty(WHITELISTED_PLAYERS, new ArrayList<>(Arrays.asList(BukkitUtils.getWhitelistedPlayerNames())));

            //Server's hub priority (-1 if not a hub)
            info.setProperty(HUB_PRIORITY, NPConfig.isHub() ? NPConfig.getHubPriority() : -1);

            //Server's plugin version
            info.setProperty(SERVER_VERSION, NetworkPlus.getPluginVersion());

            //Server's list of online players
            info.setProperty(ONLINE_PLAYERS, new ArrayList<>(Arrays.asList(BukkitUtils.getPlayerNames())));

            //Players queued for this server.
            info.setProperty(QUEUED_PLAYERS, QueueManager.instance.getQueueMembers());

            //Time the server was last online (-1 if it's online right now)
            info.setProperty(LAST_ONLINE, -1L);

            return info;
        }

    };

    public static ServerInfo generateInfo() {
        return currentGenerator.generate();
    }

    /**
     * Merges the map into this instance's option map.
     * Used to "patch" the current properties.
     * @param info The ServerFlags to copy flags from.
     */
    public void merge(ServerInfo info) {
        properties.putAll(info.toMap());
    }

	public void merge(Map<String, Object> properties) {
		this.properties.putAll(properties);
	}

    public boolean propertyExists(String key) {
        return properties.containsKey(key);
    }

    /**
     * Sets a flag to a value.
     * @param key The key.
     * @param value The value.
     */
    private void setProperty(String key, Object value) {
        properties.put(key, value);
    }

	public void setCustom(String key, Object value) {
		setProperty(key, value);
	}

	public void setProperty(Property key, Object value) {
		setProperty(key.toString(), value);
	}

    /**
     * Gets the flag by key.
     * @param key The key.
     * @return The value located at the key, or 'defaultValue' if the key doesn't exist.
     */
    @SuppressWarnings("unchecked")
    private <T> T getProperty(String key, Class<T> clazz, T defaultValue) throws ClassCastException
    {
        Object value;

        if((value = properties.get(key)) == null) return defaultValue;

        if(!clazz.isInstance(value)) {
            throw new ClassCastException("Value for server flag '" + key + "' is not an instance of " + clazz.getName());
        } else {
            return (T)value;
        }
    }

    public <T> T getProperty(Property key, Class<T> clazz) {
        return getProperty(key.toString(), clazz, null);
    }

    public Object getProperty(Property key) {
        return getProperty(key, Object.class);
    }

    public String getFlagString(Property key) {
        return getProperty(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(Property key) {
        return getProperty(key, (Class<List<String>>) (Class<?>) List.class);
    }

    public int getInteger(Property key) {
        Object value = getProperty(key);
        if(value != null) {
            if(value instanceof Integer)
                return (Integer)value;
            if(value instanceof Double)
                return ((Double)value).intValue();
        }
        return -1;
    }

    public long getLong(Property key) {
        Object value = getProperty(key);
        if(value != null) {
            if(value instanceof String) {
                try {
                    return Long.parseLong((String)value);
                } catch (NumberFormatException e) {}
            }
            if(value instanceof Long)
                return (Long)value;
            if(value instanceof Integer)
                return (Integer)value;
            if(value instanceof Double)
                return ((Double)value).intValue();
        }
        return -1L;
    }

    public boolean getBoolean(Property key) {
        return getProperty(key, Boolean.class);
    }

    //A bunch of default getters

    public String getServerName()   {return getFlagString(Property.SERVER_NAME);}
    public String getDescription()  {return getFlagString(Property.SERVER_DESCRIPTION);}
    public String getVersion()      {return getFlagString(Property.SERVER_VERSION);}

    public Integer getMaxPlayers()  {return getInteger(Property.MAX_PLAYERS);}
    public Integer getHubPriority() {return getInteger(Property.HUB_PRIORITY);}

    public List<String> getWhitelistedPlayers() {return getStringList(Property.WHITELISTED_PLAYERS);}
    public List<String> getOnlinePlayers()      {return getStringList(Property.ONLINE_PLAYERS);}
    public List<String> getQueuedPlayers()      {return getStringList(Property.QUEUED_PLAYERS);}

	public Long getLastOnline() {return getLong(Property.LAST_ONLINE);}

    /**
     * Gets this object returned as a JSON string.
     * @return The JSON representation of this object.
     */
    @Override
    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

    public Map<String, Object> toMap() {
        return properties;
    }

    public NPMessage toServerMessage(String serverID) {
        return new NPMessage(serverID, toString());
    }

}