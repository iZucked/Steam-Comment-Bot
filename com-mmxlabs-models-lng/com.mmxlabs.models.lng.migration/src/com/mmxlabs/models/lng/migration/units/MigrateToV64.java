/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV64 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 63;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 64;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
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
