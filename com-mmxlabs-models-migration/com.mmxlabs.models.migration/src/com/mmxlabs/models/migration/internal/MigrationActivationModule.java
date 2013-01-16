package com.mmxlabs.models.migration.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.impl.MigrationRegistry;

public class MigrationActivationModule extends AbstractModule {

	@Override
	protected void configure() {

		// Extension points
		bind(iterable(MigrationContextExtensionPoint.class)).toProvider(service(MigrationContextExtensionPoint.class).multiple());
		bind(iterable(MigrationUnitExtensionPoint.class)).toProvider(service(MigrationUnitExtensionPoint.class).multiple());
		bind(iterable(DefaultMigrationContextExtensionPoint.class)).toProvider(service(DefaultMigrationContextExtensionPoint.class).multiple());

		// Bind implementation as a singleton to our service interface
		bind(TypeLiterals.export(IMigrationRegistry.class)).toProvider(service(MigrationRegistry.class).export());
		bind(MigrationRegistry.class).in(Singleton.class);
	}
}
