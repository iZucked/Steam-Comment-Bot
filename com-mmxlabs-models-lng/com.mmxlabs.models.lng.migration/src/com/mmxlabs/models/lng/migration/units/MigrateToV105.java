/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV105 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 104;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 105;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");
		spotMarketsModel.unsetFeature("charterOutStartDate");
	}
}
