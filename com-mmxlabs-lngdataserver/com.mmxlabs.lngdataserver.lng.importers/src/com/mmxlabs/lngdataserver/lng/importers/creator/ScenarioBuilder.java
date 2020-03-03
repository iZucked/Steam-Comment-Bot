/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.creator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupDefinition;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.DistancesLinesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LingoDistanceUpdater;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LocationsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.UserUpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.portconfig.PortConfig;
import com.mmxlabs.lngdataserver.lng.io.portconfig.PortConfigType;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.importers.HolidayCalendarImporter;
import com.mmxlabs.models.lng.pricing.importers.PricingCalendarImporter;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioBuilder {
	public static final String DEFAULT_ENTITY_NAME = "Entity";

	private final IScenarioDataProvider scenarioDataProvider;

	private ScenarioModelBuilder scenarioModelBuilder;

	public static ScenarioBuilder initialiseBasicScenario() throws IOException {
		ScenarioBuilder builder = new ScenarioBuilder();

		return builder;
	}

	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return scenarioDataProvider;
	}

	private ScenarioBuilder() throws IOException {

		scenarioModelBuilder = ScenarioModelBuilder.instantiate();

		final LNGScenarioModel scenarioModel = scenarioModelBuilder.getLNGScenarioModel();
		scenarioDataProvider = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), scenarioModel);
		loadPortsAndDistances();
	}

	public ScenarioBuilder loadDefaultData() throws IOException {
		configureDefaultCommercialModel(DEFAULT_ENTITY_NAME);
		configureDefaultLoadAndDischargePorts();
		loadDefaultVessels();
		loadDefaultCanalCosts();
		loadDefaultPortCosts();

		createDefaultCooldownCost("1000000");
		setDefaultBunkerCosts("400");

		loadDefaultConversionFactors();
		loadDefaultCalendars();

		return this;
	}

	private void loadDefaultConversionFactors() throws IOException {
		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/Conversion Factors.csv")) {

				DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					DefaultClassImporter importer = new DefaultClassImporter();
					pricingModel.getConversionFactors().addAll((Collection<? extends UnitConversion>) importer.importObjects(PricingPackage.Literals.UNIT_CONVERSION, reader, context));
				}
			}
		}
	}

	private void loadDefaultCalendars() throws IOException {
		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/Holiday Calendars.csv")) {

				DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					HolidayCalendarImporter importer = new HolidayCalendarImporter();
					List<HolidayCalendar> importObjects = importer.importObjects(reader, context);
					pricingModel.getHolidayCalendars().addAll(importObjects);
				}
			}
		}
		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/Pricing Calendars.csv")) {

				DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					PricingCalendarImporter importer = new PricingCalendarImporter();
					List<PricingCalendar> importObjects = importer.importObjects(reader, context);
					pricingModel.getPricingCalendars().addAll(importObjects);
				}
			}
		}
		PricingModelFinder pricingModelFinder = new PricingModelFinder(pricingModel);

		{
			MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("Brent");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("ICE"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("LSE"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("HH");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("Socal"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("NYSE"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("TTF");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("ICE"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("EURONEXT"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("NBP");
			// idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("???"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("LSE"));
			pricingModel.getMarketIndices().add(idx);
		}

	}

	private ScenarioBuilder setDefaultBunkerCosts(String cost) {
		scenarioModelBuilder.getCostModelBuilder().setAllBaseFuelCost(ScenarioModelUtil.getFleetModel(scenarioDataProvider), cost);

		return this;
	}

	private ScenarioBuilder createDefaultCooldownCost(String expr) {
		PortModelFinder portFinder = new PortModelFinder(ScenarioModelUtil.getPortModel(scenarioDataProvider));
		CostModel costModel = ScenarioModelUtil.getCostModel(scenarioDataProvider);
		CooldownPrice cp = PricingFactory.eINSTANCE.createCooldownPrice();
		cp.getPorts().add(portFinder.getCapabilityPortsGroup(PortCapability.LOAD));
		cp.setLumpsum(true);
		cp.setExpression(expr);
		costModel.getCooldownCosts().add(cp);

		return this;
	}

	public ScenarioBuilder configureDefaultLoadAndDischargePorts() throws IOException {
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/port-config.json")) {
			configureLoadAndDischargePorts(inputStream);
		}
		return this;
	}

	public ScenarioBuilder configureLoadAndDischargePorts(InputStream inputStream) throws IOException {

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final PortModelFinder finder = new PortModelFinder(portModel);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final List<PortConfig> portConfigs = mapper.readValue(inputStream, new TypeReference<List<PortConfig>>() {
		});
		for (final PortConfig config : portConfigs) {
			final Port p = finder.findPortById(config.mmxId);
			if (config.type == PortConfigType.LOAD) {
				p.getCapabilities().add(PortCapability.LOAD);
				p.setCvValue(config.cv);
				p.setLoadDuration(config.duration);
			} else if (config.type == PortConfigType.DISCHARGE) {
				p.getCapabilities().add(PortCapability.DISCHARGE);
				p.getCapabilities().add(PortCapability.DISCHARGE);
				p.setDischargeDuration(config.duration);
			}
		}
		return this;
	}

	public ScenarioBuilder configureDefaultCommercialModel(String defaultEntityName) {
		final LegalEntity entity = scenarioModelBuilder.getCommercialModelBuilder().createEntity(defaultEntityName);
		scenarioModelBuilder.getCommercialModelBuilder().setTaxRates(entity, LocalDate.of(2000, Month.JANUARY, 1), 0);
		return this;
	}

	private ScenarioBuilder loadPortsAndDistances() throws IOException {

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

		{
			for (final PortCapability capability : PortCapability.values()) {
				boolean found = false;
				for (final CapabilityGroup g : portModel.getSpecialPortGroups()) {
					if (g.getCapability().equals(capability)) {
						found = true;
						break;
					}
				}
				if (!found) {
					final CapabilityGroup g = PortFactory.eINSTANCE.createCapabilityGroup();
					g.setName("All " + capability.getName() + " Ports");
					g.setCapability(capability);
					portModel.getSpecialPortGroups().add(g);
				}
			}

		}

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		{
			final LocationsVersion locationsVersion;
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/ports.json")) {
				locationsVersion = mapper.readValue(inputStream, LocationsVersion.class);
			}

			final List<UpdateItem> steps = LocationsToScenarioCopier.generateUpdates(editingDomain, portModel, locationsVersion);

			final CompoundCommand command = new CompoundCommand("Update ports");
			// Apply basic updates
			steps.stream() //
					.filter(UpdateStep.class::isInstance) //
					.map(UpdateStep.class::cast) //
					.map(UpdateStep::getAction) //
					.filter(Objects::nonNull) //
					.forEach(a -> a.accept(command));

			// Apply selected quick fixes
			steps.stream() //
					.filter(UserUpdateStep.class::isInstance) //
					.map(UserUpdateStep.class::cast) //
					.filter(UserUpdateStep::isHasQuickFix) //
					.forEach(a -> a.getQuickFix().accept(command));

			steps.stream() //
					.filter(UpdateWarning.class::isInstance) //
					.map(UpdateWarning.class::cast) //
					.filter(UpdateWarning::isHasQuickFix) //
					.forEach(a -> a.getQuickFix().accept(command));

			command.execute();
		}

		{
			final List<AtoBviaCLookupRecord> distanceRecords;

			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/distances.json")) {
				distanceRecords = mapper.readValue(inputStream, new TypeReference<List<AtoBviaCLookupRecord>>() {
				});
			}

			final CompoundCommand command = DistancesLinesToScenarioCopier.getUpdateCommand(editingDomain, portModel, distanceRecords);
			command.execute();
		}

		{
			final Map<String, APortSet<Port>> typeMap = new HashMap<>();

			portModel.getPorts().forEach(c -> typeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
			portModel.getPortCountryGroups().forEach(c -> typeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
			portModel.getPortGroups().forEach(c -> typeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/port-groups.json")) {

				final List<PortGroupDefinition> portGroups = mapper.readValue(inputStream, new TypeReference<List<PortGroupDefinition>>() {
				});

				for (PortGroupDefinition pgd : portGroups) {
					PortGroup pg = PortFactory.eINSTANCE.createPortGroup();
					pg.setName(pgd.getName());

					for (String id : pgd.getEntries()) {
						APortSet<Port> obj = typeMap.get(id);
						if (obj == null) {
							throw new IllegalStateException("Unknown object: " + id);
						}
						pg.getContents().add(obj);
					}
					portModel.getPortGroups().add(pg);
				}

			}
		}

		return this;

	}

	public ScenarioBuilder loadDefaultVessels() throws IOException {

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());

		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/bunkerfuels.json")) {
				final BunkerFuelsVersion v = mapper.readValue(inputStream, BunkerFuelsVersion.class);
				final Command command = BunkerFuelsToScenarioImporter.getUpdateCommand(editingDomain, fleetModel, v);
				command.execute();
			}
		}
		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/vessels.json")) {
				final VesselsVersion v = mapper.readValue(inputStream, VesselsVersion.class);
				final Command command = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fleetModel, portModel, v);
				command.execute();
			}
		}
		return this;
	}

	public ScenarioBuilder loadDefaultPortCosts() throws IOException {
		try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/port-costs.json")) {
			loadPortCosts(inputStream);
		}
		return this;
	}

	public ScenarioBuilder loadPortCosts(final InputStream inputStream) throws IOException {

		final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		final CompoundCommand command = new CompoundCommand();
		final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createPortCostsUpdater(json);
		updater.accept(command, scenarioDataProvider);
		command.execute();

		return this;
	}

	public ScenarioBuilder loadDefaultCanalCosts() throws IOException {

		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/suez-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				final CompoundCommand command = new CompoundCommand();
				final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createSuezTariffUpdater(json);
				updater.accept(command, scenarioDataProvider);
				command.execute();
			}
		}
		{
			try (InputStream inputStream = LingoDistanceUpdater.class.getResourceAsStream("/panama-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				final CompoundCommand command = new CompoundCommand();
				final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createPanamaTariffUpdater(json);
				updater.accept(command, scenarioDataProvider);
				command.execute();
			}
		}

		return this;
	}

	public void createDummyPricingData() {

		PricingModelBuilder pricingModelBuilder = new PricingModelBuilder(ScenarioModelUtil.getPricingModel(scenarioDataProvider));
		YearMonth date = YearMonth.from(LocalDate.now().withMonth(1));

		// May want original euro/mwh or pence/therm
		pricingModelBuilder.makeCommodityDataCurve("TTF", "€", "MWh").addIndexPoint(date, 13.0).build();
		pricingModelBuilder.createCommodityExpressionCurve("TTF_USD", "$", "mmbtu", "TTF*FX_EUR_USD*MWh_to_mmBtu");

		pricingModelBuilder.makeCommodityDataCurve("NBP", "p", "therm").addIndexPoint(date, 40).build();
		pricingModelBuilder.createCommodityExpressionCurve("NBP_USD", "$", "mmbtu", "NBP*FX_GBP_USD*Therm_to_mmBtu");

		pricingModelBuilder.makeCommodityDataCurve("HH", "$", "mmBtu").addIndexPoint(date, 3.0).build();
		pricingModelBuilder.makeCommodityDataCurve("JCC", "$", "bbl").addIndexPoint(date, 74.0).build();
		pricingModelBuilder.makeCommodityDataCurve("JKM", "$", "mmBtu").addIndexPoint(date, 5.0).build();
		pricingModelBuilder.makeCommodityDataCurve("Brent", "$", "bbl").addIndexPoint(date, 60.0).build();

		// Currency curves

		pricingModelBuilder.makeCurrencyDataCurve("FX_EUR_USD", "€", "$").addIndexPoint(date, 1.1).build();
		// Include pence to pound conversion
		pricingModelBuilder.makeCurrencyDataCurve("FX_GBP_USD", "p", "$").addIndexPoint(date, 1.3 / 100.0).build();

		// Charter
		pricingModelBuilder.makeCharterDataCurve("SPOT", "$", "day").addIndexPoint(date, 80_000).build();

		// Bunker curve
		pricingModelBuilder.makeBunkerFuelDataCurve("HFO", "$", "MT").addIndexPoint(date, 400).build();
		pricingModelBuilder.makeBunkerFuelDataCurve("MGO", "$", "MT").addIndexPoint(date, 600).build();

		// Replace expressions with new curve
		CostModel costModel = ScenarioModelUtil.getCostModel(scenarioDataProvider);
		for (BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
			if ("MGO".equals(baseFuelCost.getFuel().getName())) {
				baseFuelCost.setExpression("MGO");
			}
			if ("HFO".equals(baseFuelCost.getFuel().getName())) {
				baseFuelCost.setExpression("HFO");
			}
		}
	}
}
