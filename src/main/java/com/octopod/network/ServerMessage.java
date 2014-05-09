package com.octopod.network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Octopod
 *         Created on 4/14/14
 */
public class ServerMessage {

    private ArrayList<String> arguments;

    /**
     * An empty ServerMessage.
     */
    public static final ServerMessage EMPTY = new ServerMessage();

    public ServerMessage(String... args) {
        arguments = new ArrayList<>(Arrays.asList(args));
    }

    public String[] getArgs() {
        return arguments.toArray(new String[arguments.size()]);
    }

    /**
     * Returns a serialized version of the arguments.
     * TODO: Find an easier and quicker way to serialize and deserialize just a list of Strings.
     * @return A serialized String.
     */
    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

}
