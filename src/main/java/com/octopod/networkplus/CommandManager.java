package com.octopod.networkplus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CommandManager
{
	private Map<String, Command> labelMap = Collections.synchronizedMap(new HashMap<String, Command>());
	private Map<Class<? extends Command>, Command> classMap = Collections.synchronizedMap(new HashMap<Class<? extends Command>, Command>());

	public void reset()
	{
		labelMap.clear();
		classMap.clear();
	}

	public Command getCommand(String label)
	{
		return labelMap.get(label);
	}

	public Command getCommand(Class<? extends Command> clazz)
	{
		return classMap.get(clazz);
	}

	public Map<String, Command> getCommands()
	{
		return labelMap;
	}

	public void registerCommand(Command... commands)
	{
		for(Command command: commands) {
			labelMap.put(command.getLabel(), command);
			classMap.put(command.getClass(), command);
			for(String alias: command.getAliases()) {
				labelMap.put(alias, command);
			}
		}
	}
}
