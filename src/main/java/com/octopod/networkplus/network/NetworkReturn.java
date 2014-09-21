package com.octopod.networkplus.network;

import com.octopod.networkplus.event.ListenerResult;
import com.octopod.networkplus.event.events.NetworkMessageEvent;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkReturn
{
	public void ret(ListenerResult result, NetworkMessageEvent event);
}
