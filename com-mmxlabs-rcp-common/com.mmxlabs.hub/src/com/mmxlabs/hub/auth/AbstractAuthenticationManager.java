/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.OkHttpClient;

abstract class AbstractAuthenticationManager {

	static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticationManager.class);

	static final OkHttpClient httpClient = HttpClientUtil.basicBuilder()
			.build();

	static final ISecurePreferences PREFERENCES = SecurePreferencesFactory.getDefault();

	private static final String PREFERENCES_NODE = "upstream";

	AtomicBoolean authenticationShellIsOpen = new AtomicBoolean(false);

	public abstract void run(String upstreamURL, @Nullable Shell optionalDisplay);

	abstract boolean isAuthenticated(String upstreamURL);

	public void storeInSecurePreferences(String key, String value) {
		// returns node corresponding to the path specified. If such node does not exist, a new node is created
		try {
			final ISecurePreferences node = PREFERENCES.node(PREFERENCES_NODE);
			node.put(key, value, true);
		} catch (IllegalArgumentException e) {
			LOGGER.error(String.format("Failed to create a node in secure preferences, please use a valid node path: %s", e.getMessage()));
		} catch (StorageException e) {
			LOGGER.error(String.format("Failed to encrypt the value and save it to secure preferences: %s", e.getMessage()));
		}
	}

	public Optional<String> retrieveFromSecurePreferences(String key) {
		Optional<String> value = Optional.empty();

		final ISecurePreferences node = PREFERENCES.node(PREFERENCES_NODE);
		try {
			value = Optional.ofNullable(node.get(key, null));
		} catch (final StorageException e) {
			LOGGER.error(String.format("Failed to get value with key %s from secure preferences: %s", key, e.getMessage()));
		}

		return value;
	}

	public void deleteFromSecurePreferences(String key) {
		if (PREFERENCES.nodeExists("upstream")) {
			final ISecurePreferences node = PREFERENCES.node(PREFERENCES_NODE);
			node.remove(key);
		}
	}

	public abstract void clearCookies(String url);

	public abstract void logout(String upstreamURL, @Nullable Shell shell);

}
