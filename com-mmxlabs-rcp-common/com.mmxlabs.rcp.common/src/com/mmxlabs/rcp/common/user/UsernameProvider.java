/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.user;

import com.mmxlabs.rcp.common.ServiceHelper;

/**
 * Utility class to get the current username. Either from a provided service (e.g. to return the hub username or from the system property).
 * 
 * @author Simon Goodall
 *
 */
public class UsernameProvider {
	private UsernameProvider() {

	}

	public static final String getUsername() {
		return ServiceHelper.withCheckedOptionalService(IUserNameProvider.class, p -> {
			if (p != null) {
				return p.getUsername();
			}
			String property = System.getProperty("user.name");
			if (property != null && !property.isEmpty()) {
				return property;
			}
			return "Unknown user";
		});
	}
}
