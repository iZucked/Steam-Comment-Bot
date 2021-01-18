/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV117 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 116;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 117;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		//Add in the nominations parameters.
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EPackage nominationsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_NominationsModel);
		final EFactory nominationsFactory = nominationsPackage.getEFactoryInstance();
		EObjectWrapper nominationsModel = scenarioModel.getRef("nominationsModel");
		EObjectWrapper nominationParameters = nominationsModel.getRef("nominationParameters");
		if (nominationParameters == null) {
			final EClass nominationsParametersClass = MetamodelUtils.getEClass(nominationsPackage, "NominationsParameters");
			nominationParameters = (EObjectWrapper) nominationsFactory.create(nominationsParametersClass);
			nominationsModel.setRef("nominationParameters", nominationParameters);
		}
	}
}
