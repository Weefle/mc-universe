package com.octopod.network.cache;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.commands.DocumentedCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandCache {
	
	private CommandCache() {}
	
	private static CommandCache cache;
	
	public static CommandCache getCache() {
		if(cache != null) {
			return cache;
		} else {
			return new CommandCache();
		}
	}
	
	public static void deleteCache() {
		if(cache != null) {
			cache = null;
		}
	}
	
	private Map<String, DocumentedCommand> commandMap = new HashMap<String, DocumentedCommand>();
	
	public DocumentedCommand getCommand(String label) {
		return commandMap.get(label);
	}
	
	public Map<String, DocumentedCommand> getCommands() {
		return commandMap;
	}
	
	public void registerCommand(DocumentedCommand... commands) {
		for(DocumentedCommand command: commands) {
			commandMap.put(command.getClass().getSimpleName(), command);
			NetworkPlugin.self.getCommand(command.getLabel()).setExecutor(command);			
		}
	}

}