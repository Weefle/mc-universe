package com.octopod.switchcore.lilypad;

import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.SwitchCoreListener;
import com.octopod.switchcore.event.events.NetworkMessageInEvent;
import com.octopod.switchcore.exceptions.DeserializationException;
import com.octopod.switchcore.packets.SwitchPacket;
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
		SwitchCore.getEventManager().callEvent(event);

		if(!event.isCancelled() && event.getChannel().equals(StaticChannel.SWITCH_PACKET.toString()))
		{
			try
			{
				SwitchPacket packet = SwitchCore.getSerializer().deserialize(message);
				if(packet != null)
				{
					SwitchCoreListener.onPacketInbound(server, packet);
				}
			} catch (DeserializationException e) {}
		}
	}
}
