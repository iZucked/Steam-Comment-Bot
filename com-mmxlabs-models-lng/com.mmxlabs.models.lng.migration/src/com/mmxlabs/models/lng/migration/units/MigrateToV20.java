/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
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

public class MigrateToV20 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 19;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 20;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV19Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV20Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);
		createEndHeelOptions(modelLoader, model);

	}

	private void createEndHeelOptions(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_VesselAvailability = MetamodelUtils.getEClass(package_CargoModel, "VesselAvailability");
		final EClass class_EndHeelOptions = MetamodelUtils.getEClass(package_CargoModel, "EndHeelOptions");

		final EReference reference_CargoModel_vesselAvailabilities = MetamodelUtils.getReference(class_CargoModel, "vesselAvailabilities");
		final EReference reference_VesselAvailability_endHeel = MetamodelUtils.getReference(class_VesselAvailability, "endHeel");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel == null) {
			return;
		}
		final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);
		if (cargoModel == null) {
			return;
		}

		// Purchase contracts default to start of load
		final EList<EObject> vesselAvailabilities = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_vesselAvailabilities);
		if (vesselAvailabilities != null) {
			for (final EObject vesselAvailability : vesselAvailabilities) {
				if (!vesselAvailability.eIsSet(reference_VesselAvailability_endHeel)) {
					EObject endHeelOptions = package_CargoModel.getEFactoryInstance().create(class_EndHeelOptions);
					vesselAvailability.eSet(reference_VesselAvailability_endHeel, endHeelOptions);
				}
			}
		}
	}
}
