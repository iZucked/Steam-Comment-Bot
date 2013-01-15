package com.mmxlabs.models.migration.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.impl.MigrationRegistry;

public class MigrationActivationModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		// Extension points
		bind(iterable(DefaultMigrationContextExtensionPoint.class)).toProvider(service(DefaultMigrationContextExtensionPoint.class).multiple());
		bind(iterable(MigrationContextExtensionPoint.class)).toProvider(service(MigrationContextExtensionPoint.class).multiple());
		bind(iterable(MigrationUnitExtensionPoint.class)).toProvider(service(MigrationUnitExtensionPoint.class).multiple());

		bindService(MigrationRegistry.class).export();
	}
}
