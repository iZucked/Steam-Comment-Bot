/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV84 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 83;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 84;
	}

	@Override
	protected void doMigration(@NonNull MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		if (fleetModel == null) {
			return;
		}

		final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
		if (vessels != null) {
			vessels.forEach(vc -> {
				if (vc.isSetFeature("baseFuel")) {
					EObjectWrapper bf = vc.getRef("baseFuel");
					vc.setRef("inPortBaseFuel", bf);
					vc.setRef("pilotLightBaseFuel", bf);
					vc.setRef("idleBaseFuel", bf);
				}
			});
		}

		// TODO: Update schedule models with changes

	}
}
