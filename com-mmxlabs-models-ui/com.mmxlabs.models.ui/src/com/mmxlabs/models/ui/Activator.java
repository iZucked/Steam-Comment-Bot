/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import javax.inject.Inject;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.ui.extensions.ExtensionConfigurationModule;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IDisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.IEditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;
import com.mmxlabs.models.ui.validation.IValidationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.ui"; //$NON-NLS-1$

	@Inject
	IEditorFactoryRegistry editorFactoryRegistry;
	@Inject
	IDisplayCompositeFactoryRegistry displayCompositeFactoryRegistry;
	@Inject
	IComponentHelperRegistry componentHelperRegistry;
	@Inject
	IReferenceValueProviderFactoryRegistry referenceValueProviderFactoryRegistry;
	@Inject
	IJointModelEditorContributionRegistry jointModelEditorContributionRegistry;
	@Inject
	IModelFactoryRegistry modelFactoryRegistry;

	// The shared instance
	private static Activator plugin;
	private ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker;
	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerTracker;
	private ServiceTracker<IValidationService, IValidationService> validationServiceTracker;

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

		commandProviderTracker = new ServiceTracker<IModelCommandProvider, IModelCommandProvider>(context, IModelCommandProvider.class, null);
		commandProviderTracker.open();

		jobManagerTracker = new ServiceTracker<IEclipseJobManager, IEclipseJobManager>(context, IEclipseJobManager.class.getName(), null);
		jobManagerTracker.open();

		validationServiceTracker = new ServiceTracker<IValidationService, IValidationService>(context, IValidationService.class.getName(), null);
		validationServiceTracker.open();

		final Injector injector = Guice.createInjector(new ExtensionConfigurationModule(this));
		injector.injectMembers(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		commandProviderTracker.close();
		commandProviderTracker = null;

		validationServiceTracker.close();
		validationServiceTracker = null;

		jobManagerTracker.close();
		jobManagerTracker = null;
		super.stop(context);
	}

	public IEclipseJobManager getJobManager() {
		return jobManagerTracker.getService();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public IEditorFactoryRegistry getEditorFactoryRegistry() {
		return editorFactoryRegistry;
	}

	public IDisplayCompositeFactoryRegistry getDisplayCompositeFactoryRegistry() {
		return displayCompositeFactoryRegistry;
	}

	public IComponentHelperRegistry getComponentHelperRegistry() {
		return componentHelperRegistry;
	}

	public IReferenceValueProviderFactoryRegistry getReferenceValueProviderFactoryRegistry() {
		return referenceValueProviderFactoryRegistry;
	}

	public IJointModelEditorContributionRegistry getJointModelEditorContributionRegistry() {
		return jointModelEditorContributionRegistry;
	}

	public IModelFactoryRegistry getModelFactoryRegistry() {
		return modelFactoryRegistry;
	}

	public ServiceTracker<IModelCommandProvider, IModelCommandProvider> getCommandProviderTracker() {
		return commandProviderTracker;
	}

	/**
	 */
	public IValidationService getValidationService() {
		return validationServiceTracker.getService();
	}
}
