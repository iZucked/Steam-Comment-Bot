/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

public class MigrateToV164 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 163;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 164;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		// Nominations model migration.
		final EPackage transfersPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_TransfersModel);
		final EClass transferModelClass = MetamodelUtils.getEClass(transfersPackage, "TransferModel");
		final EFactory transfersFactory = transfersPackage.getEFactoryInstance();

		EObjectWrapper transferModel = scenarioModel.getRef("transferModel");

		if (transferModel == null) {
			transferModel = (EObjectWrapper) transfersFactory.create(transferModelClass);
			scenarioModel.setRef("transferModel", transferModel);
		}
	}
}