/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private final @NonNull Map<String, WeakReference<IScenarioService>> services = new HashMap<>();
	private final @NonNull Map<IScenarioService, String> serviceComponentIDs = new WeakHashMap<>();

	private ServiceListener serviceListener;

	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerTracker;
	private ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> scenarioServiceSelectionProviderTracker;

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

		jobManagerTracker = new ServiceTracker<>(context, IEclipseJobManager.class, null);
		jobManagerTracker.open();

		scenarioServiceSelectionProviderTracker = new ServiceTracker<>(context, IScenarioServiceSelectionProvider.class, null);
		scenarioServiceSelectionProviderTracker.open();

		serviceListener = new ServiceListener() {

			@Override
			public void serviceChanged(final ServiceEvent event) {
				final ServiceReference<?> serviceReference = event.getServiceReference();
				final IScenarioService service = (IScenarioService) context.getService(serviceReference);
				final String key = serviceReference.getProperty("component.id").toString();

				if (event.getType() == ServiceEvent.REGISTERED) {
					services.put(key, new WeakReference<IScenarioService>(service));
					serviceComponentIDs.put(service, key);
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					services.remove(key);
					serviceComponentIDs.remove(service);
				}
			}
		};

		context.addServiceListener(serviceListener, "(objectclass=" + IScenarioService.class.getCanonicalName() + ")");

		// Add existing services
		final Collection<ServiceReference<IScenarioService>> serviceReferences = context.getServiceReferences(IScenarioService.class,
				"(objectclass=" + IScenarioService.class.getCanonicalName() + ")");
		for (final ServiceReference<IScenarioService> ref : serviceReferences) {

			final ServiceEvent event = new ServiceEvent(ServiceEvent.REGISTERED, ref);
			serviceListener.serviceChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		context.removeServiceListener(serviceListener);
		services.clear();
		// scenarioServiceSelectionProvider.deselectAll(false);
		jobManagerTracker.close();
		scenarioServiceSelectionProviderTracker.close();
		plugin = null;
		super.stop(context);
	}

	public Map<String, WeakReference<IScenarioService>> getScenarioServices() {
		return services;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public IScenarioServiceSelectionProvider getScenarioServiceSelectionProvider() {
		return scenarioServiceSelectionProviderTracker.getService();
	}

	public IEclipseJobManager getEclipseJobManager() {
		return jobManagerTracker.getService();
	}

	/**
	 * @param service
	 * @return
	 */
	public String getServiceComponentID(final IScenarioService service) {
		return serviceComponentIDs.get(service);
	}

	public IScenarioService getServiceForComponentID(final String componentID) {
		final WeakReference<IScenarioService> ref = services.get(componentID);
		if (ref == null) {
			return null;
		}
		return ref.get();
	}
}
