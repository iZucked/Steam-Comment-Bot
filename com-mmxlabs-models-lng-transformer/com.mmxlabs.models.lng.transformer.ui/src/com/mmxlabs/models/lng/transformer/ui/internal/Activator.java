/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.internal;

import javax.inject.Inject;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.ops4j.peaberry.Export;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunnerConsoleCommand;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParameterModesExtensionModule;
import com.mmxlabs.models.ui.validation.ValidationPlugin;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends ValidationPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.transformer.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Inject
	private Export<IParameterModesRegistry> parameterModesRegistry;

	@Inject
	private IImporterRegistry importerRegistry;

	private HeadlessOptioniserRunnerConsoleCommand optioniserConsole = new HeadlessOptioniserRunnerConsoleCommand();

	private ServiceRegistration<CommandProvider> optioniserConsoleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
 
		// Bind our module together with the hooks to the eclipse registry to get plugin extensions.
		final Injector inj = Guice.createInjector(Peaberry.osgiModule(context, EclipseRegistry.eclipseRegistry()), new ParameterModesExtensionModule(), new ExtensionConfigurationModule(null));
		inj.injectMembers(this);

		optioniserConsoleService = context.registerService(CommandProvider.class, optioniserConsole, null);
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

	public IParameterModesRegistry getParameterModesRegistry() {
		return parameterModesRegistry.get();
	}

	public IImporterRegistry getImporterRegistry() {
		return importerRegistry;
	}

}
