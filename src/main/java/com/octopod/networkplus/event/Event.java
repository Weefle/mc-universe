package com.octopod.networkplus.event;

public abstract class Event
{
	private boolean unlock = false;

	public void setUnlocked(boolean n) {
		unlock = n;
	}

	public boolean isUnlocked() {
		return unlock;
	}
}
