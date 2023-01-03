/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

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
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		// All existing settings would have been optimised with spot cargo markets enabled.
		final EObjectWrapper userSettings = model.getRef("userSettings");
		if (userSettings != null) {
			userSettings.setAttrib("withSpotCargoMarkets", Boolean.TRUE);
		}
	}
}
