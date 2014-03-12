package com.octopod.network.events;

/**
 * @author Octopod
 *         Created on 3/12/14
 */
public enum EventPriority {

    SYSTEM (-1), //NEVER use this, only for the plugin's listeners
    LOWEST (0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    Integer n;
    private EventPriority(int n) {this.n = n;}

    public int getPriority() {return n;}

}
