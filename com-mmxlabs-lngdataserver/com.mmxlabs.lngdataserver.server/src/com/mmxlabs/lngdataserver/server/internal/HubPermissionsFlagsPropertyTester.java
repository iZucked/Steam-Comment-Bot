/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.internal;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.lngdataserver.server.UserPermissionsService;

public class HubPermissionsFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (property.equals("hubsupported")) {
			return UserPermissionsService.INSTANCE.hubSupportsPermissions();
		} else if (property.equals("ispermitted") && args.length == 2) {
			String service = (String) args[0];
			String permission = (String) args[1];
			return UserPermissionsService.INSTANCE.isPermitted(service, permission);
		}
		return false;
	}
}
