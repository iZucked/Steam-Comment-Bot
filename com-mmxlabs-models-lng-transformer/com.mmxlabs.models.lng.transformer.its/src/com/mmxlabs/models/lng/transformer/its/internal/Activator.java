/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.ops4j.peaberry.Export;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerITSOptimiserInjectorService;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParameterModesExtensionModule;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.transformer.its"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration<?> reg;

	@Inject
	private IImporterRegistry importerRegistry;

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

		reg = context.registerService(new String[] { IOptimiserInjectorService.class.getCanonicalName() }, new TransformerITSOptimiserInjectorService(), null);
		// Bind our module together with the hooks to the eclipse registry to get plugin extensions.
		final Injector inj = Guice.createInjector(Peaberry.osgiModule(context, EclipseRegistry.eclipseRegistry()), new ParameterModesExtensionModule(), new ExtensionConfigurationModule(null));
		inj.injectMembers(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		reg.unregister();
		parameterModesRegistry.unput();
		parameterModesRegistry = null;

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

	public IImporterRegistry getImporterRegistry() {
		return importerRegistry;
	}

	public IParameterModesRegistry getParameterModesRegistry() {
		return parameterModesRegistry.get();
	}

}
