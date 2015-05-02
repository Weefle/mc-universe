package com.octopod.switchcore.server;

/**
 * A simple interface for generating new values.
 *
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface ServerPropertyGenerator<T>
{
	public T nextValue();
}
