package com.mmxlabs.scenario.service.ui;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.scenario.service.IScenarioService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Map<String, WeakReference<IScenarioService>> services;

	private ServiceListener serviceListener;

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
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		serviceListener = new ServiceListener() {

			@Override
			public void serviceChanged(ServiceEvent event) {
				ServiceReference<?> serviceReference = event.getServiceReference();
				IScenarioService service = (IScenarioService) context.getService(serviceReference);
				String key = serviceReference.getProperty("component.id").toString();

				if (event.getType() == ServiceEvent.REGISTERED) {
					services.put(key, new WeakReference<IScenarioService>(service));
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					services.remove(key);
				}
			}
		};

		context.addServiceListener(serviceListener, "(objectclass=" + IScenarioService.class.getCanonicalName() + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		context.removeServiceListener(serviceListener);
		services.clear();

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

}
