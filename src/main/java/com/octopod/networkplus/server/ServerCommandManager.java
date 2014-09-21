package com.octopod.networkplus.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerCommandManager
{
	private Map<String, ServerCommand> labelMap = Collections.synchronizedMap(new HashMap<String, ServerCommand>());
	private Map<Class<? extends ServerCommand>, ServerCommand> classMap = Collections.synchronizedMap(new HashMap<Class<? extends ServerCommand>, ServerCommand>());

	public void reset()
	{
		labelMap.clear();
		classMap.clear();
	}

	public ServerCommand getCommand(String label)
	{
		return labelMap.get(label);
	}

	public ServerCommand getCommand(Class<? extends ServerCommand> clazz)
	{
		return classMap.get(clazz);
	}

	public Map<String, ServerCommand> getCommands()
	{
		return labelMap;
	}

	public void registerCommand(ServerCommand... commands)
	{
		for(ServerCommand command: commands) {
			labelMap.put(command.getLabel(), command);
			classMap.put(command.getClass(), command);
			for(String alias: command.getAliases()) {
				labelMap.put(alias, command);
			}
		}
	}
}
