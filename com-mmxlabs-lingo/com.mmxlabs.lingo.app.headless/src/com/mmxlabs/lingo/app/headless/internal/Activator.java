/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.internal;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.migration.IMigrationRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lingo.app.headless"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Inject
	private IMigrationRegistry migrationRegistry;

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

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				install(osgiModule(context, eclipseRegistry()));
				bind(IMigrationRegistry.class).toProvider(service(IMigrationRegistry.class).single());

			}

		});
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

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public IMigrationRegistry getMigrationRegistry() {
		return migrationRegistry;
	}

}
