package com.hyperfresh.mcuniverse.lilypad;

import com.hyperfresh.mcuniverse.StaticChannel;
import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.UniverseEventPoster;
import com.hyperfresh.mcuniverse.event.events.NetworkMessageInEvent;
import com.hyperfresh.mcuniverse.exceptions.DeserializationException;
import com.hyperfresh.mcuniverse.packets.Packet;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LilypadListener
{
	@EventListener
	public void onLilypadMessage(MessageEvent lpEvent)
	{
		String channel = lpEvent.getChannel(), server, message;
//            if(!channel.startsWith(NetworkConfig.getChannelPrefix())) {
//                return;
//            }
		try {
			server = lpEvent.getSender();
			message = lpEvent.getMessageAsString();
		} catch (UnsupportedEncodingException e) {
			return;
		}

		NetworkMessageInEvent event = new NetworkMessageInEvent(server, channel, message);
		UniverseAPI.getInstance().getEventBus().post(event);

		if(!event.isCancelled() && event.getChannel().equals(StaticChannel.SWITCH_PACKET.toString()))
		{
			try
			{
				Packet packet = UniverseAPI.getInstance().getSerializer().deserialize(message);
				if(packet != null)
				{
					UniverseEventPoster.onPacketInbound(server, packet);
				}
			} catch (DeserializationException e) {}
		}
	}
}
