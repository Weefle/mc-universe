package com.octopod.networkplus.network.lilypad;

import com.octopod.networkplus.NetworkEvents;
import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.NetworkPlusPlugin;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.event.Listener;
import com.octopod.networkplus.event.ListenerResult;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.networkplus.network.NetworkMessage;
import com.octopod.networkplus.network.NetworkReturn;
import com.octopod.networkplus.server.ServerLogger;
import com.octopod.networkplus.server.ServerPlayer;
import com.octopod.util.configuration.yaml.YamlConfiguration;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.Result;

import java.io.UnsupportedEncodingException;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LilypadConnection implements NetworkConnection
{
	/**
	 * The Lilypad connection instance.
	 */
	private LilypadListener listener = new LilypadListener();

	private ServerLogger logger;

	private Connect connection;

	private YamlConfiguration config;

	private NetworkPlusPlugin plugin;

	/**
	 * Gets the Connect instance and ensures that it is connected.
	 * This should fire the NetworkConnectedEvent after it connects.
	 */
	public LilypadConnection(NetworkPlusPlugin plugin, ServerLogger logger) {

		this.config = NetworkPlus.getInstance().getConfig();
		this.logger = logger;
		this.plugin = plugin;

		connection = plugin.getConnect();

		//Unregisters any listeners if they exist.
		if(listener != null) {
			connection.unregisterEvents(listener);
		}

	}

	@Override
	public String getName() {return "Lilypad";}

	@Override
	public String getServerIdentifier()
	{
		if(connection == null) return "";
		return connection.getSettings().getUsername();
	}

	@Override
	public boolean isConnected()
	{
		return connection != null && connection.isConnected();
	}

	@Override
	public void disconnect()
	{
		connection.unregisterEvents(listener);
	}

	@Override
	public void connect()
	{
		connection.registerEvents(listener);

		if(connection != null && !connection.isConnected()) {

			new Thread(new Runnable() {

				@Override
				public void run()
				{
					if(!connection.isConnected())
					{
						logger.i("Waiting for LilyPad to connect...");

						int connectionAttempts = 0;
						int maxConnectionAttempts = config.getInt("connection-attempts-max", 10);
						long connectionAttemptInterval = config.getLong("connection-attempts-interval", 1000);

						while(!connection.isConnected() && connectionAttempts < maxConnectionAttempts) {
							try {
								logger.i("Waiting for LilyPad connection... " + connectionAttempts);
								synchronized(this) {
									wait(connectionAttemptInterval);
								}
							} catch (InterruptedException e) {}
							connectionAttempts++;
						}

						//If LilyPad still isn't connected, then most commands should be disabled.
						if(!connection.isConnected())
						{
							logger.i("&cLilypad could not connect!");
							plugin.disablePlugin();
							return;
						}
					}
					NetworkEvents.actionNetworkConnected();
				}

			}).start();
		} else
		if(isConnected()) {
			NetworkEvents.actionNetworkConnected();
		}
	}

	/**
	 * Sends a Lilypad request. Returns the result, or null if an exception occured.
	 * @param request The request to send.
	 * @return The Result object.
	 */
	private Result sendRequest(Request request) {
		try {
			return connection.request(request).awaitUninterruptibly(config.getInt("request-timeout", 500));
		} catch (RequestException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends a Lilypad Request to a server.
	 * if <code>serverID</code> is null, the request will be sent to all servers.
	 *
	 * @param serverID the destination server's identifier, or null
	 * @param channel the channel to send the request on
	 * @param message the message to send
	 * @return the result of the request
	 */
	private Result sendMessageRequest(String serverID, String channel, String message)
	{
		try {
			return sendRequest(new MessageRequest(serverID, channel, message));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public void sendNetworkRequest(String serverID, final NetworkMessage request, final NetworkReturn callback)
	{
		sendMessageRequest(serverID, request.getChannel(), request.getMessage());

		final EventManager eventManager = NetworkPlus.getInstance().getEventManager();
		if(request.getReturnChannel() != null && callback != null)
		{
			final Listener<NetworkMessageEvent> tempListener = new Listener<NetworkMessageEvent>()
			{
				@Override
				public void onEvent(NetworkMessageEvent event)
				{
					if(event.getChannel().equals(request.getReturnChannel()))
					{
						callback.ret(ListenerResult.PASS, event);
						eventManager.unregisterListener(this);
					}
				}
			};
			eventManager.registerListener(tempListener);
			if(tempListener.waitFor(500) == ListenerResult.FAIL)
			{
				callback.ret(ListenerResult.FAIL, null);
			}
		}
	}

	@Override
	public void sendNetworkRequest(String serverID, final NetworkMessage request)
	{
		sendNetworkRequest(serverID, request, null);
	}

	@Override
	public void broadcastNetworkRequest(NetworkMessage request)
	{
		sendNetworkRequest(null, request, null);
	}

	@Override
	public void redirectPlayer(ServerPlayer player, String server)
	{
		sendRequest(new RedirectRequest(server, player.getName()));
	}
}
