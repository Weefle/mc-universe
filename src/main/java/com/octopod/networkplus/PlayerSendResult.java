package com.octopod.networkplus;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum PlayerSendResult
{
	/**
	 * If the request passed.
	 */
	SUCCESS,

	/**
	 * If the request failed due to the server being full.
	 */
	SERVER_FULL,

	/**
	 * If the request failed due to the player being banned.
	 */
	SERVER_BANNED,

	/**
	 * If the request failed due to the player not being whitelisted
	 */
	SERVER_WHITELISTED,
}
