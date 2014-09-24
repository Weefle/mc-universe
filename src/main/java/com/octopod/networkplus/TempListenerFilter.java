package com.octopod.networkplus;

import com.octopod.networkplus.event.Event;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface TempListenerFilter<T extends Event>
{
	/**
	 * An interface for TempListener.
	 * Return true if the event is considered "valid", and should
	 * notify the lock on <code>listener</code>.
	 * @param listener the listener tied to this interface
	 * @param event the event
	 * @return true if the event should unlock the listener
	 */
	public boolean onEvent(TempListener<T> listener, T event);
}
