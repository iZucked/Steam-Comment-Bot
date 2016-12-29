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

public class MigrateToV63 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 62;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 63;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		final List<EObjectWrapper> availabilities = cargoModel.getRefAsList("vesselAvailabilities");

		// Consumer to update the set the fleet flag by default
		if (availabilities != null) {
			availabilities.forEach(va -> va.setAttrib("fleet", true));
		}

		// Move any existing analytics models.
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel != null) {
			final EObjectWrapper analysisModel = referenceModel.getRef("analyticsModel");
			if (analysisModel != null) {
				model.setRef("analyticsModel", analysisModel);
				// Should have been cleared by above call....
				referenceModel.unsetFeature("analyticsModel");
			}
		}
	}
}
