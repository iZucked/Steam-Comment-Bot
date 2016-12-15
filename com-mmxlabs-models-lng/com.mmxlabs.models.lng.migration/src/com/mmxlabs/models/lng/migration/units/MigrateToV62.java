/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV62 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 61;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 62;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		final List<EObjectWrapper> availabilities = cargoModel.getRefAsList("vesselAvailabilities");

		// Consumer to update the set the fleet flag by default
		final Consumer<EObjectWrapper> availabilityUpdater = (va) -> {
			va.setAttrib("fleet", true);
		};
		if (availabilities != null) {
			availabilities.forEach(availabilityUpdater);
		}

		// All existing settings would have been optimised with spot cargo markets enabled.
		final EObjectWrapper userSettings = model.getRef("userSettings");
		if (userSettings != null) {
			userSettings.setAttrib("withSpotCargoMarkets", Boolean.TRUE);
		}

	}
}
