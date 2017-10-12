/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

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
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		EObjectWrapper cargoModel = model.getRef("cargoModel");

		EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");
		if (canalBookings != null) {
			// Set default 5 idle for northbound voyages days
			canalBookings.setAttrib("northboundMaxIdleDays", 5);
		}
	}
}
