package com.hyperfresh.mcuniverse;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface TempListenerFinish
{
	/**
	 * An implementation for waitForAsync() that will run after the wait.
	 * @param successful false if waitForAsync() timed out
	 */
	public void finish(boolean successful);
}
