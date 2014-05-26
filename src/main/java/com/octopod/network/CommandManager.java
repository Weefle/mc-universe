package com.octopod.network;

import com.octopod.network.commands.NetworkCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private CommandManager() {}

	private static Map<String, NetworkCommand> labelMap = Collections.synchronizedMap(new HashMap<String, NetworkCommand>());
    private static Map<Class<? extends NetworkCommand>, NetworkCommand> classMap = Collections.synchronizedMap(new HashMap<Class<? extends NetworkCommand>, NetworkCommand>());

    public static void reset() {
        labelMap.clear();
        classMap.clear();
    }

	public static NetworkCommand getCommand(String label) {
		return labelMap.get(label);
	}

    public static NetworkCommand getCommand(Class<? extends NetworkCommand> clazz) {
        return classMap.get(clazz);
    }

	public static Map<String, NetworkCommand> getCommands() {
		return labelMap;
	}

	public static void registerCommand(NetworkCommand... commands) {
		for(NetworkCommand command: commands) {
			labelMap.put(command.getLabel(), command);
            classMap.put(command.getClass(), command);
            for(String alias: command.getAliases()) {
                labelMap.put(alias, command);
            }
		}
	}

}