/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.hub.info.DatahubInformation;
import com.mmxlabs.hub.services.permissions.IUserPermissionsService;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.hub.services.users.IUserNameMapping;
import com.mmxlabs.hub.services.users.IUserNameProvider;
import com.mmxlabs.hub.services.users.UserNameUpdater;
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

		final boolean changedToOnlineAndLoggedIn = online && loggedIn && loggedIn != this.loggedIn;

		this.online = online;
		this.loggedIn = loggedIn;

		System.out.printf("Hub State - Online: %s Logged In %s\n", online, loggedIn);

		if (changedToOnlineAndLoggedIn) {
			// We have changed to online + logged in, so refresh some state

			// Update username
			UserNameUpdater.refreshUserId();

			// Update permissions model
			try {
				UserPermissionsService.INSTANCE.updateUserPermissions();
			} catch (final Exception e) {
				LOGGER.error("Error refreshing permissions: " + e.getMessage(), e);
			}
		}

		for (final IDataHubStateChangeListener l : stateChangeListeners) {
			try {
				l.hubStateChanged(online, loggedIn, changedToOnlineAndLoggedIn);
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

	public <T extends HttpRequestBase> @Nullable Triple<CloseableHttpClient, T, HttpClientContext> makeRequest(String urlPath, Function<URI, T> requestFactory) {
		return UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, requestFactory);
	}

	public <U> @Nullable U doGetRequest(String urlPath, ResponseHandler<U> responseHandler) throws IOException {
		return doRequest(urlPath, HttpGet::new, responseHandler);
	}

	public <T extends HttpRequestBase, U> @Nullable U doRequest(String urlPath, Function<URI, T> requestFactory, ResponseHandler<U> responseHandler) throws IOException {

		if (!isOnlineAndLoggedIn()) {
			return null;
		}

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, requestFactory);
		if (p == null) {
			return null;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();
		final var context= p.getThird();
		
		return httpClient.execute(request, responseHandler, context);
	}

	public void doDeleteRequest(String urlPath, CheckedConsumer<HttpResponse, IOException> responseHandler) throws IOException {

		if (!isOnlineAndLoggedIn()) {
			return;
		}

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, HttpDelete::new);
		if (p == null) {
			return;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();
		httpClient.execute(request, response -> {
			responseHandler.accept(response);
			return null;
		});
	}

	public <T extends HttpRequestBase, U> @Nullable U doRequest(String urlPath, Function<URI, T> requestFactory, Consumer<T> requestCusomiser, ResponseHandler<U> responseHandler) throws IOException {

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, requestFactory);
		if (p == null) {
			return null;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();

		requestCusomiser.accept(request);

		return httpClient.execute(request, responseHandler);

	}

	public <U> @Nullable U doPostRequest(String urlPath, Consumer<HttpPost> requestCusomiser, ResponseHandler<U> responseHandler) throws IOException {

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, HttpPost::new);
		if (p == null) {
			return null;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();

		requestCusomiser.accept(request);

		return httpClient.execute(request, responseHandler);

	}

	public <T extends HttpRequestBase> boolean doGetRequestAsBoolean(String urlPath, ResponseHandler<Boolean> responseHandler) throws IOException {

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, HttpGet::new);
		if (p == null) {
			return false;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();

		Boolean b = httpClient.execute(request, responseHandler);
		if (b == null) {
			return false;
		}
		return b;

	}

	public <T extends HttpRequestBase> boolean doRequestAsBoolean(String urlPath, Function<URI, T> requestFactory, ResponseHandler<Boolean> responseHandler) throws IOException {

		final var p = UpstreamUrlProvider.INSTANCE.makeRequest(urlPath, requestFactory);
		if (p == null) {
			return false;
		}
		final var httpClient = p.getFirst();
		final var request = p.getSecond();

		Boolean b = httpClient.execute(request, responseHandler);
		if (b == null) {
			return false;
		}
		return b;

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
