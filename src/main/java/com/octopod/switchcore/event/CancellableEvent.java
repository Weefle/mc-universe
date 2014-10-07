package com.octopod.switchcore.event;

/**
 * @author Octopod
 *         Created on 3/15/14
 */
public interface CancellableEvent
{
    public boolean isCancelled();

    public void setCancelled(boolean cancel);
}
