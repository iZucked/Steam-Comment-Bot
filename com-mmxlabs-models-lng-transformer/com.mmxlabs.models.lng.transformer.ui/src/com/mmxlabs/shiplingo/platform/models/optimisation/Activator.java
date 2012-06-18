package com.mmxlabs.shiplingo.platform.models.optimisation;

import java.util.Collection;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants2;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.shiplingo.platform.models.optimisation"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerServiceTracker;
	private ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> scenarioServiceSelectionProviderTracker;

	private final IScenarioServiceSelectionChangedListener scenarioServiceSelectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected) {

			setJobProperty(selected, true);
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected) {
			setJobProperty(deselected, false);
		}

		private void setJobProperty(final Collection<ScenarioInstance> selected, final boolean showInTaskbar) {
			final IEclipseJobManager jobManager = jobManagerServiceTracker.getService();
			if (jobManager != null) {

				for (final ScenarioInstance instance : selected) {
					final Object object = instance.getInstance();
					if (object instanceof MMXRootObject) {
						final MMXRootObject root = (MMXRootObject) object;

						final String uuid = instance.getUuid();

						final IJobDescriptor job = jobManager.findJobForResource(uuid);
						if (job == null) {
							continue;
						}

						final IJobControl control = jobManager.getControlForJob(job);
						if (control instanceof AbstractEclipseJobControl) {
							((AbstractEclipseJobControl) control).setJobProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, showInTaskbar);
						}
					}
				}
			}
		}
	};

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

		jobManagerServiceTracker = new ServiceTracker<IEclipseJobManager, IEclipseJobManager>(context, IEclipseJobManager.class.getName(), null);
		jobManagerServiceTracker.open();
		scenarioServiceSelectionProviderTracker = new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(context, IScenarioServiceSelectionProvider.class.getName(),
				null);
		scenarioServiceSelectionProviderTracker.open();

		{
			final IScenarioServiceSelectionProvider service = scenarioServiceSelectionProviderTracker.getService();
			if (service != null) {
				service.addSelectionChangedListener(scenarioServiceSelectionChangedListener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {

		{
			final IScenarioServiceSelectionProvider service = scenarioServiceSelectionProviderTracker.getService();
			if (service != null) {
				service.removeSelectionChangedListener(scenarioServiceSelectionChangedListener);
			}
		}

		// close the service tracker
		jobManagerServiceTracker.close();
		jobManagerServiceTracker = null;

		scenarioServiceSelectionProviderTracker.close();
		scenarioServiceSelectionProviderTracker = null;

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
