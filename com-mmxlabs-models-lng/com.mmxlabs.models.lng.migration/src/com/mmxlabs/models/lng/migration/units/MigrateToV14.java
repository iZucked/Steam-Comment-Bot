/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
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

public class MigrateToV14 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 13;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 14;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV13Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV14Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);
		migrateSlotVesselRestrictions(loader, model);

	}

	private void migrateSlotVesselRestrictions(final MetamodelLoader loader, final EObject model) {

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EReference reference_CargoModel_cargoes = MetamodelUtils.getReference(class_CargoModel, "cargoes");

		final EClass class_Cargo = MetamodelUtils.getEClass(package_CargoModel, "Cargo");
		final EReference reference_Cargo_slots = MetamodelUtils.getReference(class_Cargo, "slots");
		final EReference reference_Cargo_allowedVessels = MetamodelUtils.getReference(class_Cargo, "allowedVessels");

		final EClass class_Slot = MetamodelUtils.getEClass(package_CargoModel, "Slot");
		final EReference reference_Slot_allowedVessels = MetamodelUtils.getReference(class_Slot, "allowedVessels");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel == null) {
			return;
		}
		final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);

		if (cargoModel == null) {
			return;
		}
		final EList<EObject> cargoes = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_cargoes);
		if (cargoes != null) {
			for (final EObject cargo : cargoes) {
				final EList<EObject> allowedVessels = MetamodelUtils.getValueAsTypedList(cargo, reference_Cargo_allowedVessels);
				if (allowedVessels != null && !allowedVessels.isEmpty()) {
					final EList<EObject> slots = MetamodelUtils.getValueAsTypedList(cargo, reference_Cargo_slots);
					if (slots != null) {
						for (final EObject slot : slots) {
							// Copy list
							slot.eSet(reference_Slot_allowedVessels, new ArrayList<EObject>(allowedVessels));
						}
					}

					cargo.eUnset(reference_Cargo_allowedVessels);
				}

			}
		}

	}

}
