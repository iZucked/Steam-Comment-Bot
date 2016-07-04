/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.extensions.ClientMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.ClientMigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.extensions.DefaultClientMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.impl.MigrationRegistry;

/**
 * A Guice {@link Module} to bind the extension points with the {@link MigrationRegistry} and export it as a service. This class is intended to be use by the {@link MigrationActivator}
 * 
 * @author Simon Goodall
 * 
 */
public class MigrationActivationModule extends AbstractModule {

	@Override
	protected void configure() {

		// Extension points
		bind(iterable(MigrationContextExtensionPoint.class)).toProvider(service(MigrationContextExtensionPoint.class).multiple());
		bind(iterable(MigrationUnitExtensionPoint.class)).toProvider(service(MigrationUnitExtensionPoint.class).multiple());
		bind(iterable(MigrationUnitExtensionExtensionPoint.class)).toProvider(service(MigrationUnitExtensionExtensionPoint.class).multiple());
		bind(iterable(DefaultMigrationContextExtensionPoint.class)).toProvider(service(DefaultMigrationContextExtensionPoint.class).multiple());

		bind(iterable(ClientMigrationContextExtensionPoint.class)).toProvider(service(ClientMigrationContextExtensionPoint.class).multiple());
		bind(iterable(ClientMigrationUnitExtensionPoint.class)).toProvider(service(ClientMigrationUnitExtensionPoint.class).multiple());
		bind(iterable(DefaultClientMigrationContextExtensionPoint.class)).toProvider(service(DefaultClientMigrationContextExtensionPoint.class).multiple());

		// Bind implementation as a singleton to our service interface
		bind(TypeLiterals.export(IMigrationRegistry.class)).toProvider(service(MigrationRegistry.class).export());
		bind(MigrationRegistry.class).in(Singleton.class);
	}
}
