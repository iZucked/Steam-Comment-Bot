/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.internal;

import java.util.Locale;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.ops4j.peaberry.Export;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParameterModesExtensionModule;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lingo.its"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Inject
	private IImporterRegistry importerRegistry;

	@Inject
	private IMigrationRegistry migrationRegistry;

	@Inject
	private Export<IParameterModesRegistry> parameterModesRegistry;

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
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// Bind our module together with the hooks to the eclipse registry to get plugin extensions.
		Injector injector = Guice.createInjector(new ExtensionConfigurationModule(getBundle().getBundleContext()), new ParameterModesExtensionModule());
		injector.injectMembers(this);

		// Enforce UK Locale Needed for running tests on build server. Keeps date format consistent.
		// Also in the AbstractOptimisationResultTester, but does not seem to always work...
		Locale.setDefault(Locale.UK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		parameterModesRegistry.unput();
		parameterModesRegistry = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public IImporterRegistry getImporterRegistry() {
		return importerRegistry;
	}

	public IMigrationRegistry getMigrationRegistry() {
		return migrationRegistry;
	}

	public IParameterModesRegistry getParameterModesRegistry() {
		return parameterModesRegistry.get();
	}
}
