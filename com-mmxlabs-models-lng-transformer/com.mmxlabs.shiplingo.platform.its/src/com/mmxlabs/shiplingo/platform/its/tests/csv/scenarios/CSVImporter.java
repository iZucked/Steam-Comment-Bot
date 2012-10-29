package com.mmxlabs.shiplingo.platform.its.tests.csv.scenarios;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.importer.CargoModelImporter;
import com.mmxlabs.models.lng.commercial.importer.CommercialModelImporter;
import com.mmxlabs.models.lng.fleet.importer.FleetModelImporter;
import com.mmxlabs.models.lng.input.importers.InputModelImporter;
import com.mmxlabs.models.lng.port.importer.PortModelImporter;
import com.mmxlabs.models.lng.pricing.importers.PricingModelImporter;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.shiplingo.platform.its.internal.Activator;
import com.mmxlabs.shiplingo.platform.its.tests.ManifestJointModel;

public class CSVImporter {

	public static MMXRootObject importCSVScenario(String baseFileName) {

		Map<String, String> dataMap = new HashMap<String, String>();

		// No analytics model importers

		dataMap.put(CargoModelImporter.CARGO_KEY, baseFileName + "/" + "Cargoes.csv");
		dataMap.put(CargoModelImporter.CARGO_GROUP_KEY, baseFileName + "/" + "Cargo Groups.csv");

		dataMap.put(CommercialModelImporter.ENTITIES_KEY, baseFileName + "/" + "Entities.csv");
		dataMap.put(CommercialModelImporter.PURCHASE_CON_KEY, baseFileName + "/" + "Purchase Contracts.csv");
		dataMap.put(CommercialModelImporter.SALES_CON_KEY, baseFileName + "/" + "Sales Contracts.csv");

		dataMap.put(FleetModelImporter.CURVES_KEY, baseFileName + "/" + "Consumption Curves.csv");
		dataMap.put(FleetModelImporter.FUELS_KEY, baseFileName + "/" + "Base Fuels.csv");
		dataMap.put(FleetModelImporter.GROUPS_KEY, baseFileName + "/" + "Vessel Groups.csv");
		dataMap.put(FleetModelImporter.VESSEL_CLASSES_KEY, baseFileName + "/" + "Vessel Classes.csv");
		dataMap.put(FleetModelImporter.VESSELS_KEY, baseFileName + "/" + "Vessels.csv");
		dataMap.put(FleetModelImporter.EVENTS_KEY, baseFileName + "/" + "Vessel Events.csv");

		dataMap.put(InputModelImporter.ASSIGNMENTS, baseFileName + "/" + "Assignments.csv");

		// No optimiser model importers

		dataMap.put(PortModelImporter.PORT_KEY, baseFileName + "/" + "Ports.csv");
		dataMap.put(PortModelImporter.PORT_GROUP_KEY, baseFileName + "/" + "Port Groups.csv");
		dataMap.put(PortModelImporter.DISTANCES_KEY, baseFileName + "/" + "Distance Matrix.csv");
		dataMap.put(PortModelImporter.SUEZ_KEY, baseFileName + "/" + "Suez Distance Matrix.csv");

		dataMap.put(PricingModelImporter.CHARTER_CURVE_KEY, baseFileName + "/" + "Charter Curves.csv");
		dataMap.put(PricingModelImporter.CHARTER_PRICING_KEY, baseFileName + "/" + "Charter Rates.csv");
		dataMap.put(PricingModelImporter.COOLDOWN_PRICING_KEY, baseFileName + "/" + "Cooldown Prices.csv");
		dataMap.put(PricingModelImporter.PORT_COSTS_KEY, baseFileName + "/" + "Port Costs.csv");
		dataMap.put(PricingModelImporter.PRICE_CURVE_KEY, baseFileName + "/" + "Commodity Curves.csv");
		dataMap.put(PricingModelImporter.SPOT_CARGO_MARKETS_KEY, baseFileName + "/" + "Spot Cargo Markets.csv");

		// No schedule importers

		final DefaultImportContext context = new DefaultImportContext();

		final MMXRootObject root = MMXCoreFactory.eINSTANCE.createMMXRootObject();
		IImporterRegistry importerRegistry = Activator.getDefault().getImporterRegistry();
		for (final EClass subModelClass : ManifestJointModel.getSubmodelClasses()) {
			final ISubmodelImporter importer = importerRegistry.getSubmodelImporter(subModelClass);
			if (importer == null)
				continue;
			final Map<String, String> parts = importer.getRequiredInputs();
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			for (String key : parts.keySet()) {
				try {
					final CSVReader r = new CSVReader(baseFileName, dataMap.get(key));
					readers.put(key, r);
				} catch (final IOException e) {
					Assert.fail(e.getMessage());
				}
			}
			try {
				final UUIDObject subModel = importer.importModel(readers, context);
				root.addSubModel(subModel);
			} catch (final Throwable th) {
				Assert.fail(th.getMessage());
			}

		}

		context.setRootObject(root);

		context.run();
		return root;
	}
}
