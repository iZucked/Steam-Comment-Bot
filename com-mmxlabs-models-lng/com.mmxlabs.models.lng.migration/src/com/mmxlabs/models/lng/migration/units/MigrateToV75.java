/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV75 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 74;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 75;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper costModel = referenceModel.getRef("costModel");
		if (costModel == null) {
			return;
		}

		EObjectWrapper panamaCanalTariff = costModel.getRef("panamaCanalTariff");
		if (panamaCanalTariff != null) {
			panamaCanalTariff.unsetFeature("availableFrom");
		}
	}
}
