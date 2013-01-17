/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.internal;

import javax.inject.Inject;

import org.ops4j.peaberry.Export;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.migration.IMigrationRegistry;

public class MigrationActivator implements BundleActivator {

	@Inject
	private Export<IMigrationRegistry> migrationRegistry;

	public void start(final BundleContext bc) throws Exception {
		// Bind our module together with the hooks to the eclipse registry to get plugin extensions.
		final Injector inj = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new MigrationActivationModule());
		inj.injectMembers(this);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		migrationRegistry.unput();
		migrationRegistry = null;
	}
}
