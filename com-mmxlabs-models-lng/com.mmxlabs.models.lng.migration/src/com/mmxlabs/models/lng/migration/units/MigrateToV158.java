/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV158 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 157;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 158;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		final EClass clsBuyOpportunity = MetamodelUtils.getEClass(analyticsPackage, "BuyOpportunity");
		final EClass clsSellOpportunity = MetamodelUtils.getEClass(analyticsPackage, "SellOpportunity");

		final EEnum volumeModeEnum = MetamodelUtils.getEEnum(analyticsPackage, "VolumeMode");
		final EEnumLiteral fixedLiteral = MetamodelUtils.getEEnum_Literal(volumeModeEnum, "FIXED");

		final EObjectWrapper analysisModel = scenarioModel.getRef("analyticsModel");
		final List<EObjectWrapper> sandboxes = analysisModel.getRefAsList("optionModels");
		if (sandboxes != null) {
			for (final EObjectWrapper sandbox : sandboxes) {
				
				// Optimise or Optionise mode
				if (sandbox.getAttribAsInt("mode") == 1 || sandbox.getAttribAsInt("mode") == 2) {
					// make sure portfolio link is set
					sandbox.getRef("baseCase").setAttrib("keepExistingScenario", Boolean.TRUE);
					sandbox.getRef("partialCase").setAttrib("keepExistingScenario", Boolean.TRUE);
				}
				
				{
					final List<EObjectWrapper> options = sandbox.getRefAsList("buys");
					if (options != null) {
						for (final EObjectWrapper option : options) {
							if (clsBuyOpportunity.isInstance(option) && option.getAttrib("volumeMode") == fixedLiteral) {
								option.setAttrib("minVolume", option.getAttrib("maxVolume"));
							}
						}
					}
				}
				{
					final List<EObjectWrapper> options = sandbox.getRefAsList("sells");
					if (options != null) {
						for (final EObjectWrapper option : options) {
							if (clsSellOpportunity.isInstance(option) && option.getAttrib("volumeMode") == fixedLiteral) {
								option.setAttrib("minVolume", option.getAttrib("maxVolume"));
							}
						}
					}
				}
			}
		}

	}
}