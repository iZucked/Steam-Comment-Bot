/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.services.permissions;

public interface IUserPermissionsService {

	boolean isPermitted(String service, String permission);

	boolean hubSupportsPermissions();
}