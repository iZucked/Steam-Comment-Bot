/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.internal;

import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.dirscan.DirScanScenarioService;
import com.mmxlabs.scenario.service.dirscan.preferences.PreferenceConstants;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	private static final Logger log = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.dirscan"; //$NON-NLS-1$

	// First prefix empty for backwards compatibility.
	private static final String[] prefixes = { "", "option2.", "option3." };
	private static final int SERVICE_COUNT = prefixes.length;

	// The shared instance
	private static Activator plugin;

	private IPropertyChangeListener propertyChangeListener;

	@SuppressWarnings("unchecked")
	private final ServiceRegistration<IScenarioService>[] dirScanRegistration = new ServiceRegistration[prefixes.length];

	private final DirScanScenarioService[] dirScanService = new DirScanScenarioService[prefixes.length];
	private final boolean[] serviceEnabled = new boolean[prefixes.length];

	private ServiceTracker<IScenarioCipherProvider, IScenarioCipherProvider> scenarioCipherProviderTracker = null;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		scenarioCipherProviderTracker = new ServiceTracker<IScenarioCipherProvider, IScenarioCipherProvider>(context, IScenarioCipherProvider.class, null) {
			@Override
			public IScenarioCipherProvider addingService(final ServiceReference<IScenarioCipherProvider> reference) {

				final IScenarioCipherProvider provider = super.addingService(reference);

				for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
					try {
						final DirScanScenarioService service = dirScanService[idx];
						if (service != null) {
							service.setScenarioCipherProvider(provider);
						}
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
					}
				}

				return provider;
			}

			@Override
			public void removedService(final ServiceReference<IScenarioCipherProvider> reference, final IScenarioCipherProvider provider) {
				for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
					try {
						final DirScanScenarioService service = dirScanService[idx];
						if (service != null) {
							final IScenarioCipherProvider scenarioCipherProvider = service.getScenarioCipherProvider();
							if (scenarioCipherProvider == provider) {
								service.setScenarioCipherProvider(null);
							}
						}
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
					}
				}
				super.removedService(reference, provider);
			}
		};
		scenarioCipherProviderTracker.open();

		propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				try {
					final String property = event.getProperty();
					int service_idx = 0;
					for (int idx = 1; idx < SERVICE_COUNT; ++idx) {
						if (property.startsWith(prefixes[idx])) {
							service_idx = idx;
						}
					}
					if (property.equals(prefixes[service_idx] + PreferenceConstants.P_ENABLED_KEY)) {
						final Object newValue = event.getNewValue();
						if (Boolean.TRUE.equals(newValue)) {
							enableService(service_idx);
						} else {
							disableService(service_idx);
						}
					} else {
						updateService(service_idx);
					}
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		};
		getPreferenceStore().addPropertyChangeListener(propertyChangeListener);

		// Attempt to start service immediately
		for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
			try {
				enableService(idx);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private void enableService(final int idx) {

		if (dirScanService[idx] != null || dirScanRegistration[idx] != null) {
			disableService(idx);
		}

		final String preferencePrefix = prefixes[idx];
		final String enabled = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_ENABLED_KEY);
		if ("true".equalsIgnoreCase(enabled)) {
			this.serviceEnabled[idx] = true;

			final String serviceName = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_NAME_KEY);
			if (serviceName == null || serviceName.isEmpty()) {
				return;
			}
			final String path = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_PATH_KEY);
			if (path == null || path.isEmpty()) {
				return;
			}

			final Hashtable<String, String> props = new Hashtable<>();
			props.put(PreferenceConstants.P_NAME_KEY, serviceName);
			props.put(PreferenceConstants.P_PATH_KEY, path);
			// used internally by eclipse/OSGi
			props.put("component.id", preferencePrefix + serviceName);

			try {
				final DirScanScenarioService service = new DirScanScenarioService(serviceName);

				if (scenarioCipherProviderTracker != null) {
					service.setScenarioCipherProvider(scenarioCipherProviderTracker.getService());
				}

				service.start(props);
				dirScanService[idx] = service;
				dirScanRegistration[idx] = getBundle().getBundleContext().registerService(IScenarioService.class, service, props);
			} catch (final IOException e) {
				log.error("Error starting DirScan Scenario Service: " + e.getMessage(), e);
			}
		}

	}

	private void disableService(final int idx) {
		this.serviceEnabled[idx] = false;

		if (dirScanRegistration[idx] != null) {
			dirScanRegistration[idx].unregister();
			dirScanRegistration[idx] = null;
		}
		if (dirScanService[idx] != null) {
			dirScanService[idx].stop();
			dirScanService[idx] = null;
		}
	}

	private void updateService(final int idx) {
		if (dirScanRegistration[idx] != null) {
			disableService(idx);
		}
		if (this.serviceEnabled[idx]) {
			enableService(idx);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		getPreferenceStore().removePropertyChangeListener(propertyChangeListener);
		for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
			try {
				disableService(idx);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		if (scenarioCipherProviderTracker != null) {
			scenarioCipherProviderTracker.close();
			scenarioCipherProviderTracker = null;
		}

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
