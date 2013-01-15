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

		final MigrateToV1 migrator = new MigrateToV1();

		// Load v0 metamodels

		// Construct a scenario
		File tmpFile = null;
		{
			final MetamodelLoader v0Loader = migrator.createSourceMetamodelLoader();
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

			final MetamodelLoader v1Loader = migrator.createDestinationMetamodelLoader();

			final Resource r = v1Loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

			final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
			// Record features which have no meta-model equivalent so we can perform migration
			loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
			r.load(loadOptions);
			EObject cargoModel = r.getContents().get(0);

			Map<ModelsLNGSet_v1, EObject> models = new HashMap<ModelsLNGSet_v1, EObject>();
			models.put(ModelsLNGSet_v1.Cargo, cargoModel);
			// Run migration.
			migrator.clearFixedPrice(v1Loader, models);

			r.save(null);
		}

		// Check output
		{

			final MetamodelLoader v1Loader = migrator.createDestinationMetamodelLoader();
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

			EObject cargoModel = r.getContents().get(0);

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
}
