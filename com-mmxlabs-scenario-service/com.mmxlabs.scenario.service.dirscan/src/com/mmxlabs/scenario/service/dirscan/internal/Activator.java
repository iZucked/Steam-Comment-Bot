/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.internal;

import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.dirscan.DirScanScenarioService;
import com.mmxlabs.scenario.service.dirscan.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static final Logger log = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.dirscan"; //$NON-NLS-1$

	private static final String[] prefixes = { "", "option2.", "option3." };
	private static final int SERVICE_COUNT = prefixes.length;

	// The shared instance
	private static Activator plugin;

	private IPropertyChangeListener propertyChangeListener;

	@SuppressWarnings("unchecked")
	private final ServiceRegistration<IScenarioService>[] dirScanRegistration = new ServiceRegistration[prefixes.length];

	private final DirScanScenarioService[] dirScanService = new DirScanScenarioService[prefixes.length];

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

		propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				try {
					for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
						String property = event.getProperty();
						if (property.equals(prefixes[idx] + PreferenceConstants.P_ENABLED_KEY)) {
							Object newValue = event.getNewValue();
							if (Boolean.TRUE.equals(newValue)) {
								enableService(idx);
							} else {
								disableService(idx);
							}
						} else {
							updateService(idx);
						}
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
		final String preferencePrefix = prefixes[idx];
		final String enabled = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_ENABLED_KEY);
		if ("true".equalsIgnoreCase(enabled)) {

			final String serviceName = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_NAME_KEY);
			final String path = getPreferenceStore().getString(preferencePrefix + PreferenceConstants.P_PATH_KEY);
			if (path == null || path.isEmpty()) {
				return;
			}

			final Hashtable<String, String> props = new Hashtable<String, String>();
			props.put(PreferenceConstants.P_NAME_KEY, serviceName);
			props.put(PreferenceConstants.P_PATH_KEY, path);
			// used internally
			props.put("component.id", preferencePrefix + serviceName);

			try {
				dirScanService[idx] = new DirScanScenarioService(serviceName);
				// Various ways of passing properties which do not work...
				dirScanRegistration[idx] = getBundle().getBundleContext().registerService(IScenarioService.class, dirScanService[idx], props);
				dirScanRegistration[idx].setProperties(props);
				// .. the way I've found to make it work...
				dirScanService[idx].start(props);
			} catch (final IOException e) {
				log.error("Error starting DirScan Scenario Service: " + e.getMessage(), e);
			}
		}

	}

	private void disableService(final int idx) {
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

		for (int idx = 0; idx < SERVICE_COUNT; ++idx) {
			try {
				disableService(idx);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
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
