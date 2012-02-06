package com.mmxlabs.models.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderFactoryRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	public static final String REFERENCE_VALUE_PROVIDER_FACTORY_EXTENSION_POINT = "com.mmxlabs.models.ui.valueproviders";

	private final ReferenceValueProviderFactoryRegistry referenceValueProviderFactoryRegistry
		= new ReferenceValueProviderFactoryRegistry(REFERENCE_VALUE_PROVIDER_FACTORY_EXTENSION_POINT);
	
	private final EditorFactoryRegistry editorFactoryRegistry = new EditorFactoryRegistry();
	private final ComponentHelperRegistry componentHelperRegistry = new ComponentHelperRegistry();
	
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.ui"; //$NON-NLS-1$

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
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
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

	public EditorFactoryRegistry getEditorFactoryRegistry() {
		return editorFactoryRegistry;
	}

	public ReferenceValueProviderFactoryRegistry getReferenceValueProviderFactoryRegistry() {
		return referenceValueProviderFactoryRegistry;
	}

	public ComponentHelperRegistry getComponentHelperRegistry() {
		return componentHelperRegistry;
	}
}
