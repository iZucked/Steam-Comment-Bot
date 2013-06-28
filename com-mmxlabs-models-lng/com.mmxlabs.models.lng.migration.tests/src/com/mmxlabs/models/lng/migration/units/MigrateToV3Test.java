/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
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

public class MigrateToV3Test {

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
		instance = AssignmentPackage.eINSTANCE;
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
	}
}
