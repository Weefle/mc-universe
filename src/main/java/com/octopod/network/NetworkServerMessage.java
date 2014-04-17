package com.octopod.network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Octopod
 *         Created on 4/14/14
 */
public class NetworkServerMessage {

    private ArrayList<String> arguments;

    public NetworkServerMessage(String... args) {
        arguments = new ArrayList<>(Arrays.asList(args));
    }

    public String[] getArgs() {
        return arguments.toArray(new String[arguments.size()]);
    }

    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

}
