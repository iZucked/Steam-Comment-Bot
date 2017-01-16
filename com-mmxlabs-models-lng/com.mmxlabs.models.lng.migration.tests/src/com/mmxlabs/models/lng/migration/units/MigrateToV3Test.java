/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class MigrateToV3Test extends AbstractMigrationTestClass {

	static {
		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		Object instance = null;
		instance = MMXCorePackage.eINSTANCE;
		instance = TypesPackage.eINSTANCE;
		instance = AnalyticsPackage.eINSTANCE;
		instance = CargoPackage.eINSTANCE;
		instance = CommercialPackage.eINSTANCE;
		instance = FleetPackage.eINSTANCE;
		instance = ParametersPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		instance = LNGScenarioPackage.eINSTANCE;
	}

	@Test
	public void migrateFOBSalesTest() throws IOException {

		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV3().getSourceMetamodelLoader(null);

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EFactory factory_ScenarioModel = package_ScenarioModel.getEFactoryInstance();
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");
				final EReference reference_LNGScenarioModel_portModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portModel");

				final EPackage package_PortModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
				final EFactory factory_PortModel = package_PortModel.getEFactoryInstance();

				final EClass class_PortModel = MetamodelUtils.getEClass(package_PortModel, "PortModel");
				final EReference reference_PortModel_ports = MetamodelUtils.getReference(class_PortModel, "ports");
				final EClass class_Port = MetamodelUtils.getEClass(package_PortModel, "Port");

				final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
				final EFactory factory_SpotMarketsModel = package_SpotMarketsModel.getEFactoryInstance();

				final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
				final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
				final EClass class_FOBSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "FOBSalesMarket");

				final EReference reference_FOBSalesMarket_loadPort = MetamodelUtils.getReference(class_FOBSalesMarket, "loadPort");

				final EReference reference_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");
				final EReference reference_SpotMarketsGroup_markets = MetamodelUtils.getReference(class_SpotMarketGroup, "markets");

				final EObject scenarioModel = factory_ScenarioModel.create(class_LNGScenarioModel);

				final EObject portModel = factory_PortModel.create(class_PortModel);
				scenarioModel.eSet(reference_LNGScenarioModel_portModel, portModel);

				final EObject port = factory_PortModel.create(class_Port);
				portModel.eSet(reference_PortModel_ports, Collections.singletonList(port));

				final EObject spotMarketsModel = factory_SpotMarketsModel.create(class_SpotMarketsModel);
				scenarioModel.eSet(reference_LNGScenarioModel_spotMarketsModel, spotMarketsModel);

				final EObject fobSalesMarketGroup = factory_SpotMarketsModel.create(class_SpotMarketGroup);
				spotMarketsModel.eSet(reference_SpotMarketsModel_fobSalesSpotMarket, fobSalesMarketGroup);

				final List<EObject> markets = new ArrayList<EObject>(1);

				final EObject market = factory_SpotMarketsModel.create(class_FOBSalesMarket);
				markets.add(market);

				market.eSet(reference_FOBSalesMarket_loadPort, port);

				fobSalesMarketGroup.eSet(reference_SpotMarketsGroup_markets, markets);

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v1 metamodels
			// Load tmp file under v1
			{
				final MigrateToV3 migrator = new MigrateToV3();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.migrateFOBSales(loader, model);

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV3Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");
				final EReference reference_LNGScenarioModel_portModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portModel");

				final EPackage package_PortModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);

				final EClass class_PortModel = MetamodelUtils.getEClass(package_PortModel, "PortModel");
				final EReference reference_PortModel_ports = MetamodelUtils.getReference(class_PortModel, "ports");

				final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

				final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
				final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
				final EClass class_FOBSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "FOBSalesMarket");

				final EReference reference_FOBSalesMarket_originPorts = MetamodelUtils.getReference(class_FOBSalesMarket, "originPorts");

				final EReference reference_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");
				final EReference reference_SpotMarketsGroup_markets = MetamodelUtils.getReference(class_SpotMarketGroup, "markets");

				final EObject scenarioModel = r.getContents().get(0);

				Assert.assertNotNull(scenarioModel);

				final EObject portModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_portModel);
				Assert.assertNotNull(portModel);

				final List<EObject> ports = MetamodelUtils.getValueAsTypedList(portModel, reference_PortModel_ports);
				Assert.assertNotNull(ports);
				Assert.assertEquals(1, ports.size());
				final EObject port = ports.get(0);
				Assert.assertNotNull(port);

				final EObject spotMarketsModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_spotMarketsModel);
				Assert.assertNotNull(spotMarketsModel);

				final EObject fobSaleMarketGroup = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_fobSalesSpotMarket);
				Assert.assertNotNull(fobSaleMarketGroup);

				final List<EObject> markets = MetamodelUtils.getValueAsTypedList(fobSaleMarketGroup, reference_SpotMarketsGroup_markets);
				Assert.assertNotNull(markets);

				Assert.assertEquals(1, markets.size());
				final EObject market = markets.get(0);
				Assert.assertNotNull(market);

				final List<EObject> originPorts = MetamodelUtils.getValueAsTypedList(market, reference_FOBSalesMarket_originPorts);

				Assert.assertNotNull(originPorts);

				Assert.assertEquals(1, originPorts.size());
				Assert.assertSame(port, originPorts.get(0));

			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}

	@Test
	public void migrateOptimiserSettingsTest() throws IOException {
		final String settingsName = "migrateOptimiserSettingsTest";
		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV3().getSourceMetamodelLoader(null);

				final EPackage package_MMXCore = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass class_NamedObject = MetamodelUtils.getEClass(package_MMXCore, "NamedObject");
				final EAttribute attribute_NamedObject_name = MetamodelUtils.getAttribute(class_NamedObject, "name");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EFactory factory_ScenarioModel = package_ScenarioModel.getEFactoryInstance();
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

				final EPackage package_ParametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);
				final EFactory factory_ParametersModel = package_ParametersModel.getEFactoryInstance();

				final EClass class_ParametersModel = MetamodelUtils.getEClass(package_ParametersModel, "ParametersModel");
				final EClass class_OptimiserSettings = MetamodelUtils.getEClass(package_ParametersModel, "OptimiserSettings");

				final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
				final EFactory factory_CargoModel = package_CargoModel.getEFactoryInstance();

				final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
				final EClass class_Cargo = MetamodelUtils.getEClass(package_CargoModel, "Cargo");

				final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");
				final EReference reference_CargoModel_cargoes = MetamodelUtils.getReference(class_CargoModel, "cargoes");
				final EAttribute attribute_Cargo_allowRewiring = MetamodelUtils.getAttribute(class_Cargo, "allowRewiring");

				final EReference reference_LNGScenarioModel_parametersModel = MetamodelUtils.getReference(class_LNGScenarioModel, "parametersModel");
				final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
				final EReference reference_ParamatersModel_activeSetting = MetamodelUtils.getReference(class_ParametersModel, "activeSetting");
				final EReference reference_ParamatersModel_settings = MetamodelUtils.getReference(class_ParametersModel, "settings");
				final EAttribute attribute_OptimiserSettings_rewire = MetamodelUtils.getAttribute(class_OptimiserSettings, "rewire");

				final EObject scenarioModel = factory_ScenarioModel.create(class_LNGScenarioModel);
				final EObject portfolioModel = factory_ScenarioModel.create(class_LNGPortfolioModel);
				final EObject cargoModel = factory_CargoModel.create(class_CargoModel);
				final EObject parametersModel = factory_ParametersModel.create(class_ParametersModel);
				final EObject optmiserSettings = factory_ParametersModel.create(class_OptimiserSettings);

				scenarioModel.eSet(reference_LNGScenarioModel_portfolioModel, portfolioModel);
				scenarioModel.eSet(reference_LNGScenarioModel_parametersModel, parametersModel);
				portfolioModel.eSet(reference_LNGPortfolioModel_cargoModel, cargoModel);
				parametersModel.eSet(reference_ParamatersModel_activeSetting, optmiserSettings);
				optmiserSettings.eSet(attribute_NamedObject_name, settingsName);
				// Default rewiring
				optmiserSettings.eSet(attribute_OptimiserSettings_rewire, true);

				final List<EObject> settings = Collections.singletonList(optmiserSettings);
				parametersModel.eSet(reference_ParamatersModel_settings, settings);

				final EObject cargo1 = factory_CargoModel.create(class_Cargo);
				// Leave unset - take default
				final EObject cargo2 = factory_CargoModel.create(class_Cargo);
				cargo2.eSet(attribute_Cargo_allowRewiring, true);
				final EObject cargo3 = factory_CargoModel.create(class_Cargo);
				cargo3.eSet(attribute_Cargo_allowRewiring, false);

				final List<EObject> cargoes = Lists.newArrayList(cargo1, cargo2, cargo3);
				cargoModel.eSet(reference_CargoModel_cargoes, cargoes);

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v1 metamodels
			// Load tmp file under v1
			{
				final MigrateToV3 migrator = new MigrateToV3();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.migrateOptimiserSettings(loader, model);

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV3Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;

				final EPackage package_MMXCore = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass class_NamedObject = MetamodelUtils.getEClass(package_MMXCore, "NamedObject");
				final EAttribute attribute_NamedObject_name = MetamodelUtils.getAttribute(class_NamedObject, "name");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

				final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
				final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
				final EClass class_Cargo = MetamodelUtils.getEClass(package_CargoModel, "Cargo");

				final EReference reference_LNGScenarioModel_parametersModel = MetamodelUtils.getReference(class_LNGScenarioModel, "parametersModel");
				final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
				final EReference reference_LNGPortfolioModel_parameters = MetamodelUtils.getReference(class_LNGPortfolioModel, "parameters");

				final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");
				final EReference reference_CargoModel_cargoes = MetamodelUtils.getReference(class_CargoModel, "cargoes");
				final EAttribute attribute_Cargo_allowRewiring = MetamodelUtils.getAttribute(class_Cargo, "allowRewiring");

				final EObject scenarioModel = r.getContents().get(0);

				Assert.assertNotNull(scenarioModel);

				// This should have been removed
				final EObject parametersModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_parametersModel);
				Assert.assertNull(parametersModel);

				final EObject portfolioModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_portfolioModel);
				Assert.assertNotNull(portfolioModel);

				final EObject optmiserSettings = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_parameters);
				Assert.assertNotNull(optmiserSettings);

				Assert.assertEquals(settingsName, optmiserSettings.eGet(attribute_NamedObject_name));

				final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);
				Assert.assertNotNull(cargoModel);

				final List<EObject> cargoes = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_cargoes);
				Assert.assertNotNull(cargoes);
				Assert.assertEquals(3, cargoes.size());

				final EObject cargo1 = cargoes.get(0);
				final EObject cargo2 = cargoes.get(1);
				final EObject cargo3 = cargoes.get(2);

				// Cargo 1 -- settings default -> true
				Assert.assertTrue((Boolean) cargo1.eGet(attribute_Cargo_allowRewiring));
				// Cargo 2 - no change
				Assert.assertTrue((Boolean) cargo2.eGet(attribute_Cargo_allowRewiring));
				// Cargo 3 - no change
				Assert.assertFalse((Boolean) cargo3.eGet(attribute_Cargo_allowRewiring));
			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}

	@Test
	public void migrateBaseFuelPricingTest() throws IOException {

		final String baseFuel1 = "base1";
		final String baseFuel2 = "base2";

		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV3().getSourceMetamodelLoader(null);

				final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
				final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EFactory factory_ScenarioModel = package_ScenarioModel.getEFactoryInstance();
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

				final EPackage package_FleetModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);
				final EFactory factory_FleetModel = package_FleetModel.getEFactoryInstance();
				final EClass class_FleetModel = MetamodelUtils.getEClass(package_FleetModel, "FleetModel");
				final EClass class_BaseFuel = MetamodelUtils.getEClass(package_FleetModel, "BaseFuel");

				final EPackage package_PricingModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
				final EFactory factory_PricingModel = package_PricingModel.getEFactoryInstance();
				final EClass class_PricingModel = MetamodelUtils.getEClass(package_PricingModel, "PricingModel");
				final EClass class_FleetCostModel = MetamodelUtils.getEClass(package_PricingModel, "FleetCostModel");
				final EClass class_BaseFuelCost = MetamodelUtils.getEClass(package_PricingModel, "BaseFuelCost");

				final EReference reference_LNGScenarioModel_pricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");
				final EReference reference_LNGScenarioModel_fleetModel = MetamodelUtils.getReference(class_LNGScenarioModel, "fleetModel");

				final EReference reference_FleetModel_baseFuels = MetamodelUtils.getReference(class_FleetModel, "baseFuels");

				final EReference reference_PricingModel_fleetCost = MetamodelUtils.getReference(class_PricingModel, "fleetCost");
				final EReference reference_FleetCostModel_baseFuelPrices = MetamodelUtils.getReference(class_FleetCostModel, "baseFuelPrices");

				final EAttribute attribute_BaseFuelCost_price = MetamodelUtils.getAttribute(class_BaseFuelCost, "price");
				final EReference reference_BaseFuelCost_fuel = MetamodelUtils.getReference(class_BaseFuelCost, "fuel");

				// Create data

				final EObject scenarioModel = factory_ScenarioModel.create(class_LNGScenarioModel);
				final EObject fleetModel = factory_FleetModel.create(class_FleetModel);
				final EObject pricingModel = factory_PricingModel.create(class_PricingModel);

				scenarioModel.eSet(reference_LNGScenarioModel_pricingModel, pricingModel);
				scenarioModel.eSet(reference_LNGScenarioModel_fleetModel, fleetModel);

				final EObject fleetCostModel = factory_PricingModel.create(class_FleetCostModel);
				pricingModel.eSet(reference_PricingModel_fleetCost, fleetCostModel);

				// Construct base fuels
				final EObject base1 = factory_FleetModel.create(class_BaseFuel);
				base1.eSet(attribute_NamedObject_name, baseFuel1);
				final EObject base2 = factory_FleetModel.create(class_BaseFuel);
				base2.eSet(attribute_NamedObject_name, baseFuel2);

				// Add to model
				final List<EObject> baseFuels = Lists.newArrayList(base1, base2);
				fleetModel.eSet(reference_FleetModel_baseFuels, baseFuels);

				// Create initial base fuel prices
				final EObject baseFuelCost1 = factory_PricingModel.create(class_BaseFuelCost);
				baseFuelCost1.eSet(reference_BaseFuelCost_fuel, base1);
				baseFuelCost1.eSet(attribute_BaseFuelCost_price, 5.0);

				final EObject baseFuelCost2 = factory_PricingModel.create(class_BaseFuelCost);
				baseFuelCost2.eSet(reference_BaseFuelCost_fuel, base2);
				baseFuelCost2.eSet(attribute_BaseFuelCost_price, 10.0);

				final List<EObject> baseFuelCosts = Lists.newArrayList(baseFuelCost1, baseFuelCost2);
				fleetCostModel.eSet(reference_FleetCostModel_baseFuelPrices, baseFuelCosts);

				//
				// final List<EObject> baseFuelCosts = MetamodelUtils.getValueAsTypedList(fleetCostModel, reference_FleetCostModel_baseFuelPrices);
				//
				// final List<EObject> baseFuelIndices = new ArrayList<EObject>(baseFuelCosts.size());
				// final Date indexDate;
				// {
				// final Calendar cal = Calendar.getInstance();
				// cal.clear();
				// cal.set(Calendar.YEAR, 2000);
				// cal.set(Calendar.MONTH, Calendar.JANUARY);
				// cal.set(Calendar.DAY_OF_MONTH, 1);
				// indexDate = cal.getTime();
				// }
				//
				// for (final EObject baseFuelCost : baseFuelCosts) {
				//
				// if (!baseFuelCost.eIsSet(reference_BaseFuelCost_index) && baseFuelCost.eIsSet(attribute_BaseFuelCost_price)) {
				// final Number price = (Number) baseFuelCost.eGet(attribute_BaseFuelCost_price);
				// baseFuelCost.eUnset(attribute_BaseFuelCost_price);
				//
				// final EObject baseFuelIndex = factory_PricingModel.create(class_BaseFuelIndex);
				// baseFuelIndices.add(baseFuelIndex);
				//
				// // Copy name
				// final EObject fuel = (EObject) baseFuelCost.eGet(reference_BaseFuelCost_fuel);
				// if (fuel != null) {
				// baseFuelIndex.eSet(attribute_NamedObject_name, fuel.eGet(attribute_NamedObject_name));
				// }
				// // Generate a new UUID string
				// baseFuelIndex.eSet(attribute_UUIDObject_uuid, EcoreUtil.generateUUID());
				//
				// // Crate an empty index
				// final EObject dataIndex = factory_PricingModel.create(class_DataIndex);
				// baseFuelCost.eSet(reference_BaseFuelCost_index, dataIndex);
				//
				// // Copy price across if present
				// if (price != null) {
				// final EObject indexPrice = factory_PricingModel.create(class_IndexPoint);
				// indexPrice.eSet(attribute_IndexPoint_date, indexDate);
				// indexPrice.eSet(attribute_IndexPoint_value, price.doubleValue());
				//
				// dataIndex.eSet(reference_DataIndex_points, Collections.singletonList(indexPrice));
				//
				// baseFuelIndex.eSet(reference_NamedIndexContainer_data, dataIndex);
				// }
				// }
				// }
				//
				// if (!baseFuelIndices.isEmpty()) {
				// final List<EObject> existing = MetamodelUtils.getValueAsTypedList(pricingModel, reference_PricingModel_baseFuelPrices);
				// if (existing != null) {
				// baseFuelIndices.addAll(existing);
				// }
				//
				// pricingModel.eSet(reference_PricingModel_baseFuelPrices, baseFuelIndices);
				// }
				//
				// // Default rewiring
				// optmiserSettings.eSet(attribute_OptimiserSettings_rewire, true);
				//
				// final List<EObject> settings = Collections.singletonList(optmiserSettings);
				// parametersModel.eSet(reference_ParamatersModel_settings, settings);
				//
				// final EObject cargo1 = factory_CargoModel.create(class_Cargo);
				// // Leave unset - take default
				// final EObject cargo2 = factory_CargoModel.create(class_Cargo);
				// cargo2.eSet(attribute_Cargo_allowRewiring, true);
				// final EObject cargo3 = factory_CargoModel.create(class_Cargo);
				// cargo3.eSet(attribute_Cargo_allowRewiring, false);
				//
				// final List<EObject> cargoes = Lists.newArrayList(cargo1, cargo2, cargo3);
				// cargoModel.eSet(reference_CargoModel_cargoes, cargoes);

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v1 metamodels
			// Load tmp file under v1
			{
				final MigrateToV3 migrator = new MigrateToV3();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.migrateBaseFuelPricing(loader, model);

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV3Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;

				final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
				final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

				final EPackage package_PricingModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
				final EClass class_PricingModel = MetamodelUtils.getEClass(package_PricingModel, "PricingModel");
				final EClass class_FleetCostModel = MetamodelUtils.getEClass(package_PricingModel, "FleetCostModel");
				final EClass class_BaseFuelCost = MetamodelUtils.getEClass(package_PricingModel, "BaseFuelCost");
				final EClass class_DataIndex = MetamodelUtils.getEClass(package_PricingModel, "DataIndex");
				final EClass class_IndexPoint = MetamodelUtils.getEClass(package_PricingModel, "IndexPoint");
				final EClass class_NamedIndexContainer = MetamodelUtils.getEClass(package_PricingModel, "NamedIndexContainer");

				final EReference reference_LNGScenarioModel_pricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");

				final EReference reference_PricingModel_fleetCost = MetamodelUtils.getReference(class_PricingModel, "fleetCost");
				final EReference reference_PricingModel_baseFuelPrices = MetamodelUtils.getReference(class_PricingModel, "baseFuelPrices");
				final EReference reference_FleetCostModel_baseFuelPrices = MetamodelUtils.getReference(class_FleetCostModel, "baseFuelPrices");

				final EReference reference_BaseFuelCost_index = MetamodelUtils.getReference(class_BaseFuelCost, "index");

				final EAttribute attribute_IndexPoint_value = MetamodelUtils.getAttribute(class_IndexPoint, "value");

				final EReference reference_DataIndex_points = MetamodelUtils.getReference(class_DataIndex, "points");
				final EReference reference_NamedIndexContainer_data = MetamodelUtils.getReference(class_NamedIndexContainer, "data");

				final EObject scenarioModel = r.getContents().get(0);
				Assert.assertNotNull(scenarioModel);

				final EObject pricingModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_pricingModel);
				Assert.assertNotNull(pricingModel);

				final EObject fleetCostModel = (EObject) pricingModel.eGet(reference_PricingModel_fleetCost);
				Assert.assertNotNull(fleetCostModel);

				final List<EObject> baseFuelCosts = MetamodelUtils.getValueAsTypedList(fleetCostModel, reference_FleetCostModel_baseFuelPrices);
				Assert.assertNotNull(baseFuelCosts);
				Assert.assertEquals(2, baseFuelCosts.size());

				final List<EObject> baseFuelIndices = MetamodelUtils.getValueAsTypedList(pricingModel, reference_PricingModel_baseFuelPrices);
				Assert.assertNotNull(baseFuelIndices);
				Assert.assertEquals(2, baseFuelIndices.size());

				final EObject baseFuelIndex1 = baseFuelIndices.get(0);
				final EObject baseFuelIndex2 = baseFuelIndices.get(1);

				Assert.assertEquals(baseFuel1, baseFuelIndex1.eGet(attribute_NamedObject_name));
				Assert.assertEquals(baseFuel2, baseFuelIndex2.eGet(attribute_NamedObject_name));

				final EObject baseFuelCost1 = baseFuelCosts.get(0);
				final EObject baseFuelCost2 = baseFuelCosts.get(1);

				Assert.assertSame(baseFuelIndex1, baseFuelCost1.eGet(reference_BaseFuelCost_index));
				Assert.assertSame(baseFuelIndex2, baseFuelCost2.eGet(reference_BaseFuelCost_index));

				final EObject index1 = (EObject) baseFuelIndex1.eGet(reference_NamedIndexContainer_data);
				Assert.assertNotNull(index1);

				final EObject index2 = (EObject) baseFuelIndex2.eGet(reference_NamedIndexContainer_data);
				Assert.assertNotNull(index2);

				final List<EObject> points1 = MetamodelUtils.getValueAsTypedList(index1, reference_DataIndex_points);
				final List<EObject> points2 = MetamodelUtils.getValueAsTypedList(index2, reference_DataIndex_points);
				Assert.assertNotNull(points1);
				Assert.assertNotNull(points2);

				Assert.assertEquals(1, points1.size());
				Assert.assertEquals(1, points2.size());

				final EObject price1 = points1.get(0);
				final EObject price2 = points2.get(0);

				Assert.assertEquals(Double.valueOf(5.0), price1.eGet(attribute_IndexPoint_value));
				Assert.assertEquals(Double.valueOf(10.0), price2.eGet(attribute_IndexPoint_value));
			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}
}
