package com.hyperfresh.mcuniverse.event;

/**
 * Defines an Event with cancel functionality.
 * Note that does not actually stop the event from being passed to future handlers,
 * but just marks it as being "cancelled" for the future handlers to check for.
 *
 * @author octopod
 */
public abstract class CancellableEvent extends Event
{
	private boolean cancelled = false;

	/**
	 * Returns if this Event is "cancelled".
	 *
	 * @return if this event is "cancelled"
	 */
	public final boolean isCancelled() {return cancelled;}

	/**
	 * Sets if this Event is "cancelled".
	 *
	 * @param cancelled if this event is "cancelled"
	 */
	public final void setCancelled(boolean cancelled) {this.cancelled = cancelled;}
}
