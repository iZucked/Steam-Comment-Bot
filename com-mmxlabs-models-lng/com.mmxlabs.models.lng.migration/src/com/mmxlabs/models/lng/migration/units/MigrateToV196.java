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

			{
				final EClass ciiGradeBoundary = MetamodelUtils.getEClass(fleetPackage, "CIIGradeBoundary");
				final List<EObjectWrapper> ciiGradeBoundaries = new ArrayList<>(2);
				final EObjectWrapper gradeBoundary1 = (EObjectWrapper) fleetFactory.create(ciiGradeBoundary);
				gradeBoundary1.setAttrib("dwtUpperLimit", 200_000L);
				gradeBoundary1.setAttrib("gradeAValue", 0.89);
				gradeBoundary1.setAttrib("gradeBValue", 0.98);
				gradeBoundary1.setAttrib("gradeCValue", 1.06);
				gradeBoundary1.setAttrib("gradeDValue", 1.13);

				final EObjectWrapper gradeBoundary2 = (EObjectWrapper) fleetFactory.create(ciiGradeBoundary);
				gradeBoundary2.setAttrib("dwtUpperLimit", 100_000L);
				gradeBoundary2.setAttrib("gradeAValue", 0.78);
				gradeBoundary2.setAttrib("gradeBValue", 0.92);
				gradeBoundary2.setAttrib("gradeCValue", 1.10);
				gradeBoundary2.setAttrib("gradeDValue", 1.37);

				ciiGradeBoundaries.add(gradeBoundary1);
				ciiGradeBoundaries.add(gradeBoundary2);

				ciiReferences.setRef("ciiGradeBoundaries", ciiGradeBoundaries);
			}

			{
				final EClass fuelEmissionReference = MetamodelUtils.getEClass(fleetPackage, "FuelEmissionReference");
				final List<EObjectWrapper> fuelEmissions = new ArrayList<>(4);
				final EObjectWrapper fer1 = (EObjectWrapper) fleetFactory.create(fuelEmissionReference);
				fer1.setAttrib("name", "Diesel/Gas Oil");
				fer1.setAttrib("isoReference", "ISO 8217 Grades DMX through DMB");
				fer1.setAttrib("cf", 3.206);
				fuelEmissions.add(fer1);
				final EObjectWrapper fer2 = (EObjectWrapper) fleetFactory.create(fuelEmissionReference);
				fer2.setAttrib("name", "Light Fuel Oil (LFO)");
				fer2.setAttrib("isoReference", "ISO 8217 Grades RMA through RMD");
				fer2.setAttrib("cf", 3.151);
				fuelEmissions.add(fer2);
				final EObjectWrapper fer3 = (EObjectWrapper) fleetFactory.create(fuelEmissionReference);
				fer3.setAttrib("name", "Heavy Fuel Oil (HFO)");
				fer3.setAttrib("isoReference", "ISO 8217 Grades RME through RMK");
				fer3.setAttrib("cf", 3.114);
				fuelEmissions.add(fer3);
				final EObjectWrapper fer4 = (EObjectWrapper) fleetFactory.create(fuelEmissionReference);
				fer4.setAttrib("name", "Liquefied Natural Gas (LNG)");
				fer4.setAttrib("isoReference", "");
				fer4.setAttrib("cf", 2.750);
				fuelEmissions.add(fer4);
				
				ciiReferences.setRef("fuelEmissions", fuelEmissions);

			}
			{
				final EClass ciiReductionFactor = MetamodelUtils.getEClass(fleetPackage, "CIIReductionFactor");
				final List<EObjectWrapper> reductionFactors = new ArrayList<>(4);
				final EObjectWrapper rf1 = (EObjectWrapper) fleetFactory.create(ciiReductionFactor);
				rf1.setAttrib("year", 2023);
				rf1.setAttrib("percentage", 5);
				rf1.setAttrib("remark", "");
				reductionFactors.add(rf1);
				final EObjectWrapper rf2 = (EObjectWrapper) fleetFactory.create(ciiReductionFactor);
				rf2.setAttrib("year", 2024);
				rf2.setAttrib("percentage", 7);
				rf2.setAttrib("remark", "");
				reductionFactors.add(rf2);
				final EObjectWrapper rf3 = (EObjectWrapper) fleetFactory.create(ciiReductionFactor);
				rf3.setAttrib("year", 2025);
				rf3.setAttrib("percentage", 9);
				rf3.setAttrib("remark", "");
				reductionFactors.add(rf3);
				final EObjectWrapper rf4 = (EObjectWrapper) fleetFactory.create(ciiReductionFactor);
				rf4.setAttrib("year", 2026);
				rf4.setAttrib("percentage", 0);
				rf4.setAttrib("remark", "Values for 2026 onwards are not yet determined");
				reductionFactors.add(rf4);
				
				ciiReferences.setRef("reductionFactors", reductionFactors);
			}

			fleetModel.setRef("ciiReferences", ciiReferences);
		}
	}

}
