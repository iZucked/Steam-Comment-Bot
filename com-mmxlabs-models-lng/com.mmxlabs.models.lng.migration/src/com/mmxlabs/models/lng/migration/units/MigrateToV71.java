/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV71 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 70;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 71;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		if (cargoModel == null) {
			return;
		}
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				List<EObjectWrapper> startPorts = vesselAvailability.getRefAsList("startAt");
				if (startPorts != null) {
					if (startPorts.size() == 1) {
						EObjectWrapper startPort = startPorts.get(0);
						vesselAvailability.setRef("startAtTmp", startPort);
					}
					vesselAvailability.unsetFeature("startAt");
				}
			}
		}
	}
}
