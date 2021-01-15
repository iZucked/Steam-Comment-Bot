/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV81 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 80;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 81;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		@NonNull
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		if (modelRoot.getRef("analyticsModel") == null) {

			final EPackage package_analyticsModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
			final EClass class_AnalyticsModel = MetamodelUtils.getEClass(package_analyticsModel, "AnalyticsModel");
			final EObject analyticsModel = package_analyticsModel.getEFactoryInstance().create(class_AnalyticsModel);
			modelRoot.setRef("analyticsModel", analyticsModel);
		}
	}
}
