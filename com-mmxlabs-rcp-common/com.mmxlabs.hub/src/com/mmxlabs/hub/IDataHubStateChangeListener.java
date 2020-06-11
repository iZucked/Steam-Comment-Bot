package com.mmxlabs.hub;

public interface IDataHubStateChangeListener {

	void hubStateChanged(boolean online, boolean loggedin);

	void hubPermissionsChanged();

}
