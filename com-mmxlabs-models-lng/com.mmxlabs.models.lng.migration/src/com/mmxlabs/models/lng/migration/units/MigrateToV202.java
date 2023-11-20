/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV202 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 201;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 202;
	}

	// Updates to panama seasonality
	@Override
	protected void doMigration(final @NonNull MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");

		final List<EObjectWrapper> seasonalityRecords = canalBookings.getRefAsList("panamaSeasonalityRecords");
		for (final EObjectWrapper seasonalityRecord : seasonalityRecords) {
			if (seasonalityRecord.isSetFeature("startYear")) {
				final int startYear = seasonalityRecord.getAttribAsInt("startYear");
				if (startYear == 0) {
					seasonalityRecord.unsetFeature("startYear");
				}
			}
			final int startDay = seasonalityRecord.getAttribAsInt("startDay");
			if (startDay == 0) {
				seasonalityRecord.setAttrib("startDay", 1);
				seasonalityRecord.setAttrib("startMonth", 1);
				if (seasonalityRecord.isSetFeature("startYear")) {
					seasonalityRecord.unsetFeature("startYear");
				}

			}
			final int startMonth = seasonalityRecord.getAttribAsInt("startMonth");
			if (startMonth == 0) {
				seasonalityRecord.setAttrib("startDay", 1);
				seasonalityRecord.setAttrib("startMonth", 1);
				if (seasonalityRecord.isSetFeature("startYear")) {
					seasonalityRecord.unsetFeature("startYear");
				}
			}
		}
	}

}
