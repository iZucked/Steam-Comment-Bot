package com.mmxlabs.models.lng.analytics.transformer;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.models.lng.analytics.evaluation.IEvaluationService;
import com.mmxlabs.models.lng.analytics.evaluation.impl.EvaluationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.analytics.transformer"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration<IEvaluationService> evaluationServiceRegistration;
	
	/**
	 * The constructor
	 */
	public Activator() {
		
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		evaluationServiceRegistration = context.registerService(IEvaluationService.class, new EvaluationService(), null);
	}

	public void stop(BundleContext context) throws Exception {
		evaluationServiceRegistration.unregister();
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
