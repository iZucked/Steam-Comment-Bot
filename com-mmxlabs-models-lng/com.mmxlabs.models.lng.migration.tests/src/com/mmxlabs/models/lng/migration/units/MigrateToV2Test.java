/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

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

public class MigrateToV2Test extends AbstractMigrationTestClass {

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
	public void fixSpotMarketGroups_AllMissing_Test() throws IOException {

		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV2().getSourceMetamodelLoader(null);
				final EPackage scenarioPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EFactory scenarioFactory = scenarioPackage.getEFactoryInstance();

				final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
				final EFactory spotMarketsFactory = spotMarketsPackage.getEFactoryInstance();

				final EClass class_ScenarioModel = MetamodelUtils.getEClass(scenarioPackage, "LNGScenarioModel");
				final EReference feature_ScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_ScenarioModel, "spotMarketsModel");

				final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");

				final EObject scenarioModel = scenarioFactory.create(class_ScenarioModel);
				final EObject spotMarketsModel = spotMarketsFactory.create(class_SpotMarketsModel);
				scenarioModel.eSet(feature_ScenarioModel_spotMarketsModel, spotMarketsModel);

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v1 metamodels
			// Load tmp file under v1
			{
				final MigrateToV2 migrator = new MigrateToV2();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.fixSpotMarketGroups(loader, model);

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV2Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;

				final EPackage scenarioPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);

				final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

				final EClass class_ScenarioModel = MetamodelUtils.getEClass(scenarioPackage, "LNGScenarioModel");
				final EReference feature_ScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_ScenarioModel, "spotMarketsModel");

				final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");
				final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketGroup");
				final EEnum SpotType_Enum = MetamodelUtils.getEEnum(spotMarketsPackage, "SpotType");
				final EAttribute feature_SpotMarketsGroup_type = MetamodelUtils.getAttribute(class_SpotMarketGroup, "type");

				final EReference feature_SpotMarketsModel_desPurchases_group = MetamodelUtils.getReference(class_SpotMarketsModel, "desPurchaseSpotMarket");
				final EReference feature_SpotMarketsModel_desSales_group = MetamodelUtils.getReference(class_SpotMarketsModel, "desSalesSpotMarket");
				final EReference feature_SpotMarketsModel_fobPurchases_group = MetamodelUtils.getReference(class_SpotMarketsModel, "fobPurchasesSpotMarket");
				final EReference feature_SpotMarketsModel_fobSales_group = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");

				final EObject scenarioModel = r.getContents().get(0);

				Assert.assertNotNull(scenarioModel);

				final EObject spotMarketsModel = (EObject) scenarioModel.eGet(feature_ScenarioModel_spotMarketsModel);
				Assert.assertNotNull(spotMarketsModel);

				final EObject desPurchaseMarketGroup = (EObject) spotMarketsModel.eGet(feature_SpotMarketsModel_desPurchases_group);
				Assert.assertNotNull(desPurchaseMarketGroup);
				Assert.assertEquals(MetamodelUtils.getEEnum_Literal(SpotType_Enum, "DESPurchase"), desPurchaseMarketGroup.eGet(feature_SpotMarketsGroup_type));

				final EObject desSaleMarketGroup = (EObject) spotMarketsModel.eGet(feature_SpotMarketsModel_desSales_group);
				Assert.assertNotNull(desSaleMarketGroup);
				Assert.assertEquals(MetamodelUtils.getEEnum_Literal(SpotType_Enum, "DESSale"), desSaleMarketGroup.eGet(feature_SpotMarketsGroup_type));

				final EObject fobPurchaseMarketGroup = (EObject) spotMarketsModel.eGet(feature_SpotMarketsModel_fobPurchases_group);
				Assert.assertNotNull(fobPurchaseMarketGroup);
				Assert.assertEquals(MetamodelUtils.getEEnum_Literal(SpotType_Enum, "FOBPurchase"), fobPurchaseMarketGroup.eGet(feature_SpotMarketsGroup_type));

				final EObject fobSaleMarketGroup = (EObject) spotMarketsModel.eGet(feature_SpotMarketsModel_fobSales_group);
				Assert.assertNotNull(fobSaleMarketGroup);
				Assert.assertEquals(MetamodelUtils.getEEnum_Literal(SpotType_Enum, "FOBSale"), fobSaleMarketGroup.eGet(feature_SpotMarketsGroup_type));
			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}

	@Test
	public void updateIndicesTest() throws IOException {

		final String uuid1 = "uuid1";
		final String uuid2 = "uuid2";
		final String name1 = "name1";
		final String name2 = "name2";
		final String expr = "expr";

		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV2().getSourceMetamodelLoader(null);
				final EPackage package_MMXCore = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass class_UUIDObject = MetamodelUtils.getEClass(package_MMXCore, "UUIDObject");
				final EClass class_NamedObject = MetamodelUtils.getEClass(package_MMXCore, "NamedObject");
				final EAttribute attribute_NamedObject_name = MetamodelUtils.getAttribute(class_NamedObject, "name");
				final EAttribute attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(class_UUIDObject, "uuid");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EFactory factory_ScenaroModel = package_ScenarioModel.getEFactoryInstance();
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EReference reference_LNGScenarioModel_PricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");

				final EPackage package_Pricing = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
				final EFactory factory_Pricing = package_Pricing.getEFactoryInstance();
				final EClass class_PricingModel = MetamodelUtils.getEClass(package_Pricing, "PricingModel");
				final EClass class_CooldownPrice = MetamodelUtils.getEClass(package_Pricing, "CooldownPrice");
				final EClass class_Index = MetamodelUtils.getEClass(package_Pricing, "Index");
				final EClass class_DataIndex = MetamodelUtils.getEClass(package_Pricing, "DataIndex");
				final EClass class_DerivedIndex = MetamodelUtils.getEClass(package_Pricing, "DerivedIndex");
				final EAttribute attribute_DerivedIndex_expression = MetamodelUtils.getAttribute(class_DerivedIndex, "expression");
				final EReference reference_CooldownPrice_index = MetamodelUtils.getReference(class_CooldownPrice, "index");

				final EReference reference_PricingModel_commodityIndicies = MetamodelUtils.getReference(class_PricingModel, "commodityIndices");
				final EReference reference_PricingModel_cooldownPrices = MetamodelUtils.getReference(class_PricingModel, "cooldownPrices");

				// Create Scenario Model
				final EObject scenarioModel = factory_ScenaroModel.create(class_LNGScenarioModel);

				// Create and add the pricing model
				final EObject pricingModel = factory_Pricing.create(class_PricingModel);
				scenarioModel.eSet(reference_LNGScenarioModel_PricingModel, pricingModel);

				// Create sample indices
				final List<EObject> indices = new ArrayList<EObject>(2);

				// Create a DataIndex
				final EObject dataIndex = factory_Pricing.create(class_DataIndex);
				dataIndex.eSet(attribute_NamedObject_name, name1);
				dataIndex.eSet(attribute_UUIDObject_uuid, uuid1);

				indices.add(dataIndex);

				// Create UUIDIndex
				final EObject derivedIndex = factory_Pricing.create(class_DerivedIndex);
				derivedIndex.eSet(attribute_NamedObject_name, name2);
				derivedIndex.eSet(attribute_UUIDObject_uuid, uuid2);
				derivedIndex.eSet(attribute_DerivedIndex_expression, expr);

				indices.add(derivedIndex);

				pricingModel.eSet(reference_PricingModel_commodityIndicies, indices);

				// Create a cooldown object which references one of the indices
				final List<EObject> cooldownPrices = new ArrayList<EObject>(1);

				final EObject cooldownPrice = factory_Pricing.create(class_CooldownPrice);
				cooldownPrice.eSet(reference_CooldownPrice_index, derivedIndex);

				cooldownPrices.add(cooldownPrice);

				pricingModel.eSet(reference_PricingModel_cooldownPrices, cooldownPrices);

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v1 metamodels
			// Load tmp file under v1
			{
				final MigrateToV2 migrator = new MigrateToV2();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.updateIndices(loader, model, "CommodityIndex", "commodityIndices");

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV2Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;

				final EPackage package_MMXCore = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
				final EClass class_UUIDObject = MetamodelUtils.getEClass(package_MMXCore, "UUIDObject");
				final EClass class_NamedObject = MetamodelUtils.getEClass(package_MMXCore, "NamedObject");
				final EAttribute attribute_NamedObject_name = MetamodelUtils.getAttribute(class_NamedObject, "name");
				final EAttribute attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(class_UUIDObject, "uuid");

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EReference reference_LNGScenarioModel_PricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");

				final EPackage package_Pricing = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
				final EClass class_PricingModel = MetamodelUtils.getEClass(package_Pricing, "PricingModel");
				final EClass class_CooldownPrice = MetamodelUtils.getEClass(package_Pricing, "CooldownPrice");
				final EClass class_Index = MetamodelUtils.getEClass(package_Pricing, "Index");
				final EClass class_DataIndex = MetamodelUtils.getEClass(package_Pricing, "DataIndex");
				final EClass class_DerivedIndex = MetamodelUtils.getEClass(package_Pricing, "DerivedIndex");
				final EAttribute attribute_DerivedIndex_expression = MetamodelUtils.getAttribute(class_DerivedIndex, "expression");
				final EReference reference_CooldownPrice_index = MetamodelUtils.getReference(class_CooldownPrice, "index");

				final EClass class_CommodityIndex = MetamodelUtils.getEClass(package_Pricing, "CommodityIndex");
				final EReference reference_CommodityIndex_data = MetamodelUtils.getReference(class_CommodityIndex, "data");

				final EReference reference_PricingModel_commodityIndicies = MetamodelUtils.getReference(class_PricingModel, "commodityIndices");
				final EReference reference_PricingModel_cooldownPrices = MetamodelUtils.getReference(class_PricingModel, "cooldownPrices");

				final EObject scenarioModel = r.getContents().get(0);

				Assert.assertNotNull(scenarioModel);

				final EObject pricingModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_PricingModel);
				Assert.assertNotNull(pricingModel);

				final List<EObject> indices = MetamodelUtils.getValueAsTypedList(pricingModel, reference_PricingModel_commodityIndicies);
				Assert.assertNotNull(indices);

				Assert.assertEquals(2, indices.size());

				final EObject index1 = indices.get(0);
				Assert.assertNotNull(index1);
				Assert.assertTrue(class_CommodityIndex.isInstance(index1));
				Assert.assertEquals(name1, index1.eGet(attribute_NamedObject_name));
				Assert.assertEquals(uuid1, index1.eGet(attribute_UUIDObject_uuid));
				Assert.assertNotNull(index1.eGet(reference_CommodityIndex_data));

				final EObject index2 = indices.get(1);
				Assert.assertNotNull(index2);
				Assert.assertTrue(class_CommodityIndex.isInstance(index2));
				Assert.assertEquals(name2, index2.eGet(attribute_NamedObject_name));
				Assert.assertEquals(uuid2, index2.eGet(attribute_UUIDObject_uuid));
				final EObject index2_data = (EObject) index2.eGet(reference_CommodityIndex_data);
				Assert.assertNotNull(index2_data);

				Assert.assertTrue(class_DerivedIndex.isInstance(index2_data));
				Assert.assertEquals(expr, index2_data.eGet(attribute_DerivedIndex_expression));

				final List<EObject> cooldownPrices = MetamodelUtils.getValueAsTypedList(pricingModel, reference_PricingModel_cooldownPrices);
				Assert.assertNotNull(cooldownPrices);

				Assert.assertEquals(1, cooldownPrices.size());
				final EObject cooldownPrice = cooldownPrices.get(0);

				Assert.assertTrue(class_CooldownPrice.isInstance(cooldownPrice));
				final EObject indexRef = (EObject) cooldownPrice.eGet(reference_CooldownPrice_index);

				Assert.assertSame(index2, indexRef);

			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}
}
