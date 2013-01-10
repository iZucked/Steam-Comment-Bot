package com.mmxlabs.models.lng.transformer.its.tests.csv.scenarios;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Assert;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoModelImporter;
import com.mmxlabs.models.lng.cargo.importer.DischargeSlotImporter;
import com.mmxlabs.models.lng.cargo.importer.LoadSlotImporter;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.importer.CommercialModelImporter;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.importer.BaseFuelImporter;
import com.mmxlabs.models.lng.fleet.importer.FleetModelImporter;
import com.mmxlabs.models.lng.fleet.importer.VesselClassImporter;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.importers.InputModelImporter;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.importers.OptimiserModelImporter;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.importer.PortModelImporter;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.importers.DataIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.PortCostImporter;
import com.mmxlabs.models.lng.pricing.importers.PricingModelImporter;
import com.mmxlabs.models.lng.pricing.importers.SpotMarketImporter;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.importers.ScheduleModelImporter;
import com.mmxlabs.models.lng.transformer.its.internal.Activator;
import com.mmxlabs.models.lng.transformer.its.tests.ManifestJointModel;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.models.util.importer.registry.impl.ImporterRegistry;

public class CSVImporter {

	public static MMXRootObject importCSVScenario(final String baseFileName) {

		final Map<String, String> dataMap = new HashMap<String, String>();

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
		final IImporterRegistry importerRegistry = getImporterRegistry();
		for (final EClass subModelClass : ManifestJointModel.getSubmodelClasses()) {
			final ISubmodelImporter importer = importerRegistry.getSubmodelImporter(subModelClass);
			if (importer == null)
				continue;
			final Map<String, String> parts = importer.getRequiredInputs();
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			for (final String key : parts.keySet()) {
				try {
					final CSVReader r = new CSVReader(baseFileName, dataMap.get(key));
					readers.put(key, r);
				} catch (final IOException e) {
					// Assert.fail(e.getMessage());
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

	private static IImporterRegistry getImporterRegistry() {

		IImporterRegistry importerRegistry = null;
		{
			// Full plug-in env
			final Activator activator = Activator.getDefault();
			if (activator != null) {
				importerRegistry = activator.getImporterRegistry();
			} else {
				final BundleContext bundleContext = InternalPlatform.getDefault().getBundleContext();
				if (bundleContext != null) {
					// Partial plugin env - its bundle activator does not seem to be started when run from eclipse.
					final Injector injector = Guice.createInjector(new ExtensionConfigurationModule(bundleContext));
					importerRegistry = new ImporterRegistry();
					injector.injectMembers(importerRegistry);
				} else {

					final Map<EClass, ISubmodelImporter> subModelImporters = new HashMap<EClass, ISubmodelImporter>();
					subModelImporters.put(CargoPackage.eINSTANCE.getCargoModel(), new CargoModelImporter());
					subModelImporters.put(CommercialPackage.eINSTANCE.getCommercialModel(), new CommercialModelImporter());
					subModelImporters.put(FleetPackage.eINSTANCE.getFleetModel(), new FleetModelImporter());
					subModelImporters.put(InputPackage.eINSTANCE.getInputModel(), new InputModelImporter());
					subModelImporters.put(OptimiserPackage.eINSTANCE.getOptimiserModel(), new OptimiserModelImporter());
					subModelImporters.put(PortPackage.eINSTANCE.getPortModel(), new PortModelImporter());
					subModelImporters.put(PricingPackage.eINSTANCE.getPricingModel(), new PricingModelImporter());
					subModelImporters.put(SchedulePackage.eINSTANCE.getScheduleModel(), new ScheduleModelImporter());

					final Map<EClass, IClassImporter> classImporters = new HashMap<EClass, IClassImporter>();
					classImporters.put(FleetPackage.eINSTANCE.getBaseFuel(), new BaseFuelImporter());
					classImporters.put(CargoPackage.eINSTANCE.getCargo(), new CargoImporter());
					classImporters.put(CargoPackage.eINSTANCE.getDischargeSlot(), new DischargeSlotImporter());
					classImporters.put(CargoPackage.eINSTANCE.getLoadSlot(), new LoadSlotImporter());
					classImporters.put(PricingPackage.eINSTANCE.getPortCost(), new PortCostImporter());
					classImporters.put(PricingPackage.eINSTANCE.getSpotMarket(), new SpotMarketImporter());
					classImporters.put(FleetPackage.eINSTANCE.getVesselClass(), new VesselClassImporter());
					classImporters.put(PricingPackage.eINSTANCE.getDataIndex(), new DataIndexImporter());

					final DateAttributeImporter dateAttributeImporter = new DateAttributeImporter();
					final DefaultClassImporter defaultClassImporter = new DefaultClassImporter();
					final DefaultAttributeImporter defaultAttributeImporter = new DefaultAttributeImporter();

					// No plugins
					importerRegistry = new IImporterRegistry() {

						@Override
						public ISubmodelImporter getSubmodelImporter(final EClass subModelClass) {
							return subModelImporters.get(subModelClass);
						}

						@Override
						public IClassImporter getClassImporter(final EClass eClass) {
							if (classImporters.containsKey(eClass)) {
								return classImporters.get(eClass);
							}

							return defaultClassImporter;
						}

						@Override
						public IAttributeImporter getAttributeImporter(final EDataType dataType) {

							if (dataType.equals(EcorePackage.eINSTANCE.getEDate())) {
								return dateAttributeImporter;
							}

							return defaultAttributeImporter;
						}

						@Override
						public Collection<ISubmodelImporter> getAllSubModelImporters() {
							return subModelImporters.values();
						}
					};

					final IImporterRegistry finalRef = importerRegistry;
					final Injector injector = Guice.createInjector(new AbstractModule() {

						@Override
						protected void configure() {
							bind(IImporterRegistry.class).toInstance(finalRef);
						}
					});

					for (final ISubmodelImporter importer : subModelImporters.values()) {
						injector.injectMembers(importer);
					}
					for (final IClassImporter importer : classImporters.values()) {
						injector.injectMembers(importer);
					}
					injector.injectMembers(defaultAttributeImporter);
					injector.injectMembers(dateAttributeImporter);
					injector.injectMembers(defaultClassImporter);
				}
			}
		}

		return importerRegistry;
	}
}
