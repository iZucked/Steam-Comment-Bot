package com.mmxlabs.shiplingo.platform.models.optimisation;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.analytics.ui.liveeval.IResourceEvaluator;
import com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers.StartOptimisationHandler;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.shiplingo.platform.models.optimisation"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerServiceTracker;

	private ServiceRegistration<IResourceEvaluator> evaluator;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		jobManagerServiceTracker = new ServiceTracker<IEclipseJobManager, IEclipseJobManager>(context, IEclipseJobManager.class.getName(), null);
		jobManagerServiceTracker.open();
		final StartOptimisationHandler handler = new StartOptimisationHandler(false);
		evaluator = context.registerService(IResourceEvaluator.class, new IResourceEvaluator() {
			@Override
			public void evaluate(final IResource resource) {
//				handler.evaluateResource(getJobManager(), resource);
			}
		}, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {

		// close the service tracker
		jobManagerServiceTracker.close();
		jobManagerServiceTracker = null;
		evaluator.unregister();

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

	public IEclipseJobManager getJobManager() {
		return jobManagerServiceTracker.getService();
	}

}
