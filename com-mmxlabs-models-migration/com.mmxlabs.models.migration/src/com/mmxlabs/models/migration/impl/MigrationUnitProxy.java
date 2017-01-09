/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;

/**
 * A proxy class to wrap around a {@link IMigrationUnit} and lazy instantiate the class when required.
 * 
 * @author Simon Goodall
 * 
 */
class MigrationUnitProxy implements IMigrationUnit {
	private final MigrationUnitExtensionPoint ext;

	private final int sourceVersion;
	private final int destinationVersion;

	public MigrationUnitProxy(@NonNull final MigrationUnitExtensionPoint ext) {
		this.ext = ext;
		sourceVersion = Integer.parseInt(ext.getFrom());
		destinationVersion = Integer.parseInt(ext.getTo());
	}

	@Override
	public String getScenarioContext() {
		return ext.getContext();
	}

	@Override
	public int getScenarioSourceVersion() {
		return sourceVersion;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return destinationVersion;
	}

	@Override
	public void migrate(final @NonNull URI uri, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {
		final IMigrationUnit unit = ext.createMigrationUnit();
		if (unit == null) {
			throw new NullPointerException("Unable to create migration unit instance");
		}
		unit.migrate(uri, extraPackages);
	}

}
