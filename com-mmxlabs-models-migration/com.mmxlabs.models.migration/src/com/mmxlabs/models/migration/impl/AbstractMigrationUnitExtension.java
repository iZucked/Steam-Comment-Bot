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

/**
 * Abstract class handling the wrapping calls to a {@link IMigrationUnit}. The {@link #migrate(java.util.List, org.eclipse.emf.ecore.resource.URIConverter, java.util.Map)} still needs to be
 * implemented.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractMigrationUnitExtension implements IMigrationUnitExtension {
	private IMigrationUnit migrationUnit;

	public AbstractMigrationUnitExtension(@Nullable final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
	}

	public AbstractMigrationUnitExtension() {
	}

	@Override
	public abstract void migrate(@NonNull URI uri, @Nullable Map<URI, PackageData> extraPackages) throws Exception;

	@Override
	public IMigrationUnit getMigrationUnit() {
		return migrationUnit;
	}

	@Override
	public void setMigrationUnit(final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
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
