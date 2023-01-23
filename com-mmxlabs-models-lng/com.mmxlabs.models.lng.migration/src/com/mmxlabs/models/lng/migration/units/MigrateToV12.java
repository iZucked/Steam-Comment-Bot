/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV12 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 11;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 12;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {

		final EObject model = modelRecord.getModelRoot();
		final MetamodelLoader modelLoader = modelRecord.getMetamodelLoader();

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_VesselAvailability = MetamodelUtils.getEClass(package_CargoModel, "VesselAvailability");
		final EReference reference_CargoModel_vesselAvailabilities = MetamodelUtils.getReference(class_CargoModel, "vesselAvailabilities");

		final EAttribute attribute_VesselAvailability_timeCharterRate = MetamodelUtils.getAttribute(class_VesselAvailability, "timeCharterRate");
		final EAttribute attribute_VesselAvailability_charterRate = MetamodelUtils.getAttribute(class_VesselAvailability, "charterRate");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel == null) {
			return;
		}
		final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);

		if (cargoModel == null) {
			return;
		}

		final List<EObject> vesselAvailabilities = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_vesselAvailabilities);
		if (vesselAvailabilities == null) {
			return;
		}

		for (final EObject vesselAvailability : vesselAvailabilities) {
			if (vesselAvailability.eIsSet(attribute_VesselAvailability_timeCharterRate)) {
				final Object value = vesselAvailability.eGet(attribute_VesselAvailability_timeCharterRate);
				if (value instanceof Number) {
					final int rate = ((Number) value).intValue();
					vesselAvailability.eSet(attribute_VesselAvailability_charterRate, Integer.toString(rate));
				} else if (value instanceof String) {
					vesselAvailability.eSet(attribute_VesselAvailability_charterRate, value);
				}
				vesselAvailability.eUnset(attribute_VesselAvailability_timeCharterRate);
			}
		}
	}
}
