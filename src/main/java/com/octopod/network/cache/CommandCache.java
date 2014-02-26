package com.octopod.network.cache;

import com.octopod.network.commands.DocumentedCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandCache {

    private CommandCache() {}

	private static Map<String, DocumentedCommand> commandMap = Collections.synchronizedMap(new HashMap<String, DocumentedCommand>());

    public static void reset() {
        commandMap.clear();
    }
	
	public static DocumentedCommand getCommand(String label) {
		return commandMap.get(label);
	}

	public static Map<String, DocumentedCommand> getCommands() {
		return commandMap;
	}
	
	public static void registerCommand(DocumentedCommand... commands) {
		for(DocumentedCommand command: commands) {
			commandMap.put(command.getLabel(), command);
		}
	}

}