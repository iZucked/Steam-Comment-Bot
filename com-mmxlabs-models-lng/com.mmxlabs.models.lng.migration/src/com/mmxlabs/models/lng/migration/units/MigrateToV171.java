/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV171 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 170;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 171;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper costModel = referenceModel.getRef("costModel");

		final List<EObjectWrapper> cooldownPrices = costModel.getRefAsList("cooldownCostsTmp");
		costModel.setRef("cooldownCosts", cooldownPrices);
		costModel.unsetFeature("cooldownCostsTmp");
	}
}