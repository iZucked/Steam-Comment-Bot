/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV13 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 12;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 13;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV11Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV12Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// Nothing to do yet
		migrateTimeCharterRate(model);
	}

	private void migrateTimeCharterRate(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);

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
			if (vesselAvailability.eIsSet(attribute_VesselAvailability_charterRate)) {
				final Object value = vesselAvailability.eGet(attribute_VesselAvailability_charterRate);
				vesselAvailability.eUnset(attribute_VesselAvailability_charterRate);
				vesselAvailability.eSet(attribute_VesselAvailability_timeCharterRate, value);
			}
		}
	}
}
