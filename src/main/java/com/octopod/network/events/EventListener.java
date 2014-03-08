package com.octopod.network.events;

/**
 * @author Octopod
 *         Last updated on 3/7/14
 */
public interface EventListener<T extends Event> {

    public boolean onEvent(T event);

}
