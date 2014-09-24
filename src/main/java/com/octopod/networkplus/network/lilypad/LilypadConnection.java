package com.octopod.networkplus.network.lilypad;

import com.octopod.networkplus.*;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.TempListener;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.networkplus.network.NetworkMessage;
import com.octopod.networkplus.network.NetworkReturn;
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

	private Connect connection;

	/**
	 * Gets the Connect instance and ensures that it is connected.
	 * This should fire the NetworkConnectedEvent after it connects.
	 */
	public LilypadConnection()
	{
		connection = NetworkPlus.getPlugin().getConnect();

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

		final YamlConfiguration config = NetworkPlus.getConfig();
		final Logger logger = NetworkPlus.getLogger();
		final NetworkPlusPlugin plugin = NetworkPlus.getPlugin();

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
					NetworkPlusListener.onNetworkConnected();
				}

			}).start();
		} else
		if(isConnected()) {
			NetworkPlusListener.onNetworkConnected();
		}
	}

	/**
	 * Sends a Lilypad request. Returns the result, or null if an exception occured.
	 * @param request The request to send.
	 * @return The Result object.
	 */
	private Result sendRequest(Request request) {
		try {
			return connection.request(request).awaitUninterruptibly(NetworkPlus.getConfig().getInt("request-timeout", 500));
		} catch (RequestException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void sendRequestAsync(Request request)
	{
		try {connection.request(request);} catch (RequestException e) {}
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
	private void sendMessageRequest(String serverID, String channel, String message)
	{
		try {sendRequestAsync(new MessageRequest(serverID, channel, message));} catch (UnsupportedEncodingException e) {}
	}

	@Override
	public void sendNetworkRequest(String serverID, final NetworkMessage request, final NetworkReturn ret)
	{
		final EventManager eventManager = NetworkPlus.getEventManager();
		Thread thread = null;

		if(request.getReturnChannel() != null && ret != null)
		{
			TempListenerFilter<NetworkMessageEvent> listener = new TempListenerFilter<NetworkMessageEvent>()
			{
				@Override
				public boolean onEvent(TempListener listener, NetworkMessageEvent event)
				{
					if(event.getChannel().equals(request.getReturnChannel()))
					{
						ret.ret(true, event);
						return true;
					}
					return false;
				}
			};

			final TempListener genListener = new TempListener<>(NetworkMessageEvent.class, listener);

			thread = new Thread()
			{
				public void run()
				{
					eventManager.registerListener(genListener);
					boolean success = genListener.waitFor(500);
					if(!success)
					{
						ret.ret(false, null);
						eventManager.unregisterListener(genListener);
					}
				}
			};
		}

		sendMessageRequest(serverID, request.getChannel(), request.getMessage());
		if(thread != null) thread.start();
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
