/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV16 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 15;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 16;
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
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV16Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);
		updateDivertableFlags(modelLoader, model);

	}

	private void updateDivertableFlags(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EPackage package_PortModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EPackage package_LNGTypes = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_Port = MetamodelUtils.getEClass(package_PortModel, "Port");
		final EAttribute attribute_port_capabilities = MetamodelUtils.getAttribute(class_Port, "capabilities");

		final EEnum enum_PortCapability = MetamodelUtils.getEEnum(package_LNGTypes, "PortCapability");
		final EEnumLiteral enumLiteral_PortCapability_Load = MetamodelUtils.getEEnum_Literal(enum_PortCapability, "LOAD");
		final EEnumLiteral enumLiteral_PortCapability_Discharge = MetamodelUtils.getEEnum_Literal(enum_PortCapability, "DISCHARGE");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_Slot = MetamodelUtils.getEClass(package_CargoModel, "Slot");
		final EClass class_LoadSlot = MetamodelUtils.getEClass(package_CargoModel, "LoadSlot");
		final EClass class_DischargeSlot = MetamodelUtils.getEClass(package_CargoModel, "DischargeSlot");

		final EReference reference_CargoModel_loadSlots = MetamodelUtils.getReference(class_CargoModel, "loadSlots");
		final EReference reference_CargoModel_dischargeSlots = MetamodelUtils.getReference(class_CargoModel, "dischargeSlots");

		final EAttribute attribute_Slot_divertable = MetamodelUtils.getAttribute(class_Slot, "divertable");
		final EReference reference_Slot_port = MetamodelUtils.getReference(class_Slot, "port");
		final EAttribute attribute_LoadSlot_DESPurchase = MetamodelUtils.getAttribute(class_LoadSlot, "DESPurchase");
		final EAttribute attribute_DischargeSlot_FOBSale = MetamodelUtils.getAttribute(class_DischargeSlot, "FOBSale");

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
				if (Boolean.TRUE.equals(slot.eGet(attribute_LoadSlot_DESPurchase))) {
					final EObject port = (EObject) slot.eGet(reference_Slot_port);
					if (port != null) {
						final EList<Object> portCapabilities = MetamodelUtils.getValueAsTypedList(port, attribute_port_capabilities);
						if (portCapabilities != null) {
							if (portCapabilities.contains(enumLiteral_PortCapability_Load)) {
								slot.eSet(attribute_Slot_divertable, Boolean.TRUE);
							}
						}
					}
				}
			}
		}

		// Check discharge slots
		final EList<EObject> dischargeSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_dischargeSlots);
		if (dischargeSlots != null) {
			for (final EObject slot : dischargeSlots) {
				if (Boolean.TRUE.equals(slot.eGet(attribute_DischargeSlot_FOBSale))) {
					final EObject port = (EObject) slot.eGet(reference_Slot_port);
					if (port != null) {
						final EList<Object> portCapabilities = MetamodelUtils.getValueAsTypedList(port, attribute_port_capabilities);
						if (portCapabilities != null) {
							if (portCapabilities.contains(enumLiteral_PortCapability_Discharge)) {
								slot.eSet(attribute_Slot_divertable, Boolean.TRUE);
							}
						}
					}
				}
			}
		}
	}
}
