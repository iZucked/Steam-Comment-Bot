/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 */
public class MigrateToV3 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 2;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 3;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV2Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV2_V3Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateFOBSales(loader, model);
		migrateOptimiserSettings(loader, model);
		migrateBaseFuelPricing(loader, model);
	}

	protected void migrateBaseFuelPricing(final MetamodelLoader loader, final EObject model) {

		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

		final EPackage package_PricingModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EFactory factory_PricingModel = package_PricingModel.getEFactoryInstance();
		final EClass class_PricingModel = MetamodelUtils.getEClass(package_PricingModel, "PricingModel");
		final EClass class_FleetCostModel = MetamodelUtils.getEClass(package_PricingModel, "FleetCostModel");
		final EClass class_BaseFuelCost = MetamodelUtils.getEClass(package_PricingModel, "BaseFuelCost");
		final EClass class_BaseFuelIndex = MetamodelUtils.getEClass(package_PricingModel, "BaseFuelIndex");
		final EClass class_DataIndex = MetamodelUtils.getEClass(package_PricingModel, "DataIndex");
		final EClass class_IndexPoint = MetamodelUtils.getEClass(package_PricingModel, "IndexPoint");
		final EClass class_NamedIndexContainer = MetamodelUtils.getEClass(package_PricingModel, "NamedIndexContainer");

		final EReference reference_LNGScenarioModel_pricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");

		final EReference reference_PricingModel_fleetCost = MetamodelUtils.getReference(class_PricingModel, "fleetCost");
		final EReference reference_PricingModel_baseFuelPrices = MetamodelUtils.getReference(class_PricingModel, "baseFuelPrices");
		final EReference reference_FleetCostModel_baseFuelPrices = MetamodelUtils.getReference(class_FleetCostModel, "baseFuelPrices");

		final EAttribute attribute_BaseFuelCost_price = MetamodelUtils.getAttribute(class_BaseFuelCost, "price");
		final EReference reference_BaseFuelCost_index = MetamodelUtils.getReference(class_BaseFuelCost, "index");
		final EReference reference_BaseFuelCost_fuel = MetamodelUtils.getReference(class_BaseFuelCost, "fuel");

		final EAttribute attribute_IndexPoint_date = MetamodelUtils.getAttribute(class_IndexPoint, "date");
		final EAttribute attribute_IndexPoint_value = MetamodelUtils.getAttribute(class_IndexPoint, "value");

		final EReference reference_DataIndex_points = MetamodelUtils.getReference(class_DataIndex, "points");
		final EReference reference_NamedIndexContainer_data = MetamodelUtils.getReference(class_NamedIndexContainer, "data");

		final EObject pricingModel = (EObject) model.eGet(reference_LNGScenarioModel_pricingModel);

		final EObject fleetCostModel = (EObject) pricingModel.eGet(reference_PricingModel_fleetCost);
		if (fleetCostModel == null) {
			return;
		}

		final List<EObject> baseFuelCosts = MetamodelUtils.getValueAsTypedList(fleetCostModel, reference_FleetCostModel_baseFuelPrices);
		if (baseFuelCosts == null) {
			return;
		}
		final List<EObject> baseFuelIndices = new ArrayList<EObject>(baseFuelCosts.size());
		final Date indexDate;
		{
			final Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			indexDate = cal.getTime();
		}

		for (final EObject baseFuelCost : baseFuelCosts) {

			if (!baseFuelCost.eIsSet(reference_BaseFuelCost_index) && baseFuelCost.eIsSet(attribute_BaseFuelCost_price)) {
				final Number price = (Number) baseFuelCost.eGet(attribute_BaseFuelCost_price);
				baseFuelCost.eUnset(attribute_BaseFuelCost_price);

				final EObject baseFuelIndex = factory_PricingModel.create(class_BaseFuelIndex);
				baseFuelIndices.add(baseFuelIndex);

				// Copy name
				final EObject fuel = (EObject) baseFuelCost.eGet(reference_BaseFuelCost_fuel);
				if (fuel != null) {
					baseFuelIndex.eSet(attribute_NamedObject_name, fuel.eGet(attribute_NamedObject_name));
				}
				// Generate a new UUID string
				baseFuelIndex.eSet(attribute_UUIDObject_uuid, EcoreUtil.generateUUID());

				// Crate an empty index
				baseFuelCost.eSet(reference_BaseFuelCost_index, baseFuelIndex);

				// Copy price across if present
				final EObject dataIndex = factory_PricingModel.create(class_DataIndex);
				if (price != null) {
					final EObject indexPrice = factory_PricingModel.create(class_IndexPoint);
					indexPrice.eSet(attribute_IndexPoint_date, indexDate);
					indexPrice.eSet(attribute_IndexPoint_value, price.doubleValue());

					dataIndex.eSet(reference_DataIndex_points, Collections.singletonList(indexPrice));

					baseFuelIndex.eSet(reference_NamedIndexContainer_data, dataIndex);
				}
			}
		}

		if (!baseFuelIndices.isEmpty()) {
			final List<EObject> existing = MetamodelUtils.getValueAsTypedList(pricingModel, reference_PricingModel_baseFuelPrices);
			if (existing != null) {
				baseFuelIndices.addAll(existing);
			}

			pricingModel.eSet(reference_PricingModel_baseFuelPrices, baseFuelIndices);
		}
	}

	protected void migrateOptimiserSettings(final MetamodelLoader loader, final EObject model) {
		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EPackage package_ParametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);
		final EClass class_ParametersModel = MetamodelUtils.getEClass(package_ParametersModel, "ParametersModel");
		final EClass class_OptimiserSettings = MetamodelUtils.getEClass(package_ParametersModel, "OptimiserSettings");

		final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_Cargo = MetamodelUtils.getEClass(package_CargoModel, "Cargo");

		final EReference reference_LNGScenarioModel_parametersModel = MetamodelUtils.getReference(class_LNGScenarioModel, "parametersModel");
		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");

		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");
		final EReference reference_LNGPortfolioModel_parameters = MetamodelUtils.getReference(class_LNGPortfolioModel, "parameters");
		final EReference reference_CargoModel_cargoes = MetamodelUtils.getReference(class_CargoModel, "cargoes");
		final EReference reference_ParamatersModel_activeSetting = MetamodelUtils.getReference(class_ParametersModel, "activeSetting");
		final EAttribute attribute_OptimiserSettings_rewire = MetamodelUtils.getAttribute(class_OptimiserSettings, "rewire");
		final EAttribute attribute_Cargo_allowRewiring = MetamodelUtils.getAttribute(class_Cargo, "allowRewiring");

		if (model.eIsSet(reference_LNGScenarioModel_parametersModel)) {
			final EObject parametersModel = (EObject) model.eGet(reference_LNGScenarioModel_parametersModel);
			if (parametersModel != null && parametersModel.eIsSet(reference_ParamatersModel_activeSetting)) {
				// Move settings to new portfolio location
				final EObject settings = (EObject) parametersModel.eGet(reference_ParamatersModel_activeSetting);
				final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
				if (portfolioModel != null) {
					portfolioModel.eSet(reference_LNGPortfolioModel_parameters, settings);

					// Cargo rewire is no longer unsettable, so set the default value here
					final boolean rewire = (Boolean) settings.eGet(attribute_OptimiserSettings_rewire);

					final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);
					if (cargoModel != null) {
						final List<EObject> cargoes = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_cargoes);
						if (cargoes != null) {
							for (final EObject cargo : cargoes) {
								if (!cargo.eIsSet(attribute_Cargo_allowRewiring)) {
									cargo.eSet(attribute_Cargo_allowRewiring, rewire);
								}
							}
						}
					}
				}
			}
			// No need for parameters model any more
			model.eUnset(reference_LNGScenarioModel_parametersModel);
		}
	}

	protected void migrateFOBSales(final MetamodelLoader loader, final EObject model) {

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");

		final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
		final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
		final EClass class_FOBSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "FOBSalesMarket");

		final EReference reference_FOBSalesMarket_loadPort = MetamodelUtils.getReference(class_FOBSalesMarket, "loadPort");
		final EReference reference_FOBSalesMarket_originPorts = MetamodelUtils.getReference(class_FOBSalesMarket, "originPorts");

		final EReference reference_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");
		final EReference reference_SpotMarketsGroup_markets = MetamodelUtils.getReference(class_SpotMarketGroup, "markets");

		final EObject spotMarketsModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
		final EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_fobSalesSpotMarket);
		if (group != null) {
			final List<EObject> markets = MetamodelUtils.getValueAsTypedList(group, reference_SpotMarketsGroup_markets);
			if (markets != null) {
				for (final EObject market : markets) {
					if (market.eIsSet(reference_FOBSalesMarket_loadPort)) {
						final List<EObject> originPorts = new ArrayList<EObject>(1);
						originPorts.add((EObject) market.eGet(reference_FOBSalesMarket_loadPort));
						market.eSet(reference_FOBSalesMarket_originPorts, originPorts);
						market.eUnset(reference_FOBSalesMarket_loadPort);
					}
				}
			}
		}

	}
}
