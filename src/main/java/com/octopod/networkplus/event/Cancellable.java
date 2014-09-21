package com.octopod.networkplus.event;

/**
 * @author Octopod
 *         Created on 3/15/14
 */
public interface Cancellable
{
    public boolean isCancelled();

    public void setCancelled(boolean cancel);
}
