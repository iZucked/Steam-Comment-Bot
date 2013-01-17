package com.mmxlabs.models.lng.migration.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class MigrateToV1Test {

	static {
		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		Object instance = null;
		instance = MMXCorePackage.eINSTANCE;
		instance = AnalyticsPackage.eINSTANCE;
		instance = CargoPackage.eINSTANCE;
		instance = CommercialPackage.eINSTANCE;
		instance = FleetPackage.eINSTANCE;
		instance = InputPackage.eINSTANCE;
		instance = OptimiserPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		// Add other packages?
	}

	@Test
	public void testSlotFixedPrice() throws IOException {

		// Load v0 metamodels

		// Construct a scenario
		File tmpFile = null;
		{
			final MetamodelLoader v0Loader = new MigrateToV1().getSourceMetamodelLoader();
			final EPackage cargoPackage = v0Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
			final EFactory cargoFactory = cargoPackage.getEFactoryInstance();

			final EClass class_CargoModel = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
			final EStructuralFeature feature_loadSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "loadSlots");
			final EStructuralFeature feature_dischargeSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "dischargeSlots");

			final EClass class_Slot = MetamodelUtils.getEClass(cargoPackage, "Slot");
			final EStructuralFeature feature_fixedPrice = MetamodelUtils.getStructuralFeature(class_Slot, "fixedPrice");
			final EStructuralFeature feature_priceExpression = MetamodelUtils.getStructuralFeature(class_Slot, "priceExpression");
			final EClass class_LoadSlot = MetamodelUtils.getEClass(cargoPackage, "LoadSlot");
			final EClass class_DischargeSlot = MetamodelUtils.getEClass(cargoPackage, "DischargeSlot");

			final EObject cargoModel = cargoFactory.create(class_CargoModel);

			// No price values set -- no change
			final EObject loadSlot1 = cargoFactory.create(class_LoadSlot);

			// Fixed Price set, no price expr set
			final EObject loadSlot2 = cargoFactory.create(class_LoadSlot);
			loadSlot2.eSet(feature_fixedPrice, 2.0);

			// Both set - expect, fixed to disappear and price expr to be unchanged
			final EObject loadSlot3 = cargoFactory.create(class_LoadSlot);
			loadSlot3.eSet(feature_fixedPrice, 2.0);
			loadSlot3.eSet(feature_priceExpression, "EXPR");

			final List<EObject> loadSlots = new ArrayList<EObject>(3);
			loadSlots.add(loadSlot1);
			loadSlots.add(loadSlot2);
			loadSlots.add(loadSlot3);

			cargoModel.eSet(feature_loadSlots, loadSlots);

			// Save to tmp file
			tmpFile = File.createTempFile("migrationtest", ".xmi");

			final Resource r = v0Loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
			r.getContents().add(cargoModel);
			r.save(null);
		}

		// Load v1 metamodels
		// Load tmp file under v1
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();

			final Resource r = v1Loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

			final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
			// Record features which have no meta-model equivalent so we can perform migration
			loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
			r.load(loadOptions);
			final EObject cargoModel = r.getContents().get(0);

			final Map<ModelsLNGSet_v1, EObject> models = new HashMap<ModelsLNGSet_v1, EObject>();
			models.put(ModelsLNGSet_v1.Cargo, cargoModel);
			// Run migration.
			migrator.migrateFixedPrice(v1Loader, models);

			r.save(null);
		}

		// Check output
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();
			final Resource r = v1Loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
			r.load(null);
			tmpFile.delete();

			final EPackage cargoPackage = v1Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

			final EClass class_CargoModel = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
			final EStructuralFeature feature_loadSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "loadSlots");
			final EStructuralFeature feature_dischargeSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "dischargeSlots");

			final EClass class_Slot = MetamodelUtils.getEClass(cargoPackage, "Slot");
			final EStructuralFeature feature_priceExpression = MetamodelUtils.getStructuralFeature(class_Slot, "priceExpression");
			final EClass class_LoadSlot = MetamodelUtils.getEClass(cargoPackage, "LoadSlot");
			final EClass class_DischargeSlot = MetamodelUtils.getEClass(cargoPackage, "DischargeSlot");

			final EObject cargoModel = r.getContents().get(0);

			Assert.assertTrue(((XMLResource) r).getEObjectToExtensionMap().isEmpty());

			final List<EObject> loadSlots = MetamodelUtils.getValueAsTypedList(cargoModel, feature_loadSlots);

			// No price values set -- no change
			final EObject loadSlot1 = loadSlots.get(0);
			Assert.assertFalse(loadSlot1.eIsSet(feature_priceExpression));

			// Fixed Price set, no price expr set
			final EObject loadSlot2 = loadSlots.get(1);
			Assert.assertTrue(loadSlot2.eIsSet(feature_priceExpression));
			Assert.assertEquals("2.0", loadSlot2.eGet(feature_priceExpression));

			// Both set - expect, fixed to disappear and price expr to be unchanged
			final EObject loadSlot3 = loadSlots.get(2);
			Assert.assertTrue(loadSlot3.eIsSet(feature_priceExpression));
			Assert.assertEquals("EXPR", loadSlot3.eGet(feature_priceExpression));
		}
	}

	@Test
	public void testRemoveExtraAnalyticsFields() throws IOException {

		// Load v0 metamodels

		// Construct a scenario
		File portModelFile = null;
		File analyticsModelFile = null;
		{
			final MetamodelLoader v0Loader = new MigrateToV1().getSourceMetamodelLoader();
			final EPackage analyticsPackage = v0Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
			final EFactory analyticsFactory = analyticsPackage.getEFactoryInstance();

			final EPackage portPackage = v0Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
			final EFactory portFactory = portPackage.getEFactoryInstance();

			final EClass class_portModel = MetamodelUtils.getEClass(portPackage, "PortModel");
			final EStructuralFeature feature_PortModel_ports = MetamodelUtils.getStructuralFeature(class_portModel, "ports");

			final EClass class_AnalyticsModel = MetamodelUtils.getEClass(analyticsPackage, "AnalyticsModel");
			final EStructuralFeature feature_roundTripMatrices = MetamodelUtils.getStructuralFeature(class_AnalyticsModel, "roundTripMatrices");

			final EClass class_UnitCostMatrix = MetamodelUtils.getEClass(analyticsPackage, "UnitCostMatrix");
			final EStructuralFeature feature_toPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "toPorts");
			final EStructuralFeature feature_fromPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "fromPorts");

			final EStructuralFeature feature_ports = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "ports");
			final EStructuralFeature feature_returnIdleTime = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "returnIdleTime");
			final EStructuralFeature feature_dischargeIdleTime = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "dischargeIdleTime");

			final EClass class_port = MetamodelUtils.getEClass(portPackage, "Port");

			final EObject portModel = portFactory.create(class_portModel);

			final EObject port1 = portFactory.create(class_port);
			final EObject port2 = portFactory.create(class_port);
			final List<EObject> ports = new ArrayList<EObject>(2);
			ports.add(port1);
			ports.add(port2);

			final EObject port3 = portFactory.create(class_port);
			final List<EObject> ports2 = new ArrayList<EObject>(1);
			ports2.add(port3);

			final List<EObject> allPorts = new ArrayList<EObject>(3);
			allPorts.add(port1);
			allPorts.add(port2);
			allPorts.add(port3);
			portModel.eSet(feature_PortModel_ports, allPorts);

			final EObject analyticsModel = analyticsFactory.create(class_AnalyticsModel);

			// No price values set -- no change
			final EObject matrix1 = analyticsFactory.create(class_UnitCostMatrix);
			matrix1.eSet(feature_ports, ports);
			matrix1.eSet(feature_returnIdleTime, 5);
			matrix1.eSet(feature_dischargeIdleTime, 10);

			final EObject matrix2 = analyticsFactory.create(class_UnitCostMatrix);
			matrix2.eSet(feature_ports, ports);
			matrix2.eSet(feature_toPorts, ports2);
			matrix2.eSet(feature_fromPorts, ports2);

			final List<EObject> matrics = new ArrayList<EObject>(2);
			matrics.add(matrix1);
			matrics.add(matrix2);

			analyticsModel.eSet(feature_roundTripMatrices, matrics);

			// Save to tmp file

			{
				portModelFile = File.createTempFile("migrationtest-ports", ".xmi");

				final Resource r = v0Loader.getResourceSet().createResource(URI.createFileURI(portModelFile.toString()));
				r.getContents().add(portModel);
				r.save(null);
			}
			{
				analyticsModelFile = File.createTempFile("migrationtest-analytics", ".xmi");

				final Resource r = v0Loader.getResourceSet().createResource(URI.createFileURI(analyticsModelFile.toString()));
				r.getContents().add(analyticsModel);
				r.save(null);
			}
		}

		// Load v1 metamodels
		// Load tmp file under v1
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();

			final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
			// Record features which have no meta-model equivalent so we can perform migration
			loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

			final Resource portResource = v1Loader.getResourceSet().createResource(URI.createFileURI(portModelFile.toString()));
			portResource.load(loadOptions);
			final Resource analyticsResource = v1Loader.getResourceSet().createResource(URI.createFileURI(analyticsModelFile.toString()));
			analyticsResource.load(loadOptions);

			final EObject analyticsModel = analyticsResource.getContents().get(0);
			final EObject portModel = portResource.getContents().get(0);

			final Map<ModelsLNGSet_v1, EObject> models = new HashMap<ModelsLNGSet_v1, EObject>();
			models.put(ModelsLNGSet_v1.Analytics, analyticsModel);
			models.put(ModelsLNGSet_v1.Port, portModel);
			// Run migration.
			migrator.removeExtraAnalyticsFields(v1Loader, models);

			portResource.save(null);
			analyticsResource.save(null);
		}

		// Check output
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();
			final Resource portResource = v1Loader.getResourceSet().createResource(URI.createFileURI(portModelFile.toString()));
			portResource.load(null);
			final Resource analyticsResource = v1Loader.getResourceSet().createResource(URI.createFileURI(analyticsModelFile.toString()));
			analyticsResource.load(null);
			portModelFile.delete();
			analyticsModelFile.delete();

			final EPackage analyticsPackage = v1Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

			final EClass class_AnalyticsModel = MetamodelUtils.getEClass(analyticsPackage, "AnalyticsModel");
			final EStructuralFeature feature_roundTripMatrices = MetamodelUtils.getStructuralFeature(class_AnalyticsModel, "roundTripMatrices");

			final EClass class_UnitCostMatrix = MetamodelUtils.getEClass(analyticsPackage, "UnitCostMatrix");
			final EStructuralFeature feature_toPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "toPorts");
			final EStructuralFeature feature_fromPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "fromPorts");

			final EObject analyticsModel = analyticsResource.getContents().get(0);

			// No unknown features have been left over
			Assert.assertTrue(((XMLResource) analyticsResource).getEObjectToExtensionMap().isEmpty());

			final List<EObject> matrices = MetamodelUtils.getValueAsTypedList(analyticsModel, feature_roundTripMatrices);

			// No price values set -- no change
			final EObject matrix1 = matrices.get(0);
			Assert.assertTrue(matrix1.eIsSet(feature_toPorts));
			Assert.assertTrue(matrix1.eIsSet(feature_fromPorts));

			Assert.assertEquals(2, MetamodelUtils.getValueAsTypedList(matrix1, feature_toPorts).size());
			Assert.assertEquals(2, MetamodelUtils.getValueAsTypedList(matrix1, feature_fromPorts).size());

			final EObject matrix2 = matrices.get(1);
			Assert.assertTrue(matrix2.eIsSet(feature_toPorts));
			Assert.assertTrue(matrix2.eIsSet(feature_fromPorts));

			Assert.assertEquals(1, MetamodelUtils.getValueAsTypedList(matrix2, feature_toPorts).size());
			Assert.assertEquals(1, MetamodelUtils.getValueAsTypedList(matrix2, feature_fromPorts).size());

		}
	}

	@Test
	public void testClearAssignments() throws IOException {

		// Load v0 metamodels

		// Construct a scenario
		File inputModelFile = null;
		File fleetModelFile = null;
		{
			final MetamodelLoader v0Loader = new MigrateToV1().getSourceMetamodelLoader();
			final EPackage inputPackage = v0Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_InputModel);
			final EFactory inputFactory = inputPackage.getEFactoryInstance();

			final EClass class_inputModel = MetamodelUtils.getEClass(inputPackage, "InputModel");
			final EStructuralFeature feature_InputModel_assignments = MetamodelUtils.getStructuralFeature(class_inputModel, "assignments");

			final EClass class_Assignment = MetamodelUtils.getEClass(inputPackage, "Assignment");
			final EStructuralFeature feature_vessels = MetamodelUtils.getStructuralFeature(class_Assignment, "vessels");
			final EStructuralFeature feature_assignToSpot = MetamodelUtils.getStructuralFeature(class_Assignment, "assignToSpot");
			final EStructuralFeature feature_assignedObjects = MetamodelUtils.getStructuralFeature(class_Assignment, "assignedObjects");

			final EPackage vesselPackage = v0Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);
			final EFactory fleetFactory = vesselPackage.getEFactoryInstance();

			final EClass class_FleetModel = MetamodelUtils.getEClass(vesselPackage, "FleetModel");
			final EStructuralFeature feature_FleetModel_vessels = MetamodelUtils.getStructuralFeature(class_FleetModel, "vessels");

			final EClass class_Vessel = MetamodelUtils.getEClass(vesselPackage, "Vessel");

			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			final EObject fleetModel = fleetFactory.create(class_FleetModel);

			final EObject vessel1 = fleetFactory.create(class_Vessel);
			final EObject vessel2 = fleetFactory.create(class_Vessel);
			final EObject vessel3 = fleetFactory.create(class_Vessel);
			final EObject vessel4 = fleetFactory.create(class_Vessel);

			final List<EObject> allVessels = Lists.newArrayList(vessel1, vessel2, vessel3, vessel4);
			fleetModel.eSet(feature_FleetModel_vessels, allVessels);

			final EObject assignment1 = inputFactory.create(class_Assignment);
			assignment1.eSet(feature_vessels, Lists.newArrayList(vessel1));
			assignment1.eSet(feature_assignedObjects, Lists.newArrayList(vessel2));
			assignment1.eSet(feature_assignToSpot, true);

			final EObject assignment2 = inputFactory.create(class_Assignment);
			assignment2.eSet(feature_vessels, Lists.newArrayList(vessel3));
			assignment2.eSet(feature_assignedObjects, Lists.newArrayList(vessel4));
			assignment2.eSet(feature_assignToSpot, false);

			final EObject inputModel = inputFactory.create(class_inputModel);
			inputModel.eSet(feature_InputModel_assignments, Lists.newArrayList(assignment1, assignment2));

			// Save to tmp file

			{
				fleetModelFile = File.createTempFile("migrationtest-vessel", ".xmi");

				final Resource r = v0Loader.getResourceSet().createResource(URI.createFileURI(fleetModelFile.toString()));
				r.getContents().add(fleetModel);
				r.save(null);
			}
			{
				inputModelFile = File.createTempFile("migrationtest-input", ".xmi");

				final Resource r = v0Loader.getResourceSet().createResource(URI.createFileURI(inputModelFile.toString()));
				r.getContents().add(inputModel);
				r.save(null);
			}
		}

		// Load v1 metamodels
		// Load tmp file under v1
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();

			final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
			// Record features which have no meta-model equivalent so we can perform migration
			loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

			final Resource vesselResource = v1Loader.getResourceSet().createResource(URI.createFileURI(fleetModelFile.toString()));
			vesselResource.load(loadOptions);
			final Resource inputResource = v1Loader.getResourceSet().createResource(URI.createFileURI(inputModelFile.toString()));
			inputResource.load(loadOptions);

			final EObject inputModel = inputResource.getContents().get(0);
			final EObject vesselModel = vesselResource.getContents().get(0);

			final Map<ModelsLNGSet_v1, EObject> models = new HashMap<ModelsLNGSet_v1, EObject>();
			models.put(ModelsLNGSet_v1.Input, inputModel);
			models.put(ModelsLNGSet_v1.Fleet, vesselModel);
			// Run migration.
			migrator.clearAssignments(v1Loader, models);

			vesselResource.save(null);
			inputResource.save(null);
		}

		// Check output
		{
			final MigrateToV1 migrator = new MigrateToV1();

			final MetamodelLoader v1Loader = migrator.getDestinationMetamodelLoader();
			final Resource fleetResource = v1Loader.getResourceSet().createResource(URI.createFileURI(fleetModelFile.toString()));
			fleetResource.load(null);
			final Resource inputResource = v1Loader.getResourceSet().createResource(URI.createFileURI(inputModelFile.toString()));
			inputResource.load(null);
			inputModelFile.delete();
			fleetModelFile.delete();

			final EPackage inputPackage = v1Loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_InputModel);

			final EClass class_inputModel = MetamodelUtils.getEClass(inputPackage, "InputModel");
			final EStructuralFeature feature_InputModel_assignments = MetamodelUtils.getStructuralFeature(class_inputModel, "assignments");

			final EObject inputModel = inputResource.getContents().get(0);
			Assert.assertNotNull(inputModel);

			// No unknown features have been left over
			Assert.assertTrue(((XMLResource) inputResource).getEObjectToExtensionMap().isEmpty());
			Assert.assertFalse(inputModel.eIsSet(feature_InputModel_assignments));

		}
	}

}
