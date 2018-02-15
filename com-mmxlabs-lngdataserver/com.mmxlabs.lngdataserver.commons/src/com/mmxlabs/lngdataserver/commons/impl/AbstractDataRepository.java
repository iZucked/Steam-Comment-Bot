package com.mmxlabs.lngdataserver.commons.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.commons.IDataRepository;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public abstract class AbstractDataRepository implements IDataRepository {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataRepository.class);

	protected final Triple<String, String, String> auth;
	protected String backendUrl;
	protected String upstreamUrl;
	protected boolean listenForNewLocalVersions;
	protected boolean listenForNewUpstreamVersions;
	protected final List<Consumer<String>> newLocalVersionCallbacks = new LinkedList<>();
	protected final List<Consumer<String>> newUpstreamVersionCallbacks = new LinkedList<>();

	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
			case StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY:
			case StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY:

				boolean listen = listenForNewUpstreamVersions;
				if (listen) {
					stopListeningForNewUpstreamVersions();
				}
				upstreamUrl = getUpstreamUrl();
				newUpstreamURL(upstreamUrl);

				if (listen) {
					startListenForNewUpstreamVersions();
				}

				break;
			default:
			}
		}
	};

	protected final @Nullable IPreferenceStore preferenceStore;

	protected Thread localVersionThread;
	protected Thread upstreamVersionThread;

	public AbstractDataRepository(@Nullable IPreferenceStore preferenceStore, final String localUrl) {
		this.preferenceStore = preferenceStore;
		auth = localUrl == null ? getUserServiceAuth() : new Triple<>(localUrl, "", "");
		upstreamUrl = getUpstreamUrl();
	}

	@Override
	public void listenToPreferenceChanges() {
		if (preferenceStore != null) {
			preferenceStore.addPropertyChangeListener(listener);
		}
	}

	@Override
	public void stopListenToPreferenceChanges() {
		if (preferenceStore != null) {
			preferenceStore.removePropertyChangeListener(listener);
		}
	}

	protected Triple<String, String, String> getUserServiceAuth() {
		IPreferenceStore p_preferenceStore = preferenceStore;
		if (p_preferenceStore != null) {
			final String url = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
			final String username = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
			final String password = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY);

			return new Triple<>(url, username, password);
		}
		return new Triple<>();
	}

	protected String getUpstreamUrl() {
		if (preferenceStore != null) {
			final String url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
			return url;
		}
		return "";
	}

	@Override
	public void startListenForNewLocalVersions() {
		listenForNewLocalVersions = true;
		if (!canWaitForNewLocalVersion()) {
			return;
		}
		localVersionThread = new Thread(() -> {
			while (listenForNewLocalVersions) {
				final CompletableFuture<String> newVersion = waitForNewLocalVersion();
				try {
					if (newVersion != null) {
						final String version = newVersion.get();
						newLocalVersionCallbacks.forEach(c -> c.accept(version));
					}
				} catch (final InterruptedException e) {
					LOG.error(e.getMessage());
				} catch (final ExecutionException e) {
					LOG.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
		localVersionThread.start();
	}

	@Override
	public void startListenForNewUpstreamVersions() {
		listenForNewUpstreamVersions = true;
		if (!canWaitForNewUpstreamVersion()) {
			return;
		}
		if (upstreamUrl == null || upstreamUrl.trim().isEmpty()) {
			// No URL, do not try and connect
			return;
		}
		upstreamVersionThread = new Thread(() -> {
			while (listenForNewUpstreamVersions) {
				final CompletableFuture<String> newVersion = waitForNewUpstreamVersion();
				try {
					final String version = newVersion.get();
					if (version != null) {
						System.out.println("New version " + version);
						newUpstreamVersionCallbacks.forEach(c -> c.accept(version));
					}
				} catch (final InterruptedException e) {
					if (!listenForNewUpstreamVersions) {
						return;
					}
				} catch (final ExecutionException e) {
					LOG.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
		upstreamVersionThread.setName("DataServer Upstream listener: " + getClass().getName());
		upstreamVersionThread.start();
	}

	@Override
	public void stopListeningForNewLocalVersions() {
		listenForNewLocalVersions = false;
		if (localVersionThread != null) {
			localVersionThread.interrupt();
			localVersionThread = null;
		}
	}

	@Override
	public void stopListeningForNewUpstreamVersions() {
		listenForNewUpstreamVersions = false;
		if (upstreamVersionThread != null) {
			upstreamVersionThread.interrupt();
			upstreamVersionThread = null;
		}
	}

	@Override
	public void registerLocalVersionListener(final Consumer<String> versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	@Override
	public void registerUpstreamVersionListener(final Consumer<String> versionConsumer) {
		newUpstreamVersionCallbacks.add(versionConsumer);
	}

	protected Triple<String, String, String> getServiceAuth() {
		return auth;
	}

	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	protected abstract CompletableFuture<String> waitForNewLocalVersion();

	protected abstract CompletableFuture<String> waitForNewUpstreamVersion();

	protected abstract void newUpstreamURL(String upstreamURL);

	public boolean hasUpstream() {
		return upstreamUrl != null && !upstreamUrl.isEmpty();
	}
}