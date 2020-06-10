package com.mmxlabs.hub;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.services.permissions.IUserPermissionsService;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.hub.services.users.IUserNameMapping;
import com.mmxlabs.hub.services.users.IUserNameProvider;
import com.mmxlabs.hub.services.users.UsernameProvider;

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

	private final Collection<IDataHubStateChangeListener> stateChangeListeners = new ConcurrentLinkedQueue<>();

	private DataHubServiceProvider() {
		init();
	}

	public void init() {
		// Register details changed listener.
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(() -> fireStateChangedEvent(false, false));
	}

	public void addDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	public void removeDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	private synchronized void fireStateChangedEvent(final boolean online, final boolean loggedIn) {
		this.online = online;
		this.loggedIn = loggedIn;
		
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

	public IUserNameMapping getUserNameMappingService() {
		return UsernameProvider.INSTANCE;
	}

	public IUserNameProvider getUserNameProviderService() {
		return UsernameProvider.INSTANCE;
	}

	public IUserPermissionsService getUserPermissionsService() {
		return UserPermissionsService.INSTANCE;
	}

	public synchronized void setOnlineState(boolean newOnline) {
		if (this.online != newOnline) {
			fireStateChangedEvent(newOnline, newOnline ? this.loggedIn : false);
		}
	}

	public synchronized void setLoggedInState(boolean newLoggedIn) {
		if (this.loggedIn != newLoggedIn) {
			fireStateChangedEvent(this.online, newLoggedIn);
		}
	}
}
