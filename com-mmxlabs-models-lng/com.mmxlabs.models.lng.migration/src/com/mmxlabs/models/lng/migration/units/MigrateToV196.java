/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV196 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 195;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 196;
	}

	// Add CII information to cargo model
	// Moves emission rates for each vessel to emission rate per fuel
	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		final EPackage fleetPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);
		final EFactory fleetFactory = fleetPackage.getEFactoryInstance();

		final EClass ciiReferenceData = MetamodelUtils.getEClass(fleetPackage, "CIIReferenceData");
		EObjectWrapper ciiReferences = fleetModel.getRef("ciiReferences");

		if (ciiReferences == null) {
			ciiReferences = (EObjectWrapper) fleetFactory.create(ciiReferenceData);
			fleetModel.setRef("ciiReferences", ciiReferences);
		}
	}

}
