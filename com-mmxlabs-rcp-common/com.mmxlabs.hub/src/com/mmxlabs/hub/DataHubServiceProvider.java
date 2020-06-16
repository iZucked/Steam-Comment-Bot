/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.info.DatahubInformation;
import com.mmxlabs.hub.services.permissions.IUserPermissionsService;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.hub.services.users.IUserNameMapping;
import com.mmxlabs.hub.services.users.IUserNameProvider;
import com.mmxlabs.hub.services.users.UserNameUpdater;
import com.mmxlabs.hub.services.users.UsernameProvider;

import okhttp3.Request.Builder;

/**
 * A central place to access Data Hub services
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class DataHubServiceProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataHubServiceProvider.class);

	private static final DataHubServiceProvider INSTANCE = new DataHubServiceProvider();

	public static DataHubServiceProvider getInstance() {
		return INSTANCE;
	}

	private boolean online = false;
	private boolean loggedIn = false;

	private @Nullable DatahubInformation datahubInformation;

	private final Collection<IDataHubStateChangeListener> stateChangeListeners = new ConcurrentLinkedQueue<>();

	private DataHubServiceProvider() {
		start();
	}

	private void start() {
		// Register details changed listener. Clear current state
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(() -> {
			datahubInformation = null;
			fireStateChangedEvent(false, false);
		});
	}

	public void addDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	public void removeDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	private synchronized void fireStateChangedEvent(final boolean online, final boolean loggedIn) {

		final boolean changedToLoggedIn = online && loggedIn && loggedIn != this.loggedIn;

		this.online = online;
		this.loggedIn = loggedIn;

		System.out.printf("Hub State - Online: %s Logged In %s\n", online, loggedIn);
		
		if (changedToLoggedIn) {
			// We have changed to online + logged in, so refresh some state

			// Update username
			UserNameUpdater.refreshUserId();

			// Update permissions model
			try {
				UserPermissionsService.INSTANCE.updateUserPermissions();
			} catch (final IOException e) {
				LOGGER.error("Error refreshing permissions: " + e.getMessage(), e);
			}
		}

		for (final IDataHubStateChangeListener l : stateChangeListeners) {
			try {
				l.hubStateChanged(online, loggedIn);
			} catch (final Exception e) {
				LOGGER.error("Error in listener: " + e.getMessage(), e);
			}
		}
	}

	public boolean isHubOnline() {
		return online;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public boolean isOnlineAndLoggedIn() {
		return online && loggedIn;
	}

	public @Nullable DatahubInformation getDatahubInformation() {
		return datahubInformation;
	}

	public void setDatahubInformation(@Nullable DatahubInformation datahubInformation) {
		this.datahubInformation = datahubInformation;
	}

	public IUserNameMapping getUserNameMappingService() {
		return UsernameProvider.INSTANCE;
	}

	public IUserNameProvider getUserNameProviderService() {
		return UsernameProvider.INSTANCE;
	}

	public IUserPermissionsService getUserPermissionsService() {
		return UserPermissionsService.INSTANCE;
	}

	public @Nullable Builder makeRequestBuilder(String urlPath) {
		return UpstreamUrlProvider.INSTANCE.makeRequestBuilder(urlPath);
	}

	public synchronized void setOnlineState(final boolean newOnline) {
		if (this.online != newOnline) {
			fireStateChangedEvent(newOnline, newOnline ? this.loggedIn : false);
		}
	}

	public synchronized void setLoggedInState(final boolean newLoggedIn) {
		if (this.loggedIn != newLoggedIn) {
			fireStateChangedEvent(this.online, newLoggedIn);
		}
	}
}
