/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.extensions.ClientMigrationUnitExtensionPoint;

/**
 * A proxy class to wrap around a {@link IMigrationUnit} and lazy instantiate the class when required.
 * 
 * @author Simon Goodall
 * 
 */
class ClientMigrationUnitProxy implements IClientMigrationUnit {
	private final ClientMigrationUnitExtensionPoint ext;

	private final int sourceVersion;
	private final int destinationVersion;
	private final int scenarioVersion;

	public ClientMigrationUnitProxy(@NonNull final ClientMigrationUnitExtensionPoint ext) {
		this.ext = ext;
		scenarioVersion = Integer.parseInt(ext.getScenarioVersion());
		sourceVersion = Integer.parseInt(ext.getClientFrom());
		destinationVersion = Integer.parseInt(ext.getClientTo());
	}

	@Override
	public String getScenarioContext() {
		return ext.getScenarioContext();
	}

	@Override
	public String getClientContext() {
		return ext.getClientContext();
	}

	@Override
	public int getScenarioSourceVersion() {
		return scenarioVersion;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return scenarioVersion;
	}

	@Override
	public int getClientSourceVersion() {
		return sourceVersion;
	}

	@Override
	public int getClientDestinationVersion() {
		return destinationVersion;
	}

	@Override
	public void migrate(final @NonNull URI uri, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {
		final IClientMigrationUnit unit = ext.createClientMigrationUnit();
		if (unit == null) {
			throw new NullPointerException("Unable to create migration unit instance");
		}
		unit.migrate(uri, extraPackages);
	}
}
