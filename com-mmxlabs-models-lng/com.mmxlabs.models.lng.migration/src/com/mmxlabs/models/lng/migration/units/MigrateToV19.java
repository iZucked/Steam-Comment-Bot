/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

public class MigrateToV19 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 18;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 19;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV18Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV19Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);
		// Set the new PricingEvent values
		setContractDefaults(modelLoader, model);
		setSpotMarketDefaults(modelLoader, model);
		setSlotDefaults(modelLoader, model);

	}

	private void setContractDefaults(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CommericalModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EReference reference_LNGScenarioModel_commercialModel = MetamodelUtils.getReference(class_LNGScenarioModel, "commercialModel");

		final EClass class_CommercialModel = MetamodelUtils.getEClass(package_CommericalModel, "CommercialModel");

		final EReference reference_CommercialModel_purchaseContracts = MetamodelUtils.getReference(class_CommercialModel, "purchaseContracts");
		final EReference reference_CommercialModel_salesContracts = MetamodelUtils.getReference(class_CommercialModel, "salesContracts");

		final EClass class_Contract = MetamodelUtils.getEClass(package_CommericalModel, "Contract");
		final EAttribute attribute_Contract_pricingEvent = MetamodelUtils.getAttribute(class_Contract, "pricingEvent");

		final EEnum enum_PricingEvent = MetamodelUtils.getEEnum(package_CommericalModel, "PricingEvent");
		final EEnumLiteral pricingEvent_Start_Load = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_LOAD");
		final EEnumLiteral pricingEvent_Start_Discharge = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_DISCHARGE");

		final EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);
		if (commercialModel == null) {
			return;
		}

		// Purchase contracts default to start of load
		final EList<EObject> purchaseContracts = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_purchaseContracts);
		if (purchaseContracts != null) {
			for (final EObject contract : purchaseContracts) {
				contract.eSet(attribute_Contract_pricingEvent, pricingEvent_Start_Load);
			}
		}
		// Purchase contracts default to start of load
		final EList<EObject> salesContracts = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_salesContracts);
		if (salesContracts != null) {
			for (final EObject contract : salesContracts) {
				contract.eSet(attribute_Contract_pricingEvent, pricingEvent_Start_Discharge);
			}
		}
	}

	private void setSlotDefaults(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_CargoModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EPackage package_CommericalModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EEnum enum_PricingEvent = MetamodelUtils.getEEnum(package_CommericalModel, "PricingEvent");
		final EEnumLiteral pricingEvent_Start_Load = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_LOAD");
		final EEnumLiteral pricingEvent_Start_Discharge = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_DISCHARGE");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_Slot = MetamodelUtils.getEClass(package_CargoModel, "Slot");
		final EClass class_SpotSlot = MetamodelUtils.getEClass(package_CargoModel, "SpotSlot");

		final EReference reference_CargoModel_loadSlots = MetamodelUtils.getReference(class_CargoModel, "loadSlots");
		final EReference reference_CargoModel_dischargeSlots = MetamodelUtils.getReference(class_CargoModel, "dischargeSlots");

		final EReference reference_Slot_contract = MetamodelUtils.getReference(class_Slot, "contract");
		final EAttribute attribute_Slot_pricingEvent = MetamodelUtils.getAttribute(class_Slot, "pricingEvent");

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
				if (!class_SpotSlot.isInstance(slot) && !slot.eIsSet(reference_Slot_contract)) {
					slot.eSet(attribute_Slot_pricingEvent, pricingEvent_Start_Load);
				}
			}
		}

		// Check discharge slots
		final EList<EObject> dischargeSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_dischargeSlots);
		if (dischargeSlots != null) {
			for (final EObject slot : dischargeSlots) {
				if (!class_SpotSlot.isInstance(slot) && !slot.eIsSet(reference_Slot_contract)) {
					slot.eSet(attribute_Slot_pricingEvent, pricingEvent_Start_Discharge);
				}
			}
		}
	}

	private void setSpotMarketDefaults(final MetamodelLoader modelLoader, final EObject model) {

		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_SpotMarketsModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
		final EPackage package_CommericalModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

		final EEnum enum_PricingEvent = MetamodelUtils.getEEnum(package_CommericalModel, "PricingEvent");
		final EEnumLiteral pricingEvent_Start_Load = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_LOAD");
		final EEnumLiteral pricingEvent_Start_Discharge = MetamodelUtils.getEEnum_Literal(enum_PricingEvent, "START_DISCHARGE");

		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
		final EClass class_SlotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
		final EClass class_SpotMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarket");

		final EReference reference_SpotMarketsModel_desPurchaseSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "desPurchaseSpotMarket");
		final EReference reference_SpotMarketsModel_desSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "desSalesSpotMarket");
		final EReference reference_SpotMarketsModel_fobPurchasesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobPurchasesSpotMarket");
		final EReference reference_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");

		final EReference reference_SlotMarketGroup_markets = MetamodelUtils.getReference(class_SlotMarketGroup, "markets");
		final EAttribute attribute_SpotMarket_pricingEvent = MetamodelUtils.getAttribute(class_SpotMarket, "pricingEvent");

		final EObject spotMarketsModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
		if (spotMarketsModel == null) {
			return;
		}

		// DES Purchases Markets
		{
			EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_desPurchaseSpotMarket);
			if (group != null) {
				final EList<EObject> spotMarkets = MetamodelUtils.getValueAsTypedList(group, reference_SlotMarketGroup_markets);
				if (spotMarkets != null) {
					for (final EObject spotMarket : spotMarkets) {
						spotMarket.eSet(attribute_SpotMarket_pricingEvent, pricingEvent_Start_Discharge);
					}
				}
			}
		}
		// DES Sales Markets
		{
			EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_desSalesSpotMarket);
			if (group != null) {
				final EList<EObject> spotMarkets = MetamodelUtils.getValueAsTypedList(group, reference_SlotMarketGroup_markets);
				if (spotMarkets != null) {
					for (final EObject spotMarket : spotMarkets) {
						spotMarket.eSet(attribute_SpotMarket_pricingEvent, pricingEvent_Start_Discharge);
					}
				}
			}
		}

		// FOB Purchases Markets
		{
			EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_fobPurchasesSpotMarket);
			if (group != null) {
				final EList<EObject> spotMarkets = MetamodelUtils.getValueAsTypedList(group, reference_SlotMarketGroup_markets);
				if (spotMarkets != null) {
					for (final EObject spotMarket : spotMarkets) {
						spotMarket.eSet(attribute_SpotMarket_pricingEvent, pricingEvent_Start_Load);
					}
				}
			}
		}
		// FOB Sales Markets
		{
			EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_fobSalesSpotMarket);
			if (group != null) {
				final EList<EObject> spotMarkets = MetamodelUtils.getValueAsTypedList(group, reference_SlotMarketGroup_markets);
				if (spotMarkets != null) {
					for (final EObject spotMarket : spotMarkets) {
						spotMarket.eSet(attribute_SpotMarket_pricingEvent, pricingEvent_Start_Load);
					}
				}
			}
		}
	}
}
