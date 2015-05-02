package com.hyperfresh.mcuniverse.server;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface ServerProperty<T>
{
	public String getName();

	public Class<T> getType();

	public T nextValue();
}
