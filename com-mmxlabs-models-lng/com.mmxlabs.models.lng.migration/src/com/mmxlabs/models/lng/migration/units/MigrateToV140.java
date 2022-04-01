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

public class MigrateToV140 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 139;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 140;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");

		for (final EObjectWrapper vessel : vessels) {
			vessel.setAttrib("mmxReference", Boolean.FALSE);
			final EObjectWrapper referenceVessel = vessel.getRef("reference");
			if (referenceVessel != null && referenceVessel.getRef("reference") == null) {
				referenceVessel.setAttrib("referenceVessel", Boolean.TRUE);
			}
		}
	}
}