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

	private final Collection<IDataHubStateChangeListener> stateChangeListeners = new ConcurrentLinkedQueue<>();

	private DataHubServiceProvider() {

	}

	public void addDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	public void removeDataHubStateListener(final IDataHubStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	private void fireStateChangedEvent(final boolean online, final boolean loggedIn) {
		for (final IDataHubStateChangeListener l : stateChangeListeners) {
			try {
				l.hubStateChanged(online, loggedIn);
			} catch (final Exception e) {
				LOGGER.error("Error in listener: " + e.getMessage(), e);
			}
		}
	}

	public boolean isHubOnline() {
		// FIXME: This should return true if the hub is online regardless of logged in state.
		return UpstreamUrlProvider.INSTANCE.isAvailable();
	}

	public boolean isLoggedIn() {
		return UpstreamUrlProvider.INSTANCE.isAvailable();
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
}
