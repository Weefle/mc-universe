package com.hyperfresh.mcuniverse.event;

import com.hyperfresh.mcuniverse.event.Event;

/**
 * An interface for event handlers.
 * TODO: Add priority support for event handlers.
 *
 * @author octopod
 */
public interface Handler<E extends Event> //implements Comparable<EventHandler>
{
	public void handle(E event);

	public Class<E> getEventType();
}
