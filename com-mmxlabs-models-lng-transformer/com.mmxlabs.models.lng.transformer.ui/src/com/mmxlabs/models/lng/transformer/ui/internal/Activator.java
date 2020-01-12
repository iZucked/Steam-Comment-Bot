/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.internal;

import java.util.Collection;

import javax.inject.Inject;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.ui.progress.IProgressConstants2;
import org.ops4j.peaberry.Export;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunnerConsoleCommand;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadessOptimiserRunnerConsoleCommand;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParameterModesExtensionModule;
import com.mmxlabs.models.ui.validation.ValidationPlugin;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends ValidationPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.transformer.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerServiceTracker;
	private ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> scenarioServiceSelectionProviderTracker;

	@Inject
	private Export<IParameterModesRegistry> parameterModesRegistry;

	@Inject
	private IImporterRegistry importerRegistry;

	private final IScenarioServiceSelectionChangedListener scenarioServiceSelectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> selected, boolean block) {

			setJobProperty(selected, true);
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> deselected, boolean block) {
			setJobProperty(deselected, false);
		}

		private void setJobProperty(final Collection<ScenarioResult> selected, final boolean showInTaskbar) {
			final IEclipseJobManager jobManager = jobManagerServiceTracker.getService();
			if (jobManager != null) {

				for (final ScenarioResult result : selected) {
					if (result == null) {
						continue;
					}
					ScenarioModelRecord instance = result.getModelRecord();

					final String uuid = instance.getManifest().getUUID();

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

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioResult oldPin, final ScenarioResult newPin, boolean block) {
		}

		@Override
		public void selectionChanged(ScenarioResult pinned, Collection<ScenarioResult> others, boolean block) {

		}
	};

	/**
	 * The constructor
	 */
	public Activator() {
	}

	private HeadlessOptioniserRunnerConsoleCommand optioniserConsole = new HeadlessOptioniserRunnerConsoleCommand();
	private HeadessOptimiserRunnerConsoleCommand optimiserConsole = new HeadessOptimiserRunnerConsoleCommand();

	private ServiceRegistration<CommandProvider> optioniserConsoleService;
	private ServiceRegistration<CommandProvider> optimiserConsoleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		jobManagerServiceTracker = new ServiceTracker<>(context, IEclipseJobManager.class.getName(), null);
		jobManagerServiceTracker.open();
		scenarioServiceSelectionProviderTracker = new ServiceTracker<>(context, IScenarioServiceSelectionProvider.class.getName(), null);
		scenarioServiceSelectionProviderTracker.open();

		{
			final IScenarioServiceSelectionProvider service = scenarioServiceSelectionProviderTracker.getService();
			if (service != null) {
				service.addSelectionChangedListener(scenarioServiceSelectionChangedListener);
			}
		}

		// Bind our module together with the hooks to the eclipse registry to get plugin extensions.
		final Injector inj = Guice.createInjector(Peaberry.osgiModule(context, EclipseRegistry.eclipseRegistry()), new ParameterModesExtensionModule(), new ExtensionConfigurationModule(null));
		inj.injectMembers(this);

		 optioniserConsoleService = context.registerService(CommandProvider.class, optioniserConsole, null);
		 optimiserConsoleService = context.registerService(CommandProvider.class, optimiserConsole, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		if (optioniserConsoleService != null) {
			optioniserConsoleService.unregister();
			optioniserConsoleService = null;
		}
		if (optimiserConsoleService != null) {
			optimiserConsoleService.unregister();
			optimiserConsoleService = null;
		}
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

		parameterModesRegistry.unput();
		parameterModesRegistry = null;

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

	public IParameterModesRegistry getParameterModesRegistry() {
		return parameterModesRegistry.get();
	}

	public IImporterRegistry getImporterRegistry() {
		return importerRegistry;
	}

}
