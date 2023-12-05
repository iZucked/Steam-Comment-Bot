/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV203 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 202;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 203;
	}

	// Updates to EU ETS 
	@Override
	protected void doMigration(final @NonNull MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EFactory cargoFactory = cargoPackage.getEFactoryInstance();
		final EClass euEtsData = MetamodelUtils.getEClass(cargoPackage, "EuEtsModel");
		
		EObjectWrapper euEtsReferences = (EObjectWrapper) cargoFactory.create(euEtsData);
		
		{
			final EClass euEtsEmissionsCoveredTable = MetamodelUtils.getEClass(cargoPackage, "EmissionsCoveredTable");
			final List<EObjectWrapper> reductionFactors = new ArrayList<>(4);
			final EObjectWrapper rf1 = (EObjectWrapper) cargoFactory.create(euEtsEmissionsCoveredTable);
			rf1.setAttrib("startYear", 2023);
			rf1.setAttrib("emissionsCovered", 0);
			reductionFactors.add(rf1);
			final EObjectWrapper rf2 = (EObjectWrapper) cargoFactory.create(euEtsEmissionsCoveredTable);
			rf2.setAttrib("startYear", 2024);
			rf2.setAttrib("emissionsCovered", 40);
			reductionFactors.add(rf2);
			final EObjectWrapper rf3 = (EObjectWrapper) cargoFactory.create(euEtsEmissionsCoveredTable);
			rf3.setAttrib("startYear", 2025);
			rf3.setAttrib("emissionsCovered", 70);
			reductionFactors.add(rf3);
			final EObjectWrapper rf4 = (EObjectWrapper) cargoFactory.create(euEtsEmissionsCoveredTable);
			rf4.setAttrib("startYear", 2026);
			rf4.setAttrib("emissionsCovered", 100);
			reductionFactors.add(rf4);
			
			euEtsReferences.setRef("EmissionsCoveredTable", reductionFactors);
		}
		
		cargoModel.setRef("EuEtsModel", euEtsReferences);
	}

}
