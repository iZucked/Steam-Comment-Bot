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

public class MigrateToV70 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 69;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 70;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		if (cargoModel == null) {
			return;
		}
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				final EObjectWrapper startHeel = vesselAvailability.getRef("startHeelTmp");
				vesselAvailability.setRef("startHeel", startHeel);
				// Shouldn't really need this due to containership
				vesselAvailability.unsetFeature("startHeelTmp");

			}
		}

	}
}
