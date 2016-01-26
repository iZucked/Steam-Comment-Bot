/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionExtensionPoint;

/**
 * A proxy class to wrap around a {@link IMigrationUnitExtension} and lazy instantiate the class when required.
 * 
 * @author Simon Goodall
 * 
 */
class MigrationUnitExtensionProxy implements IMigrationUnitExtension {
	private final MigrationUnitExtensionExtensionPoint ext;
	private IMigrationUnitExtension unitExtension;
	private IMigrationUnit migrationUnit;

	public MigrationUnitExtensionProxy(@NonNull final MigrationUnitExtensionExtensionPoint ext) {
		this.ext = ext;
	}

	@Override
	public void migrate(final @NonNull URI uri, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {
		if (unitExtension == null) {
			unitExtension = ext.createMigrationUnitExtension();
			unitExtension.setMigrationUnit(migrationUnit);
		}
		unitExtension.migrate(uri, extraPackages);
	}

	@Override
	public IMigrationUnit getMigrationUnit() {
		return migrationUnit;
	}

	@Override
	public void setMigrationUnit(final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
		if (unitExtension != null) {
			unitExtension.setMigrationUnit(migrationUnit);
		}
	}

	@Override
	public String getScenarioContext() {
		return migrationUnit.getScenarioContext();
	}

	@Override
	public int getScenarioSourceVersion() {
		return migrationUnit.getScenarioSourceVersion();
	}

	@Override
	public int getScenarioDestinationVersion() {
		return migrationUnit.getScenarioDestinationVersion();
	}
}
