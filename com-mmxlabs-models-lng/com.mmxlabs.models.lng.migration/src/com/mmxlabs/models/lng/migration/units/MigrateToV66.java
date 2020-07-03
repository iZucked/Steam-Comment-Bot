/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV66 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 65;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 66;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper scheduleModel = model.getRef("scheduleModel");
		if (scheduleModel == null) {
			return;
		}
		final EObjectWrapper schedule = scheduleModel.getRef("schedule");
		if (schedule == null) {
			return;
		}
		final List<EObjectWrapper> cargoAllocations = schedule.getRefAsList("cargoAllocations");
		if (cargoAllocations != null) {
			cargoAllocations.forEach(ca -> ca.unsetFeature("inputCargo"));
		}
	}
}
