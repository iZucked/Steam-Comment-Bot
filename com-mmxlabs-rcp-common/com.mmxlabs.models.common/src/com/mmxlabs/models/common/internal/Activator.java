/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.common.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.common.commandservice.IModelCommandProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.common"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Injector injector;

	@Inject
	private Iterable<IModelCommandProvider> commandProviderServices;

	@Inject
	private Iterable<IModelCommandProviderExtension> commandProvidersExtensions;

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

		injector = Guice.createInjector(new ModelCommandProviderModule(context));
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
		super.stop(context);
	}

	public static Activator getPlugin() {
		return plugin;
	}

	public List<IModelCommandProvider> getModelCommandProviders() {

		final List<IModelCommandProvider> providers = new ArrayList<IModelCommandProvider>();
		for (final IModelCommandProvider p : commandProviderServices) {
			providers.add(p);
		}
		for (final IModelCommandProviderExtension e : commandProvidersExtensions) {
			providers.add(new ModelCommandProviderExtensionProxy(e));
		}

		return providers;
	}
}
