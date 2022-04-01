/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.DataManifest;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.PackageData;

/**
 * Migration Unit delegating to Models LNG migration
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractMigrationWrapper implements IMigrationUnitExtension {

	private IMigrationUnit unit;

	@Override
	public String getScenarioContext() {
		return getMigrationUnit().getScenarioContext();
	}

	@Override
	public int getScenarioSourceVersion() {
		return getMigrationUnit().getScenarioSourceVersion();
	}

	@Override
	public int getScenarioDestinationVersion() {
		return getMigrationUnit().getScenarioDestinationVersion();
	}

	@Override
	public void migrate(@Nullable final Map<URI, PackageData> extraPackagesOrig, final @NonNull DataManifest dataManifest) throws Exception {
		final Map<URI, PackageData> extraPackages = new HashMap<>(extraPackagesOrig);

		populateExtraPackages(extraPackages);

		unit.migrate(extraPackages, dataManifest);
	}

	@Override
	public void setMigrationUnit(final IMigrationUnit unit) {
		this.unit = unit;
	}

	@Override
	public IMigrationUnit getMigrationUnit() {
		return unit;
	}

	protected abstract void populateExtraPackages(final @NonNull Map<URI, PackageData> extraPackages);
}
