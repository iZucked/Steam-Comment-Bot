/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.hub.services.users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;

/**
 * Utility class to get the current username. Either from a provided service (e.g. to return the hub username or from the system property).
 * 
 * @author Simon Goodall
 *
 */
public class UsernameProvider implements IUserNameProvider, IUserNameMapping {
	public static final UsernameProvider INSTANCE = new UsernameProvider();

	private String userId = "UnknownUser";

	private final Map<String, String> displayNameCache = new ConcurrentHashMap<>();

	private UsernameProvider() {
		final String property = System.getProperty("user.name");
		if (property != null && !property.isEmpty()) {
			userId = property;
		}

		try {
			final ISecurePreferences PREFERENCES = SecurePreferencesFactory.getDefault();
			if (PREFERENCES.nodeExists("upstream")) {
				userId = PREFERENCES.node("upstream").get("lastSeenUserId", userId);
			}
		} catch (final Exception e) {
			// Silently ignore
		}

	}

	public void setUserId(final String userId) {
		this.userId = userId;
		try {
			final ISecurePreferences PREFERENCES = SecurePreferencesFactory.getDefault();
			PREFERENCES.node("upstream").put("lastSeenUserId", userId, true);
		} catch (final Exception e) {
			// Silently ignore
		}

	}

	@Override
	public String getDisplayName(final String userId) {
		if (userId == null) {
			return null;
		}
		return displayNameCache.computeIfAbsent(userId, UserNameUpdater::getDisplayName);
	}

	@Override
	public final String getUserID() {
		return userId;
	}
}
