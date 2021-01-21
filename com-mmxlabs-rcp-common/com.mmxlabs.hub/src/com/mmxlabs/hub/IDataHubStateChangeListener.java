/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub;

public interface IDataHubStateChangeListener {

	void hubStateChanged(boolean online, boolean loggedin, boolean changedToOnlineAndLoggedIn);

	void hubPermissionsChanged();

}
