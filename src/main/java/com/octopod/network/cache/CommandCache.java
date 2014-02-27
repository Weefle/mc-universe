package com.octopod.network.cache;

import com.octopod.network.commands.DocumentedCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandCache {

    private CommandCache() {}

	private static Map<String, DocumentedCommand> labelMap = Collections.synchronizedMap(new HashMap<String, DocumentedCommand>());
    private static Map<Class<? extends DocumentedCommand>, DocumentedCommand> classMap = Collections.synchronizedMap(new HashMap<Class<? extends DocumentedCommand>, DocumentedCommand>());

    public static void reset() {
        labelMap.clear();
        classMap.clear();
    }

	public static DocumentedCommand getCommand(String label) {
		return labelMap.get(label);
	}

    public static DocumentedCommand getCommand(Class<? extends DocumentedCommand> clazz) {
        return classMap.get(clazz);
    }

	public static Map<String, DocumentedCommand> getCommands() {
		return labelMap;
	}

	public static void registerCommand(DocumentedCommand... commands) {
		for(DocumentedCommand command: commands) {
			labelMap.put(command.getLabel(), command);
            classMap.put(command.getClass(), command);
		}
	}

}