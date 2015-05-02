package com.octopod.switchcore.server;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface ServerProperty<T>
{
	public String getName();

	public Class<T> getType();

	public T nextValue();
}
