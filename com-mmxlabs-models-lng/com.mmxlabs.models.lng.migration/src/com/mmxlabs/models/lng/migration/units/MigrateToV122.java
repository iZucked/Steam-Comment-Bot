/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV122 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 121;
	}
	@Override
	public int getScenarioDestinationVersion() {
		return 122;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		// Main user settings
		{
			final EObjectWrapper userSettings = scenarioModel.getRef("userSettings");
			updateUserSettings(modelRecord, userSettings);
		}
		
		// Main schedule model settings
		{
			final EPackage schedulePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
			final EPackage parametersPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

			final EClass classScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
			final EClass classUserSettings = MetamodelUtils.getEClass(parametersPackage, "UserSettings");

			final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");

			final TreeIterator<EObject> itr = analyticsModel.eAllContents();
			while (itr.hasNext()) {
				final EObject obj = itr.next();
				if (classScheduleModel.isInstance(obj)) {
					itr.prune();
				}
				if (classUserSettings.isInstance(obj)) {
					updateUserSettings(modelRecord, (EObjectWrapper) obj);
					itr.prune();
				}
			}
		}
	}

	private void updateUserSettings(@NonNull final MigrationModelRecord modelRecord, final EObjectWrapper userSettings) {

		if (userSettings == null) {
			return;
		}

		final EPackage parametersPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		final EEnum optimisationModeEnum = MetamodelUtils.getEEnum(parametersPackage, "OptimisationMode");

		if (userSettings.getAttribAsBoolean("adpOptimisation")) {
			final EEnumLiteral mode_ADP = MetamodelUtils.getEEnum_Literal(optimisationModeEnum, "ADP");
			userSettings.setAttrib("mode", mode_ADP);
		} else {
			final EEnumLiteral mode_SHORT_TERM = MetamodelUtils.getEEnum_Literal(optimisationModeEnum, "SHORT_TERM");
			userSettings.setAttrib("mode", mode_SHORT_TERM);
		}
		userSettings.setAttrib("cleanSlateOptimisation", userSettings.getAttribAsBoolean("cleanStateOptimisation"));
		userSettings.setAttrib("nominalOnly", userSettings.getAttribAsBoolean("nominalADP"));

		userSettings.unsetFeature("adpOptimisation");
		userSettings.unsetFeature("cleanStateOptimisation");
		userSettings.unsetFeature("nominalADP");
	}

}