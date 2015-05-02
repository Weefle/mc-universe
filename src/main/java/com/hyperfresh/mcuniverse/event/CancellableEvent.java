package com.hyperfresh.mcuniverse.event;

/**
 * Defines an Event as being cancellable.
 *
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface CancellableEvent
{
    public boolean isCancelled();

    public void setCancelled(boolean cancel);
}
