/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.scenario;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.actuals.importer.ActualsModelExtraImporter;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.importer.AssignmentModelImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoModelImporter;
import com.mmxlabs.models.lng.cargo.importer.DischargeSlotImporter;
import com.mmxlabs.models.lng.cargo.importer.LoadSlotImporter;
import com.mmxlabs.models.lng.cargo.importer.VesselAvailabilityBallastBonusImporter;
import com.mmxlabs.models.lng.cargo.importer.VesselAvailabilityBallastBonusImporterExtraImporter;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.importer.CommercialModelImporter;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.importer.BaseFuelImporter;
import com.mmxlabs.models.lng.fleet.importer.FleetModelImporter;
import com.mmxlabs.models.lng.fleet.importer.VesselClassImporter;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
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
import com.mmxlabs.models.lng.transformer.its.internal.Activator;
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

public class CSVImporter {

	private final @NonNull Map<String, URL> dataMap = new HashMap<>();

	public void importPortData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(PortModelImporter.PORT_KEY, createURL(urlRoot, "Ports.csv"));
		dataMap.put(PortModelImporter.PORT_GROUP_KEY, createURL(urlRoot, "Port Groups.csv"));
		dataMap.put(PortModelImporter.DISTANCES_KEY, createURL(urlRoot, "Distance Matrix.csv"));
		dataMap.put(PortModelImporter.SUEZ_KEY, createURL(urlRoot, "Suez Distance Matrix.csv"));
		dataMap.put(PortModelImporter.PANAMA_KEY, createURL(urlRoot, "Panama Distance Matrix.csv"));
	}

	public void importFleetData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(FleetModelImporter.VESSEL_CLASSES_KEY, createURL(urlRoot, "Vessel Classes.csv"));
		dataMap.put(FleetModelImporter.VESSELS_KEY, createURL(urlRoot, "Vessels.csv"));
		dataMap.put(FleetModelImporter.CURVES_KEY, createURL(urlRoot, "Consumption Curves.csv"));
		dataMap.put(FleetModelImporter.GROUPS_KEY, createURL(urlRoot, "Vessel Groups.csv"));
		dataMap.put(FleetModelImporter.FUELS_KEY, createURL(urlRoot, "Base Fuels.csv"));
	}

	public void importEntityData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(CommercialModelImporter.ENTITIES_KEY, createURL(urlRoot, "Entities.csv"));
		dataMap.put(CommercialModelImporter.ENTITY_BOOKS_KEY, createURL(urlRoot, "Entity Books.csv"));
	}

	public void importContractData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(CommercialModelImporter.PURCHASE_CON_KEY, createURL(urlRoot, "Purchase Contracts.csv"));
		dataMap.put(CommercialModelImporter.SALES_CON_KEY, createURL(urlRoot, "Sales Contracts.csv"));

	}

	public void importMarketData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(PricingModelImporter.PRICE_CURVE_KEY, createURL(urlRoot, "Commodity Curves.csv"));
		dataMap.put(PricingModelImporter.BASEFUEL_PRICING_KEY, createURL(urlRoot, "Base Fuel Curves.csv"));
		dataMap.put(PricingModelImporter.CHARTER_CURVE_KEY, createURL(urlRoot, "Charter Curves.csv"));
	}

	public void importCostData(@NonNull final String urlRoot) throws MalformedURLException {

		dataMap.put(CostModelImporter.COOLDOWN_PRICING_KEY, createURL(urlRoot, "Cooldown Prices.csv"));
		dataMap.put(CostModelImporter.PORT_COSTS_KEY, createURL(urlRoot, "Port Costs.csv"));
		dataMap.put(CostModelImporter.PANAMA_CANAL_TARIFF_KEY, createURL(urlRoot, "Panama Canal Tariff.csv"));
	}

	public void importPromptData(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(PromptPeriodImporter.PROMPT_PERIOD_KEY, createURL(urlRoot, "Prompt Period.csv"));
	}

	public void importSpotMarkets(@NonNull final String urlRoot) throws MalformedURLException {
		dataMap.put(SpotMarketsModelImporter.CHARTER_PRICING_KEY, createURL(urlRoot, "Charter Markets.csv"));
		dataMap.put(SpotMarketsModelImporter.SPOT_CARGO_MARKETS_KEY, createURL(urlRoot, "Spot Cargo Markets.csv"));
	}

	public void importPorfolioData(@NonNull final String urlRoot) throws MalformedURLException {

		dataMap.put(CargoModelImporter.CARGO_KEY, createURL(urlRoot, "Cargoes.csv"));
		dataMap.put(CargoModelImporter.CARGO_GROUP_KEY, createURL(urlRoot, "Cargo Groups.csv"));
		dataMap.put(CargoModelImporter.EVENTS_KEY, createURL(urlRoot, "Events.csv"));
		dataMap.put(CargoModelImporter.VESSEL_AVAILABILITY_KEY, createURL(urlRoot, "Vessel Availability.csv"));
		dataMap.put(AssignmentModelImporter.ASSIGNMENTS, createURL(urlRoot, "Assignments.csv"));
		dataMap.put(VesselAvailabilityBallastBonusImporterExtraImporter.BALLASTBONUS_KEY, createURL(urlRoot, "Vessel Availability--Ballast Bonus.csv"));
	}

	public void importStandardComponents(@NonNull final String urlRoot) throws MalformedURLException {
		importMarketData(urlRoot);
		importPortData(urlRoot);
		importFleetData(urlRoot);
		importCostData(urlRoot);
		importEntityData(urlRoot);
		importContractData(urlRoot);
		importSpotMarkets(urlRoot);
		importPorfolioData(urlRoot);
		importPromptData(urlRoot);
	}

	@NonNull
	public URL createURL(final String urlRoot, final String filename) throws MalformedURLException {
		return new URL(String.format("%s/%s", urlRoot, filename));
	}

	public void addExtraMapEntry(@NonNull final String key, @NonNull final URL url) {
		dataMap.put(key, url);
	}

	@NonNull
	public Map<String, URL> getDataMap() {
		return dataMap;
	}

	public static LNGScenarioModel importCSVScenario(@NonNull final String urlRoot, final String... extraMapEntries) throws MalformedURLException {

		final CSVImporter importer = new CSVImporter();

		importer.importStandardComponents(urlRoot);

		for (int i = 0; i < extraMapEntries.length; i += 2) {
			if (i + 1 < extraMapEntries.length) {
				final String key = extraMapEntries[i];
				assert key != null;
				final String value = extraMapEntries[i + 1];
				assert value != null;
				importer.addExtraMapEntry(key, new URL(value));
			}
		}
		return importCSVScenario(importer.dataMap);
	}

	@NonNull
	public static LNGScenarioModel importCSVScenario(@NonNull final Map<String, URL> dataMap) {

		final IImporterRegistry importerRegistry = getImporterRegistry();
		return importCSVScenario(dataMap, importerRegistry);
	}

	@NonNull
	public static LNGScenarioModel importCSVScenario(@NonNull final Map<String, URL> dataMap, @NonNull final IImporterRegistry importerRegistry) {

		final DefaultImportContext context = new DefaultImportContext('.');

		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		scenarioModel.setCargoModel((CargoModel) importSubModel(importerRegistry, context, dataMap, CargoPackage.eINSTANCE.getCargoModel()));
		// scenarioModel.setAssignmentModel((AssignmentModel) importSubModel(importerRegistry, context, dataMap, AssignmentPackage.eINSTANCE.getAssignmentModel()));
		scenarioModel.setScheduleModel((ScheduleModel) importSubModel(importerRegistry, context, dataMap, SchedulePackage.eINSTANCE.getScheduleModel()));
		scenarioModel.setAnalyticsModel(((AnalyticsModel) importSubModel(importerRegistry, context, dataMap, AnalyticsPackage.eINSTANCE.getAnalyticsModel())));

		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		scenarioModel.setReferenceModel(referenceModel);

		referenceModel.setPortModel((PortModel) importSubModel(importerRegistry, context, dataMap, PortPackage.eINSTANCE.getPortModel()));
		referenceModel.setFleetModel((FleetModel) importSubModel(importerRegistry, context, dataMap, FleetPackage.eINSTANCE.getFleetModel()));
		referenceModel.setPricingModel((PricingModel) importSubModel(importerRegistry, context, dataMap, PricingPackage.eINSTANCE.getPricingModel()));
		referenceModel.setCostModel((CostModel) importSubModel(importerRegistry, context, dataMap, PricingPackage.eINSTANCE.getCostModel()));
		referenceModel.setCommercialModel((CommercialModel) importSubModel(importerRegistry, context, dataMap, CommercialPackage.eINSTANCE.getCommercialModel()));
		referenceModel.setSpotMarketsModel((SpotMarketsModel) importSubModel(importerRegistry, context, dataMap, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel()));
		// scenarioModel.setParametersModel((ParametersModel) importSubModel(importerRegistry, context, dataMap, ParametersPackage.eINSTANCE.getParametersModel()));

		importExtraModels(scenarioModel, importerRegistry, context, dataMap);

		context.setRootObject(scenarioModel);

		context.run();

		for (final IPostModelImporter postModelImporter : importerRegistry.getPostModelImporters()) {
			postModelImporter.onPostModelImport(context, scenarioModel);
		}

		return scenarioModel;
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
				final Bundle bundle = FrameworkUtil.getBundle(CSVImporter.class);
				final BundleContext bundleContext = bundle != null ? bundle.getBundleContext() : null;
				if (bundleContext != null) {
					// Partial plugin env - its bundle activator does not seem to be started when run from eclipse.
					final Injector injector = Guice.createInjector(new ExtensionConfigurationModule(bundleContext));
					final IImporterRegistry importerRegistry = new ImporterRegistry();
					injector.injectMembers(importerRegistry);
					return importerRegistry;
				} else {

					final Map<EClass, ISubmodelImporter> subModelImporters = new HashMap<EClass, ISubmodelImporter>();
					subModelImporters.put(CargoPackage.eINSTANCE.getCargoModel(), new CargoModelImporter());
					subModelImporters.put(CommercialPackage.eINSTANCE.getCommercialModel(), new CommercialModelImporter());
					subModelImporters.put(FleetPackage.eINSTANCE.getFleetModel(), new FleetModelImporter());
					// subModelImporters.put(AssignmentPackage.eINSTANCE.getAssignmentModel(), new AssignmentModelImporter());
//					subModelImporters.put(ParametersPackage.eINSTANCE.getParametersModel(), new ParametersModelImporter());
					subModelImporters.put(PortPackage.eINSTANCE.getPortModel(), new PortModelImporter());
					subModelImporters.put(PricingPackage.eINSTANCE.getCostModel(), new CostModelImporter());
					subModelImporters.put(PricingPackage.eINSTANCE.getPricingModel(), new PricingModelImporter());
					subModelImporters.put(SchedulePackage.eINSTANCE.getScheduleModel(), new ScheduleModelImporter());
					subModelImporters.put(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel(), new SpotMarketsModelImporter());
					// subModelImporters.put(ActualsPackage.eINSTANCE.getActualsModel(), new ActualsModelImporter());

					final Map<EClass, IClassImporter> classImporters = new HashMap<EClass, IClassImporter>();
					classImporters.put(FleetPackage.eINSTANCE.getBaseFuel(), new BaseFuelImporter());
					classImporters.put(CargoPackage.eINSTANCE.getCargo(), new CargoImporter());
					classImporters.put(CargoPackage.eINSTANCE.getDischargeSlot(), new DischargeSlotImporter());
					classImporters.put(CargoPackage.eINSTANCE.getLoadSlot(), new LoadSlotImporter());
					classImporters.put(PricingPackage.eINSTANCE.getPortCost(), new PortCostImporter());
					classImporters.put(SpotMarketsPackage.eINSTANCE.getSpotMarket(), new SpotMarketImporter());
					classImporters.put(FleetPackage.eINSTANCE.getVesselClass(), new VesselClassImporter());
					classImporters.put(PricingPackage.eINSTANCE.getDataIndex(), new DataIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getBaseFuelIndex(), new BaseFuelIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCurrencyIndex(), new CurrencyIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCharterIndex(), new CharterIndexImporter());
					classImporters.put(PricingPackage.eINSTANCE.getCommodityIndex(), new CommodityIndexImporter());

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

	private static EObject importSubModel(@NonNull final IImporterRegistry importerRegistry, @NonNull final IMMXImportContext context, @NonNull final Map<String, URL> dataMap,
			@NonNull final EClass subModelClass) {

		final ISubmodelImporter importer = importerRegistry.getSubmodelImporter(subModelClass);
		if (importer == null) {
			return null;
		}
		final Map<String, String> parts = importer.getRequiredInputs();
		final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
		try {
			for (final String key : parts.keySet()) {
				try {
					final URL url = dataMap.get(key);
					if (url != null) {
						@SuppressWarnings("resource")
						final CSVReader r = new FileCSVReader(url);
						readers.put(key, r);
					}
				} catch (final IOException e) {
					// e.printStackTrace();
					// Assert.fail(e.getMessage());
				}
			}
			try {
				return importer.importModel(readers, context);
			} catch (final Throwable th) {
				Assert.fail(th.getMessage());
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
			@NonNull final Map<String, URL> dataMap) {
		for (final IExtraModelImporter importer : importerRegistry.getExtraModelImporters()) {
			if (importer == null) {
				continue;
			}
			final Map<String, String> parts = importer.getRequiredInputs();
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			try {
				for (final String key : parts.keySet()) {
					try {
						final URL url = dataMap.get(key);
						if (url != null) {
							@SuppressWarnings("resource")
							final CSVReader r = new FileCSVReader(url);
							readers.put(key, r);
						}
					} catch (final IOException e) {
						// Assert.fail(e.getMessage());
					}
				}
				try {
					importer.importModel(scenarioModel, readers, context);
				} catch (final Throwable th) {
					Assert.fail(th.getMessage());
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
	public LNGScenarioModel doImport() {
		return CSVImporter.importCSVScenario(getDataMap());
	}
}
