/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV121 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 120;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 121;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		
		final List<EObjectWrapper> settleStrategies = pricingModel.getRefAsList("settleStrategies");
		if (settleStrategies != null) {
			for (EObjectWrapper settleStrategy : settleStrategies) {
				settleStrategy.unsetFeature("useCalendarMonth");
			}
		}
	}
}
