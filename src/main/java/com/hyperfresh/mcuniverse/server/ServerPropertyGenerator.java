package com.hyperfresh.mcuniverse.server;

/**
 * A simple interface for generating new values.
 *
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface ServerPropertyGenerator<T>
{
	public T nextValue();
}
