/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.actuals.importer.ActualsModelExtraImporter;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.importer.AssignmentModelImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoModelImporter;
import com.mmxlabs.models.lng.cargo.importer.DischargeSlotImporter;
import com.mmxlabs.models.lng.cargo.importer.InventoryExtraImporter;
import com.mmxlabs.models.lng.cargo.importer.LoadSlotImporter;
import com.mmxlabs.models.lng.cargo.importer.VesselAvailabilityBallastBonusImporterExtraImporter;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.importer.CharterContractBallastBonusImporterExtraImporter;
import com.mmxlabs.models.lng.commercial.importer.CommercialModelImporter;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.importer.BaseFuelImporter;
import com.mmxlabs.models.lng.fleet.importer.FleetModelImporter;
import com.mmxlabs.models.lng.fleet.importer.VesselImporter;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.importer.PortModelImporter;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.importers.BaseFuelIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.CharterIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.CommodityIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.CostModelImporter;
import com.mmxlabs.models.lng.pricing.importers.CurrencyIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.DataIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.PortCostImporter;
import com.mmxlabs.models.lng.pricing.importers.PricingModelImporter;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.ui.importers.PromptPeriodImporter;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.importers.ScheduleModelImporter;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.editor.importers.SpotMarketImporter;
import com.mmxlabs.models.lng.spotmarkets.editor.importers.SpotMarketsModelImporter;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.models.util.importer.registry.ExtensionConfigurationModule;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.models.util.importer.registry.impl.ImporterRegistry;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class CopiedCSVImporter {

	private final @NonNull Map<String, URI> dataMap = new HashMap<>();

	public void importPortData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(PortModelImporter.PORT_KEY, createURI(uriRoot, "Ports.csv"));
		dataMap.put(PortModelImporter.PORT_GROUP_KEY, createURI(uriRoot, "Port Groups.csv"));
		dataMap.put(PortModelImporter.DISTANCES_KEY, createURI(uriRoot, "Distance Matrix.csv"));
		dataMap.put(PortModelImporter.SUEZ_KEY, createURI(uriRoot, "Suez Distance Matrix.csv"));
		dataMap.put(PortModelImporter.PANAMA_KEY, createURI(uriRoot, "Panama Distance Matrix.csv"));
		dataMap.put(PortModelImporter.CANAL_PORTS_KEY, createURI(uriRoot, "Canal Ports.csv"));
		dataMap.put(PortModelImporter.CONTINGENCY_MATRIX_KEY, createURI(uriRoot, "Contingency Matrix.csv"));
	}

	public void importFleetData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(FleetModelImporter.VESSELS_KEY, createURI(uriRoot, "Vessels.csv"));
		dataMap.put(FleetModelImporter.CURVES_KEY, createURI(uriRoot, "Consumption Curves.csv"));
		dataMap.put(FleetModelImporter.GROUPS_KEY, createURI(uriRoot, "Vessel Groups.csv"));
		dataMap.put(FleetModelImporter.FUELS_KEY, createURI(uriRoot, "Base Fuels.csv"));
	}

	public void importEntityData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(CommercialModelImporter.ENTITIES_KEY, createURI(uriRoot, "Entities.csv"));
		dataMap.put(CommercialModelImporter.ENTITY_BOOKS_KEY, createURI(uriRoot, "Entity Books.csv"));
	}

	public void importContractData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(CommercialModelImporter.PURCHASE_CON_KEY, createURI(uriRoot, "Purchase Contracts.csv"));
		dataMap.put(CommercialModelImporter.SALES_CON_KEY, createURI(uriRoot, "Sales Contracts.csv"));
		dataMap.put(CommercialModelImporter.CHARTER_CON_KEY, createURI(uriRoot, "Charter Contracts.csv"));
		dataMap.put(CharterContractBallastBonusImporterExtraImporter.BALLASTBONUS_KEY, createURI(uriRoot, "Charter Contracts--Ballast Bonus.csv"));

	}

	public void importMarketData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(PricingModelImporter.PRICE_CURVE_KEY, createURI(uriRoot, "Commodity Curves.csv"));
		dataMap.put(PricingModelImporter.BASEFUEL_PRICING_KEY, createURI(uriRoot, "Base Fuel Curves.csv"));
		dataMap.put(PricingModelImporter.CHARTER_CURVE_KEY, createURI(uriRoot, "Charter Curves.csv"));
		dataMap.put(PricingModelImporter.CONVERSION_FACTORS_KEY, createURI(uriRoot, "Conversion Factors.csv"));
		dataMap.put(PricingModelImporter.SETTLED_PRICES_KEY, createURI(uriRoot, "Settled Prices.csv"));
	}

	public void importCostData(@NonNull final String uriRoot) throws MalformedURLException {

		dataMap.put(CostModelImporter.COOLDOWN_PRICING_KEY, createURI(uriRoot, "Cooldown Prices.csv"));
		dataMap.put(CostModelImporter.PORT_COSTS_KEY, createURI(uriRoot, "Port Costs.csv"));
		dataMap.put(CostModelImporter.ROUTE_COSTS_KEY, createURI(uriRoot, "Route Costs.csv"));
		dataMap.put(CostModelImporter.PANAMA_CANAL_TARIFF_KEY, createURI(uriRoot, "Panama Canal Tariff.csv"));
		dataMap.put(CostModelImporter.SUEZ_CANAL_TARIFF_KEY, createURI(uriRoot, "Suez Canal Tariff.csv"));
	}

	public void importPromptData(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(PromptPeriodImporter.PROMPT_PERIOD_KEY, createURI(uriRoot, "Prompt Period.csv"));
	}

	public void importSpotMarkets(@NonNull final String uriRoot) throws MalformedURLException {
		dataMap.put(SpotMarketsModelImporter.CHARTER_PRICING_KEY, createURI(uriRoot, "Charter Markets.csv"));
		dataMap.put(SpotMarketsModelImporter.SPOT_CARGO_MARKETS_KEY, createURI(uriRoot, "Spot Cargo Markets.csv"));
	}

	public void importPorfolioData(@NonNull final String uriRoot) throws MalformedURLException {

		dataMap.put(CargoModelImporter.CARGO_KEY, createURI(uriRoot, "Cargoes.csv"));
		dataMap.put(CargoModelImporter.PAPER_DEALS_KEY, createURI(uriRoot, "Paper Deals.csv"));
		dataMap.put(CargoModelImporter.CARGO_GROUP_KEY, createURI(uriRoot, "Cargo Groups.csv"));
		dataMap.put(CargoModelImporter.EVENTS_KEY, createURI(uriRoot, "Events.csv"));
		dataMap.put(CargoModelImporter.VESSEL_AVAILABILITY_KEY, createURI(uriRoot, "Vessel Availability.csv"));
		dataMap.put(AssignmentModelImporter.ASSIGNMENTS, createURI(uriRoot, "Assignments.csv"));
		dataMap.put(CargoModelImporter.CANAL_BOOKINGS_KEY, createURI(uriRoot, "Canal Bookings.csv"));
		dataMap.put(VesselAvailabilityBallastBonusImporterExtraImporter.BALLAST_BONUS_KEY, createURI(uriRoot, "Vessel Availability--Ballast Bonus.csv"));
		dataMap.put(InventoryExtraImporter.INVENTORY_KEY, createURI(uriRoot, "Inventories.csv"));
	}

	public void importStandardComponents(@NonNull final String uriRoot) throws MalformedURLException {
		importMarketData(uriRoot);
		importPortData(uriRoot);
		importFleetData(uriRoot);
		importCostData(uriRoot);
		importEntityData(uriRoot);
		importContractData(uriRoot);
		importSpotMarkets(uriRoot);
		importPorfolioData(uriRoot);
		importPromptData(uriRoot);
	}

	@NonNull
	public URI createURI(final String uriRoot, final String filename) throws MalformedURLException {
		return URI.createURI(String.format("%s/%s", uriRoot, filename));
	}

	public void addExtraMapEntry(@NonNull final String key, @NonNull final URI uri) {
		dataMap.put(key, uri);
	}

	@NonNull
	public Map<String, URI> getDataMap() {
		return dataMap;
	}

	public static @NonNull IScenarioDataProvider importCSVScenario(@NonNull final String uriRoot, final String... extraMapEntries) throws MalformedURLException {

		final CopiedCSVImporter importer = new CopiedCSVImporter();

		importer.importStandardComponents(uriRoot);

		for (int i = 0; i < extraMapEntries.length; i += 2) {
			if (i + 1 < extraMapEntries.length) {
				final String key = extraMapEntries[i];
				assert key != null;
				final String value = extraMapEntries[i + 1];
				assert value != null;
				importer.addExtraMapEntry(key, URI.createURI(value));
			}
		}
		return importCSVScenario(importer.dataMap);
	}

	public static @NonNull IScenarioDataProvider importCSVScenario(@NonNull final Map<String, URI> dataMap) {

		final IImporterRegistry importerRegistry = getImporterRegistry();
		return importCSVScenario(dataMap, importerRegistry);
	}

	public static @NonNull IScenarioDataProvider importCSVScenario(final @NonNull Map<String, URI> dataMap, @NonNull final IImporterRegistry importerRegistry) {

		final DefaultImportContext context = new DefaultImportContext('.');

		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		scenarioModel.setCargoModel((CargoModel) importSubModel(importerRegistry, context, dataMap, CargoPackage.eINSTANCE.getCargoModel()));
		scenarioModel.setScheduleModel((ScheduleModel) importSubModel(importerRegistry, context, dataMap, SchedulePackage.eINSTANCE.getScheduleModel()));
		scenarioModel.setAnalyticsModel(((AnalyticsModel) importSubModel(importerRegistry, context, dataMap, AnalyticsPackage.eINSTANCE.getAnalyticsModel())));
		if (scenarioModel.getAnalyticsModel() == null) {
			scenarioModel.setAnalyticsModel(AnalyticsFactory.eINSTANCE.createAnalyticsModel());
		}

		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		scenarioModel.setReferenceModel(referenceModel);

		referenceModel.setPortModel((PortModel) importSubModel(importerRegistry, context, dataMap, PortPackage.eINSTANCE.getPortModel()));
		referenceModel.setFleetModel((FleetModel) importSubModel(importerRegistry, context, dataMap, FleetPackage.eINSTANCE.getFleetModel()));
		referenceModel.setPricingModel((PricingModel) importSubModel(importerRegistry, context, dataMap, PricingPackage.eINSTANCE.getPricingModel()));
		referenceModel.setCostModel((CostModel) importSubModel(importerRegistry, context, dataMap, PricingPackage.eINSTANCE.getCostModel()));
		referenceModel.setCommercialModel((CommercialModel) importSubModel(importerRegistry, context, dataMap, CommercialPackage.eINSTANCE.getCommercialModel()));
		referenceModel.setSpotMarketsModel((SpotMarketsModel) importSubModel(importerRegistry, context, dataMap, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel()));

		// Post import map port names to MMX ID
//		{
//			Map<String, String> portNameToID = new HashMap<>();
//			for (Port port : referenceModel.getPortModel().getPorts()) {
//				Location l = port.getLocation();
//				if (l.getName() == null) {
//					l.setName(port.getName());
//				}
//				String mmxId = l.getMmxId();
//				if (mmxId == null) {
//					mmxId = port.getName();
//					l.setMmxId(mmxId);
//				}
//				// Restores correct case
//				portNameToID.put(mmxId.toLowerCase(), mmxId);
//				
//				portNameToID.put(port.getName().toLowerCase(), mmxId);
//				portNameToID.put(l.getName().toLowerCase(), mmxId);
//				for (String other : l.getOtherNames()) {
//					portNameToID.put(other.toLowerCase(), mmxId);
//				}
//			}
//		}
		importExtraModels(scenarioModel, importerRegistry, context, dataMap);

		context.setRootObject(scenarioModel);

		context.run();

		for (final IPostModelImporter postModelImporter : importerRegistry.getPostModelImporters()) {
			postModelImporter.onPostModelImport(context, scenarioModel);
		}

		final SimpleScenarioDataProvider scenarioDataProvider = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), scenarioModel);

		return scenarioDataProvider;
	}

	@NonNull
	private static IImporterRegistry getImporterRegistry() {

		// IImporterRegistry importerRegistry = null;
		{
			// Full plug-in env
			final Activator activator = Activator.getDefault();
			if (activator != null) {
				final IImporterRegistry importerRegistry = activator.getImporterRegistry();
				assert importerRegistry != null;
				return importerRegistry;
			} else {
				final Bundle bundle = FrameworkUtil.getBundle(CopiedCSVImporter.class);
				final BundleContext bundleContext = bundle != null ? bundle.getBundleContext() : null;
				if (bundleContext != null) {
					// Partial plugin env - its bundle activator does not seem to be started when run from eclipse.
					final Injector injector = Guice.createInjector(new ExtensionConfigurationModule(bundleContext));
					final IImporterRegistry importerRegistry = new ImporterRegistry();
					injector.injectMembers(importerRegistry);
					return importerRegistry;
				} else {

					final Map<EClass, ISubmodelImporter> subModelImporters = new HashMap<>();
					subModelImporters.put(CargoPackage.eINSTANCE.getCargoModel(), new CargoModelImporter());
					subModelImporters.put(CommercialPackage.eINSTANCE.getCommercialModel(), new CommercialModelImporter());
					subModelImporters.put(FleetPackage.eINSTANCE.getFleetModel(), new FleetModelImporter());
					// subModelImporters.put(AssignmentPackage.eINSTANCE.getAssignmentModel(), new AssignmentModelImporter());
					// subModelImporters.put(ParametersPackage.eINSTANCE.getParametersModel(), new ParametersModelImporter());
					subModelImporters.put(PortPackage.eINSTANCE.getPortModel(), new PortModelImporter());
					subModelImporters.put(PricingPackage.eINSTANCE.getCostModel(), new CostModelImporter());
					subModelImporters.put(PricingPackage.eINSTANCE.getPricingModel(), new PricingModelImporter());
					subModelImporters.put(SchedulePackage.eINSTANCE.getScheduleModel(), new ScheduleModelImporter());
					subModelImporters.put(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel(), new SpotMarketsModelImporter());
					// subModelImporters.put(ActualsPackage.eINSTANCE.getActualsModel(), new ActualsModelImporter());

					final Map<EClass, IClassImporter> classImporters = new HashMap<>();
					classImporters.put(FleetPackage.eINSTANCE.getBaseFuel(), new BaseFuelImporter());
					classImporters.put(CargoPackage.eINSTANCE.getCargo(), new CargoImporter());
					classImporters.put(CargoPackage.eINSTANCE.getDischargeSlot(), new DischargeSlotImporter());
					classImporters.put(CargoPackage.eINSTANCE.getLoadSlot(), new LoadSlotImporter());
					classImporters.put(PricingPackage.eINSTANCE.getPortCost(), new PortCostImporter());
					classImporters.put(SpotMarketsPackage.eINSTANCE.getSpotMarket(), new SpotMarketImporter());
					classImporters.put(FleetPackage.eINSTANCE.getVessel(), new VesselImporter());
					classImporters.put(PricingPackage.eINSTANCE.getDataIndex(), new DataIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getBunkerFuelCurve(), new BaseFuelIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCurrencyCurve(), new CurrencyIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCharterCurve(), new CharterIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCommodityCurve(), new CommodityIndexImporter());

					final List<IPostModelImporter> portModelImporters = new ArrayList<>();
					final List<IExtraModelImporter> extraModelImporters = new ArrayList<>();
					extraModelImporters.add(new ActualsModelExtraImporter());
					extraModelImporters.add(new VesselAvailabilityBallastBonusImporterExtraImporter());

					final DefaultClassImporter defaultClassImporter = new DefaultClassImporter();
					final DefaultAttributeImporter defaultAttributeImporter = new DefaultAttributeImporter();

					// No plugins
					final IImporterRegistry importerRegistry = new IImporterRegistry() {

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

							return defaultAttributeImporter;
						}

						@Override
						public Collection<ISubmodelImporter> getAllSubModelImporters() {
							return subModelImporters.values();
						}

						@Override
						public Collection<IPostModelImporter> getPostModelImporters() {
							return portModelImporters;
						}

						@Override
						public Collection<IExtraModelImporter> getExtraModelImporters() {
							return extraModelImporters;
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
					injector.injectMembers(defaultClassImporter);

					return importerRegistry;
				}
			}
		}

	}

	private static EObject importSubModel(@NonNull final IImporterRegistry importerRegistry, @NonNull final IMMXImportContext context, @NonNull final Map<String, URI> dataMap,
			@NonNull final EClass subModelClass) {

		final ISubmodelImporter importer = importerRegistry.getSubmodelImporter(subModelClass);
		if (importer == null) {
			return null;
		}
		final Map<String, String> parts = importer.getRequiredInputs();
		final Map<String, CSVReader> readers = new HashMap<>();
		try {
			for (final String key : parts.keySet()) {
				try {
					final URI uri = dataMap.get(key);
					if (uri != null) {
						@SuppressWarnings("resource")
						final CSVReader r = new FileCSVReader(uri);
						readers.put(key, r);
					}
				} catch (final IOException e) {
					// e.printStackTrace();
					// Assertions.fail(e.getMessage());
				}
			}
			try {
				return importer.importModel(readers, context);
			} catch (final Throwable th) {
				th.printStackTrace();
			}
		} finally {
			for (final CSVReader r : readers.values()) {
				try {
					r.close();
				} catch (final IOException e) {

				}
			}
		}
		return null;
	}

	private static void importExtraModels(@NonNull final LNGScenarioModel scenarioModel, @NonNull final IImporterRegistry importerRegistry, @NonNull final IMMXImportContext context,
			@NonNull final Map<String, URI> dataMap) {
		for (final IExtraModelImporter importer : importerRegistry.getExtraModelImporters()) {
			if (importer == null) {
				continue;
			}
			final Map<String, String> parts = importer.getRequiredInputs();
			final Map<String, CSVReader> readers = new HashMap<>();
			try {
				for (final String key : parts.keySet()) {
					try {
						final URI uri = dataMap.get(key);
						if (uri != null) {
							@SuppressWarnings("resource")
							final CSVReader r = new FileCSVReader(uri);
							readers.put(key, r);
						}
					} catch (final IOException e) {
						// Assertions.fail(e.getMessage());
					}
				}
				try {
					importer.importModel(scenarioModel, readers, context);
				} catch (final Throwable th) {
					throw new RuntimeException(th);
//					Assertions.fail(th.getMessage());
				}
			} finally {
				for (final CSVReader r : readers.values()) {
					try {
						r.close();
					} catch (final IOException e) {

					}
				}
			}
		}
	}

	@NonNull
	public IScenarioDataProvider doImport() {
		return CopiedCSVImporter.importCSVScenario(getDataMap());
	}
}
