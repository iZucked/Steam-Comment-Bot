/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IUpstreamServiceChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.IUpstreamServiceChangedListener.Service;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.BaseCaseScenarioService;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.SharedWorkspaceScenarioService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.ui.IScenarioVersionService;

public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.integration.ui.scenarios"; //$NON-NLS-1$

	static final String URL_PREFIX = "/ui";

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration<IBaseCaseVersionsProvider> baseCaseVersionsProviderServiceRegistration = null;
	private ServiceRegistration<IScenarioVersionService> scenarioVersionsServiceRegistration = null;
	private ServiceRegistration<IScenarioService> baseCaseServiceRegistration = null;
	private ServiceRegistration<IScenarioService> teamServiceRegistration = null;

	private BaseCaseVersionsProviderService baseCaseVersionsProviderService = null;
	private BaseCaseScenarioService baseCaseService = null;

	private ScenarioVersionsService scenarioVersionsService = null;

	private SharedWorkspaceScenarioService teamService = null;

	private ServiceTracker<IScenarioCipherProvider, IScenarioCipherProvider> scenarioCipherProviderTracker = null;

	private IUpstreamServiceChangedListener listener = this::updateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// Ensure something has triggered this class load.
		DataHubServiceProvider.getInstance();

		baseCaseVersionsProviderService = new BaseCaseVersionsProviderService();
		baseCaseVersionsProviderServiceRegistration = getBundle().getBundleContext().registerService(IBaseCaseVersionsProvider.class, getBaseCaseVersionsProviderService(), new Hashtable<>());

		scenarioVersionsService = new ScenarioVersionsService(getBaseCaseVersionsProviderService());
		scenarioVersionsServiceRegistration = getBundle().getBundleContext().registerService(IScenarioVersionService.class, scenarioVersionsService, new Hashtable<>());

		scenarioCipherProviderTracker = new ServiceTracker<IScenarioCipherProvider, IScenarioCipherProvider>(context, IScenarioCipherProvider.class, null) {
			@Override
			public IScenarioCipherProvider addingService(final ServiceReference<IScenarioCipherProvider> reference) {

				final IScenarioCipherProvider provider = super.addingService(reference);
				if (baseCaseService != null) {
					baseCaseService.setScenarioCipherProvider(provider);
				}
				if (teamService != null) {
					teamService.setScenarioCipherProvider(provider);
				}

				return provider;
			}

			@Override
			public void removedService(final ServiceReference<IScenarioCipherProvider> reference, final IScenarioCipherProvider provider) {
				if (baseCaseService != null) {
					baseCaseService.setScenarioCipherProvider(null);
				}
				if (teamService != null) {
					teamService.setScenarioCipherProvider(null);
				}
				super.removedService(reference, provider);
			}
		};
		scenarioCipherProviderTracker.open();

		UpstreamUrlProvider.INSTANCE.registerServiceChangedLister(listener);
		updateService(IUpstreamServiceChangedListener.Service.BaseCaseWorkspace);
		updateService(IUpstreamServiceChangedListener.Service.TeamWorkspace);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		UpstreamUrlProvider.INSTANCE.deregisterServiceChangedLister(listener);
		stopService(IUpstreamServiceChangedListener.Service.BaseCaseWorkspace);
		stopService(IUpstreamServiceChangedListener.Service.TeamWorkspace);

		if (scenarioCipherProviderTracker != null) {
			scenarioCipherProviderTracker.close();
			scenarioCipherProviderTracker = null;
		}

		if (scenarioVersionsServiceRegistration != null) {
			scenarioVersionsServiceRegistration.unregister();
			scenarioVersionsServiceRegistration = null;
		}

		if (baseCaseVersionsProviderServiceRegistration != null) {
			baseCaseVersionsProviderServiceRegistration.unregister();
			baseCaseVersionsProviderServiceRegistration = null;
		}

	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private void enableService(final IUpstreamServiceChangedListener.Service serviceType) {

		if (serviceType == Service.BaseCaseWorkspace) {
			if (baseCaseService != null || baseCaseServiceRegistration != null) {
				// Already enabled
				return;
			}
			baseCaseService = new BaseCaseScenarioService(getBaseCaseVersionsProviderService());

			if (scenarioCipherProviderTracker != null) {

				baseCaseService.setScenarioCipherProvider(scenarioCipherProviderTracker.getService());
			}

			try {
				baseCaseService.start();
			} catch (Exception e) {
				e.printStackTrace();
				baseCaseService.stop();
				baseCaseService = null;
				return;
			}

			final Hashtable<String, String> props = new Hashtable<>();
			// used internally by eclipse/OSGi
			props.put("component.id", baseCaseService.getSerivceID());

			baseCaseServiceRegistration = getBundle().getBundleContext().registerService(IScenarioService.class, baseCaseService, props);
		} else if (serviceType == Service.TeamWorkspace) {
			if (teamService != null || teamServiceRegistration != null) {
				// Already enabled
				return;
			}
			teamService = new SharedWorkspaceScenarioService();

			if (scenarioCipherProviderTracker != null) {
				teamService.setScenarioCipherProvider(scenarioCipherProviderTracker.getService());
			}

			try {
				teamService.start();
			} catch (IOException e) {
				e.printStackTrace();
				teamService.stop();
				teamService = null;
				return;
			}

			final Hashtable<String, String> props = new Hashtable<>();
			// used internally by eclipse/OSGi
			props.put("component.id", teamService.getSerivceID());

			teamServiceRegistration = getBundle().getBundleContext().registerService(IScenarioService.class, teamService, props);
		}

	}

	private void stopService(final IUpstreamServiceChangedListener.Service serviceType) {

		if (serviceType == Service.BaseCaseWorkspace) {
			if (baseCaseServiceRegistration != null) {
				baseCaseServiceRegistration.unregister();
				baseCaseServiceRegistration = null;
			}
			if (baseCaseService != null) {
				baseCaseService.stop();
				baseCaseService = null;
			}
		} else if (serviceType == Service.TeamWorkspace) {
			if (teamServiceRegistration != null) {
				teamServiceRegistration.unregister();
				teamServiceRegistration = null;
			}
			if (teamService != null) {
				teamService.stop();
				teamService = null;
			}
		}
	}

	private void updateService(final IUpstreamServiceChangedListener.Service serviceType) {
		if (serviceType == Service.BaseCaseWorkspace) {
			if (UpstreamUrlProvider.INSTANCE.isBaseCaseServiceEnabled()) {
				enableService(serviceType);
			} else {
				stopService(serviceType);
			}
		} else if (serviceType == Service.TeamWorkspace) {
			if (UpstreamUrlProvider.INSTANCE.isTeamServiceEnabled()) {
				enableService(serviceType);
			} else {
				stopService(serviceType);
			}
		}

	}

	public BaseCaseVersionsProviderService getBaseCaseVersionsProviderService() {
		return baseCaseVersionsProviderService;
	}
}
