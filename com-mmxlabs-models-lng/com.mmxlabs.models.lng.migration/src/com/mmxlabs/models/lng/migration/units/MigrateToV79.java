/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV79 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 78;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 79;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper cargoModel = model.getRef("cargoModel");

		final EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");
		if (canalBookings != null) {
			// Set default 5 idle for northbound voyages days
			canalBookings.setAttrib("northboundMaxIdleDays", 5);
		}
	}
}
