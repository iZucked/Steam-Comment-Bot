package com.mmxlabs.models.ui;

import javax.inject.Inject;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.ui.extensions.ExtensionConfigurationModule;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IDisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.IEditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.ui"; //$NON-NLS-1$

	@Inject IEditorFactoryRegistry editorFactoryRegistry;
	@Inject IDisplayCompositeFactoryRegistry displayCompositeFactoryRegistry;
	@Inject IComponentHelperRegistry componentHelperRegistry;
	@Inject IReferenceValueProviderFactoryRegistry referenceValueProviderFactoryRegistry;
	@Inject IJointModelEditorContributionRegistry jointModelEditorContributionRegistry;
	@Inject IModelFactoryRegistry modelFactoryRegistry;
	
	// The shared instance
	private static Activator plugin;
	
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
		
		final Injector injector = Guice.createInjector(new ExtensionConfigurationModule(this));
		injector.injectMembers(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
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
}
