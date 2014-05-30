package com.octopod.network;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum NPLogLevel {

	INFO(0, NPPermission.NETWORK_LOGGER_INFO),

	WARNING(1, NPPermission.NETWORK_LOGGER_WARNING),

	DEBUG(2, NPPermission.NETWORK_LOGGER_DEBUG),

	VERBOSE(3, NPPermission.NETWORK_LOGGER_VERBOSE);

	int level;
	NPPermission permission;

	private NPLogLevel(int n, NPPermission perm) {
		level = n;
		permission = perm;
	}

	public int getLevel() {
		return level;
	}

	public NPPermission getPermission() {
		return permission;
	}

}
