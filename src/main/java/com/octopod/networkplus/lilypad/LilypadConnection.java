package com.octopod.networkplus.lilypad;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.*;
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
	 * if <code>server</code> is null, the request will be sent to all servers.
	 *
	 * @param server the destination server's identifier, or null
	 * @param channel the channel to send the request on
	 * @param message the message to send
	 * @return the result of the request
	 */
	@Override
	public void sendMessage(String server, String channel, String message)
	{
		try {sendRequestAsync(new MessageRequest(server, channel, message));} catch (UnsupportedEncodingException e) {}
	}

	@Override
	public void broadcastMessage(String channel, String message)
	{
		sendMessage(null, channel, message);
	}

	@Override
	public void sendPlayer(MinecraftPlayer player, String server)
	{
		sendRequest(new RedirectRequest(server, player.getName()));
	}
}
