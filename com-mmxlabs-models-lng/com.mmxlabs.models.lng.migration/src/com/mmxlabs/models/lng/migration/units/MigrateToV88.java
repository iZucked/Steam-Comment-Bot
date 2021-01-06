/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV88 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 87;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 88;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");

		// Set new data version if now already set - the CSV import did not set these correctly.
		
		setNewVersion(portModel, "portDataVersion");
		setNewVersion(portModel, "distanceDataVersion");

		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		setNewVersion(pricingModel, "marketCurveDataVersion");

		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		setNewVersion(fleetModel, "fleetDataVersion");
	}

	private void setNewVersion(EObjectWrapper object, String feature) {
		String existing = object.getAttrib(feature);
		if (existing == null || existing.isEmpty()) {
			object.setAttrib(feature, "private-" + EcoreUtil.generateUUID());
		}
	}
}
