/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;

public class MigrateToV78 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 77;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 78;
	}

	@Override
	protected void doMigration(final @NonNull MigrationModelRecord modelRecord) {

	}
}
