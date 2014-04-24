/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
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

public class MigrateToV17 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 16;
	}

	@Override
	public int getDestinationVersion() {
		return 17;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV15Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV16Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);
		fixDivertibleFlagTypo(modelLoader, model);

	}

	/**
	 * Fix spelling of "divertible" flag
	 * 
	 * @param modelLoader
	 * @param model
	 */
	private void fixDivertibleFlagTypo(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_Slot = MetamodelUtils.getEClass(package_CargoModel, "Slot");

		final EReference reference_CargoModel_loadSlots = MetamodelUtils.getReference(class_CargoModel, "loadSlots");
		final EReference reference_CargoModel_dischargeSlots = MetamodelUtils.getReference(class_CargoModel, "dischargeSlots");

		final EAttribute attribute_Slot_divertable = MetamodelUtils.getAttribute(class_Slot, "divertable");
		final EAttribute attribute_Slot_divertible = MetamodelUtils.getAttribute(class_Slot, "divertible");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel == null) {
			return;
		}
		final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);

		if (cargoModel == null) {
			return;
		}

		// Check load slots
		final EList<EObject> loadSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_loadSlots);
		if (loadSlots != null) {
			for (final EObject slot : loadSlots) {

				if (slot.eIsSet(attribute_Slot_divertable)) {
					slot.eSet(attribute_Slot_divertible, slot.eIsSet(attribute_Slot_divertable));
					slot.eUnset(attribute_Slot_divertable);
				}
			}
		}

		// Check discharge slots
		final EList<EObject> dischargeSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_dischargeSlots);
		if (dischargeSlots != null) {
			for (final EObject slot : dischargeSlots) {
				if (slot.eIsSet(attribute_Slot_divertable)) {
					slot.eSet(attribute_Slot_divertible, slot.eIsSet(attribute_Slot_divertable));
					slot.eUnset(attribute_Slot_divertable);
				}
			}
		}
	}
}
