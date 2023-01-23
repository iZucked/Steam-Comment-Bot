/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV156 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 155;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 156;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		@NonNull
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		@NonNull
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage analyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

		final EClass classSensitivityModel = MetamodelUtils.getEClass(analyticsPackage, "SensitivityModel");
		final EObjectWrapper sensitivityModel = (EObjectWrapper) analyticsPackage.getEFactoryInstance().create(classSensitivityModel);
		final EClass classOptionAnalysisModel = MetamodelUtils.getEClass(analyticsPackage, "OptionAnalysisModel");
		final EObjectWrapper optionAnalysisModel = (EObjectWrapper) analyticsPackage.getEFactoryInstance().create(classOptionAnalysisModel);
		optionAnalysisModel.setAttrib("name", "sensitivityModel");
		final EClass classBaseCase = MetamodelUtils.getEClass(analyticsPackage, "BaseCase");
		final EObjectWrapper baseCase = (EObjectWrapper) analyticsPackage.getEFactoryInstance().create(classBaseCase);
		optionAnalysisModel.setRef("baseCase", baseCase);
		sensitivityModel.setRef("sensitivityModel", optionAnalysisModel);

		scenarioModel.setRef("sensitivityModel", sensitivityModel);
	}
}