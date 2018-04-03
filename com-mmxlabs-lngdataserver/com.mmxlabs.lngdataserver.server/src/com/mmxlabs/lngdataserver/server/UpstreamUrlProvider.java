package com.mmxlabs.lngdataserver.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lngdataserver.server.dialogs.AuthDetailsPromptDialog;
import com.mmxlabs.lngdataserver.server.internal.Activator;
import com.mmxlabs.lngdataserver.server.preferences.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.rcp.common.RunnerHelper;

public class UpstreamUrlProvider {
	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> listeners = new HashSet<>();

	private boolean hasDetails = false;
	private AtomicBoolean dialogOpen = new AtomicBoolean(false);

	public UpstreamUrlProvider() {

		preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(listener);
	}

	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY:
				fireChangedListeners();
			}
		}

	};

	protected String username;

	protected String password;

	public String getBaseURL() {
		final String url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
		if (url == null) {
			return "";
		}

		if (!hasDetails) {
			final Display display = RunnerHelper.getWorkbenchDisplay();
			if (display == null) {
				return "";
			}
			if (dialogOpen.compareAndSet(false, true)) {
				display.syncExec(() -> {
					final AuthDetailsPromptDialog dialog = new AuthDetailsPromptDialog(display.getActiveShell());
					dialog.setBlockOnOpen(true);
					if (dialog.open() == Window.OK) {
						UpstreamUrlProvider.this.username = dialog.getUsername();
						UpstreamUrlProvider.this.password = new String(dialog.getPassword());
					}
					hasDetails = true;
				});
				// Fire change listener without further blocking this call
				ForkJoinPool.commonPool().submit(() -> fireChangedListeners());
			}
		}
		if (!hasDetails) {
			return "";
		}

		return url;
	}

	public String getUsername() {
		return username;// preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
	}

	public String getPassword() {
		return password;// preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY);
	}

	public void registerDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		listeners.add(listener);
	}

	public void deregisterDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		listeners.remove(listener);
	}

	public boolean isAvailable() {
		final String url = getBaseURL();
		if (url == null || url.isEmpty()) {
			return false;
		}

		if (!url.startsWith("http")) {
			return false;
		}
		return true;
	}

	private void fireChangedListeners() {
		for (final IUpstreamDetailChangedListener l : listeners) {
			try {
				l.changed();
			} catch (final Exception e) {

			}
		}
	}
}
