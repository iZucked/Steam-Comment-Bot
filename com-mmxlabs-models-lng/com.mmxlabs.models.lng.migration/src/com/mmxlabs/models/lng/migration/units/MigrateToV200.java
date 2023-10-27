/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.parameters.AdpOptimisationMode;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV200 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 199;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 200;
	}

	private void updateCleanSlateRefs(EObjectWrapper obj, EPackage parametersPackage) {
		final String cleanSlateFeature = "cleanSlateOptimisation";
		final String adpOptModeFeature = "adpOptimisationMode";
		boolean isCleanSlate = obj.getAttribAsBoolean(cleanSlateFeature);
		final EEnum enumAdpOptMode = MetamodelUtils.getEEnum(parametersPackage, "AdpOptimisationMode");

		final EEnumLiteral cleanSlate = MetamodelUtils.getEEnum_Literal(enumAdpOptMode, AdpOptimisationMode.CLEAN_SLATE.getLiteral());
		final EEnumLiteral partialCleanSlate = MetamodelUtils.getEEnum_Literal(enumAdpOptMode, AdpOptimisationMode.PARTIAL_CLEAN_SLATE.getLiteral());
		obj.unsetFeature(cleanSlateFeature);
		obj.setAttrib(adpOptModeFeature, isCleanSlate ? cleanSlate : partialCleanSlate);
	}

	// ADP partial clean slate mode
	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EPackage parametersPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);
		final EPackage schedulePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		EObjectWrapper userSettings = scenarioModel.getRef("userSettings");
		final EClass classUserSettings = MetamodelUtils.getEClass(parametersPackage, "UserSettings");
		final EClass classSchedule = MetamodelUtils.getEClass(schedulePackage, "Schedule");

		updateCleanSlateRefs(userSettings, parametersPackage);

		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");

		final TreeIterator<EObject> itr = analyticsModel.eAllContents();
		while (itr.hasNext()) {
			final EObject obj = itr.next();
			if (classSchedule.isInstance(obj)) {
				itr.prune();
			} else if (classUserSettings.isInstance(obj)) {
				updateCleanSlateRefs((EObjectWrapper) obj, parametersPackage);
			}
		}
	}

}
