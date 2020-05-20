/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.services.user;

public interface IUserPermissionsService {

	boolean isPermitted(String service, String permission);

	boolean hubSupportsPermissions();
}