/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupDefinition;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsIOConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.DistancesLinesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LocationsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.importers.update.UserUpdateStep;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.portconfig.PortConfig;
import com.mmxlabs.lngdataserver.lng.io.portconfig.PortConfigType;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.importer.AssignmentImporter;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.VesselConstants;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.importers.CommodityIndexImporter;
import com.mmxlabs.models.lng.pricing.importers.HolidayCalendarImporter;
import com.mmxlabs.models.lng.pricing.importers.PricingCalendarImporter;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.models.util.importer.impl.EncoderUtil;
import com.mmxlabs.rcp.common.versions.VersionsUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioBuilder {
	public static final String DEFAULT_ENTITY_NAME = "Entity";

	private final @NonNull IScenarioDataProvider scenarioDataProvider;

	private final ScenarioModelBuilder scenarioModelBuilder;

	public static ScenarioBuilder initialiseBasicScenario() throws IOException {
		final ScenarioBuilder builder = new ScenarioBuilder();

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

		scenarioModelBuilder.getCargoModelBuilder().initCanalBookings();

		createDefaultCooldownCost("1000000");
		setDefaultBunkerCosts("400");

		loadDefaultConversionFactors();
		loadDefaultCalendars();

		initVersionRecords();

		return this;
	}

	private void initVersionRecords() {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		fleetModel.setVesselGroupVersionRecord(VersionsUtil.createNewRecord());
		fleetModel.setBunkerFuelsVersionRecord(VersionsUtil.createNewRecord());
		fleetModel.setFleetVersionRecord(VersionsUtil.createNewRecord());

		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.setMarketCurvesVersionRecord(VersionsUtil.createNewRecord());
		pricingModel.setSettledPricesVersionRecord(VersionsUtil.createNewRecord());

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		portModel.setDistanceVersionRecord(VersionsUtil.createNewRecord());
		portModel.setPortVersionRecord(VersionsUtil.createNewRecord());
		portModel.setPortGroupVersionRecord(VersionsUtil.createNewRecord());
	}

	private void loadDefaultConversionFactors() throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/Conversion Factors.csv")) {

				final DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					final DefaultClassImporter importer = new DefaultClassImporter();
					pricingModel.getConversionFactors().addAll((Collection<? extends UnitConversion>) importer.importObjects(PricingPackage.Literals.UNIT_CONVERSION, reader, context));
				}
			}
		}
	}

	private void loadDefaultCalendars() throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/Holiday Calendars.csv")) {

				final DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					final HolidayCalendarImporter importer = new HolidayCalendarImporter();
					final List<HolidayCalendar> importObjects = importer.importObjects(reader, context);
					pricingModel.getHolidayCalendars().addAll(importObjects);
				}
			}
		}
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/Pricing Calendars.csv")) {

				final DefaultImportContext context = new DefaultImportContext('.');
				try (CSVReader reader = new CSVReader(',', inputStream)) {

					final PricingCalendarImporter importer = new PricingCalendarImporter();
					final List<PricingCalendar> importObjects = importer.importObjects(reader, context);
					pricingModel.getPricingCalendars().addAll(importObjects);
				}
			}
		}
		final PricingModelFinder pricingModelFinder = new PricingModelFinder(pricingModel);

		{
			final MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("Brent");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("ICE"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("LSE"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			final MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("HH");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("Socal"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("NYSE"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			final MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("TTF");
			idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("ICE"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("EURONEXT"));
			pricingModel.getMarketIndices().add(idx);
		}
		{
			final MarketIndex idx = PricingFactory.eINSTANCE.createMarketIndex();
			idx.setName("NBP");
			// idx.setPricingCalendar(pricingModelFinder.findPricingCalendar("???"));
			idx.setSettleCalendar(pricingModelFinder.findHolidayCalendar("LSE"));
			pricingModel.getMarketIndices().add(idx);
		}

	}

	private ScenarioBuilder setDefaultBunkerCosts(final String cost) {
		scenarioModelBuilder.getCostModelBuilder().setAllBaseFuelCost(ScenarioModelUtil.getFleetModel(scenarioDataProvider), cost);

		return this;
	}

	private ScenarioBuilder createDefaultCooldownCost(final String expr) {
		final PortModelFinder portFinder = new PortModelFinder(ScenarioModelUtil.getPortModel(scenarioDataProvider));
		final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioDataProvider);
		final CooldownPrice cp = PricingFactory.eINSTANCE.createCooldownPrice();
		cp.getPorts().add(portFinder.getCapabilityPortsGroup(PortCapability.LOAD));
		cp.setLumpsumExpression(expr);
		costModel.getCooldownCosts().add(cp);

		return this;
	}

	public ScenarioBuilder configureDefaultLoadAndDischargePorts() throws IOException {
		try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/port-config.json")) {
			configureLoadAndDischargePorts(inputStream);
		}
		return this;
	}

	public ScenarioBuilder configureLoadAndDischargePorts(final InputStream inputStream) throws IOException {

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

	public ScenarioBuilder configureDefaultCommercialModel(final @NonNull String defaultEntityName) {
		final LegalEntity entity = scenarioModelBuilder.getCommercialModelBuilder().createEntity(defaultEntityName, LocalDate.of(2000, Month.JANUARY, 1), 0);
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

		final LocationsVersion locationsVersion;
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/ports.json")) {
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
			final List<AtoBviaCLookupRecord> manualDistanceRecords;

			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/distances.json")) {
				distanceRecords = mapper.readValue(inputStream, new TypeReference<List<AtoBviaCLookupRecord>>() {
				});
			}
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/distances-manual.json")) {
				manualDistanceRecords = mapper.readValue(inputStream, new TypeReference<List<AtoBviaCLookupRecord>>() {
				});
			}

			final CompoundCommand command = DistancesLinesToScenarioCopier.getUpdateCommand(editingDomain, portModel, locationsVersion, distanceRecords, manualDistanceRecords);
			command.execute();
		}

		{
			final Map<String, APortSet<Port>> typeMap = new HashMap<>();

			portModel.getPorts().forEach(c -> typeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
			portModel.getPortCountryGroups().forEach(c -> typeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
			portModel.getPortGroups().forEach(c -> typeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/port-groups.json")) {

				final List<PortGroupDefinition> portGroups = mapper.readValue(inputStream, new TypeReference<List<PortGroupDefinition>>() {
				});

				for (final PortGroupDefinition pgd : portGroups) {
					final PortGroup pg = PortFactory.eINSTANCE.createPortGroup();
					pg.setName(pgd.getName());

					for (final String id : pgd.getEntries()) {
						final APortSet<Port> obj = typeMap.get(id);
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

	/**
	 * Method to programatically update ports and distances.
	 * 
	 * Note: Keep in sync with LingoDistanceUpdater and UpdatePortsPage. TODO: Refactor into shared code path.
	 * 
	 * @param scenarioDataProvider
	 * @throws IOException
	 */
	public static void reloadPortsAndDistances(IScenarioDataProvider scenarioDataProvider) throws IOException {
		try (InputStream portInputStream = ScenarioBuilder.class.getResourceAsStream("/ports.json")) {
			try (InputStream distancesInputStream = ScenarioBuilder.class.getResourceAsStream("/distances.json")) {
				try (InputStream manualDistancesInputStream = ScenarioBuilder.class.getResourceAsStream("/distances-manual.json")) {
					try (InputStream portGroupsInputStream = ScenarioBuilder.class.getResourceAsStream("/port-groups.json")) {
						reloadPortsAndDistances(scenarioDataProvider, portInputStream, distancesInputStream, manualDistancesInputStream, portGroupsInputStream);
					}
				}
			}
		}

	}

	public static void reloadPortsAndDistances(IScenarioDataProvider scenarioDataProvider, InputStream portInputStream, InputStream distancesInputStream,
			@Nullable InputStream manualDistancesInputStream, @Nullable InputStream portGroupsInputStream) throws IOException {

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

		final LocationsVersion locationsVersion;
		{
			{
				locationsVersion = mapper.readValue(portInputStream, LocationsVersion.class);
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
			final List<AtoBviaCLookupRecord> manualDistanceRecords;

			{
				distanceRecords = mapper.readValue(distancesInputStream, new TypeReference<List<AtoBviaCLookupRecord>>() {
				});
			}
			if (manualDistancesInputStream != null) {
				manualDistanceRecords = mapper.readValue(manualDistancesInputStream, new TypeReference<List<AtoBviaCLookupRecord>>() {
				});
			} else {
				manualDistanceRecords = Collections.emptyList();
			}

			final CompoundCommand command = DistancesLinesToScenarioCopier.getUpdateCommand(editingDomain, portModel, locationsVersion, distanceRecords, manualDistanceRecords);
			command.execute();
		}

		if (portGroupsInputStream != null) {
			final Map<String, APortSet<Port>> typeMap = new HashMap<>();

			portModel.getPorts().forEach(c -> typeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
			portModel.getPortCountryGroups().forEach(c -> typeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
			portModel.getPortGroups().forEach(c -> typeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

			final List<PortGroupDefinition> portGroups = mapper.readValue(portGroupsInputStream, new TypeReference<List<PortGroupDefinition>>() {
			});

			for (final PortGroupDefinition pgd : portGroups) {
				final PortGroup pg = PortFactory.eINSTANCE.createPortGroup();
				pg.setName(pgd.getName());

				for (final String id : pgd.getEntries()) {
					final APortSet<Port> obj = typeMap.get(id);
					if (obj == null) {
						throw new IllegalStateException("Unknown object: " + id);
					}
					pg.getContents().add(obj);
				}
				portModel.getPortGroups().add(pg);
			}

		}

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
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/bunkerfuels.json")) {
				final BunkerFuelsVersion v = mapper.readValue(inputStream, BunkerFuelsVersion.class);
				final Command command = BunkerFuelsToScenarioImporter.getUpdateCommand(editingDomain, fleetModel, v);
				command.execute();
			}
		}
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream(String.format("/%s", VesselsIOConstants.JSON_VESSELS_REFERENCE))) {
				final VesselsVersion vesselsVersion = mapper.readValue(inputStream, VesselsVersion.class);
				vesselsVersion.getVessels().stream().forEach(vessel -> {
					vessel.setName(VesselConstants.convertMMXReferenceNameToInternalName(EncoderUtil.decode(vessel.getName())));
					vessel.setIsReference(Optional.of(Boolean.TRUE));
					vessel.setMmxReference(Optional.of(Boolean.TRUE));
				});
				final Command command = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fleetModel, portModel, vesselsVersion);
				if (command instanceof CompoundCommand) {
					final CompoundCommand cmd = (CompoundCommand) command;
					final String versionId = vesselsVersion.getIdentifier();
					if (versionId != null && !versionId.isBlank()) {
						cmd.append(SetCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__MMX_VESSEL_DB_VERSION, vesselsVersion.getIdentifier()));
					} else {
						throw new IllegalStateException("Vessels version identifier must be present and non-blank.");
					}
				}
				command.execute();
			}
		}
		return this;
	}

	public ScenarioBuilder loadDefaultPortCosts() throws IOException {
		try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/port-costs.json")) {
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
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/suez-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				final CompoundCommand command = new CompoundCommand();
				final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createSuezTariffUpdater(json);
				updater.accept(command, scenarioDataProvider);
				command.execute();
			}
		}
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/panama-tariff.json")) {
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

		final PricingModelBuilder pricingModelBuilder = new PricingModelBuilder(ScenarioModelUtil.getPricingModel(scenarioDataProvider));
		final YearMonth date = YearMonth.from(LocalDate.now().withMonth(1));

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
		final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioDataProvider);
		for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
			if ("MGO".equals(baseFuelCost.getFuel().getName())) {
				baseFuelCost.setExpression("MGO");
			}
			if ("HFO".equals(baseFuelCost.getFuel().getName())) {
				baseFuelCost.setExpression("HFO");
			}
		}
	}

	public void switchPilotLightTo(final String fuelName) {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);

		BaseFuel targetFuel = null;
		for (final BaseFuel bf : fleetModel.getBaseFuels()) {
			if (bf.getName().equals(fuelName)) {
				targetFuel = bf;
				break;
			}
		}

		if (targetFuel != null) {
			for (final Vessel v : fleetModel.getVessels()) {
				// If the vessel has a reference, skip it
				if (v.getReference() != null) {
					continue;
				}
				v.setPilotLightBaseFuel(targetFuel);
			}
		}

	}

	public void importVesselCharters(InputStream inputStream) throws IOException {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		{

			final DefaultImportContext context = new DefaultImportContext('.');
			context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
			context.registerNamedObjectsFromSubModels();
			try (CSVReader reader = new CSVReader(',', inputStream)) {

				final DefaultClassImporter importer = new DefaultClassImporter();
				cargoModel.getVesselCharters().addAll((Collection<? extends VesselCharter>) importer.importObjects(CargoPackage.Literals.VESSEL_CHARTER, reader, context));
			}
			context.run();

		}
	}

	public void importAssignments(InputStream inputStream) throws IOException {
		{

			final DefaultImportContext context = new DefaultImportContext('.');
			context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
			context.registerNamedObjectsFromSubModels();
			try (CSVReader reader = new CSVReader(',', inputStream)) {

				final AssignmentImporter importer = new AssignmentImporter();
				importer.importAssignments(reader, context);
			}
			context.run();

		}
	}

	public void importCommodityCurves(InputStream inputStream) throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		{

			final DefaultImportContext context = new DefaultImportContext('.');
			context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
			context.registerNamedObjectsFromSubModels();
			try (CSVReader reader = new CSVReader(',', inputStream)) {

				final CommodityIndexImporter importer = new CommodityIndexImporter();
				pricingModel.getCommodityCurves().addAll((Collection<? extends CommodityCurve>) importer.importObjects(PricingPackage.Literals.COMMODITY_CURVE, reader, context));
			}
			context.run();

		}
	}

	public void importSpotMarkets(InputStream inputStream) throws IOException {
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);

		final DefaultImportContext context = new DefaultImportContext('.');
		context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
		context.registerNamedObjectsFromSubModels();
		try (CSVReader reader = new CSVReader(',', inputStream)) {

			final DefaultClassImporter importer = new DefaultClassImporter();

			final Collection<EObject> markets = (importer.importObjects(SpotMarketsPackage.eINSTANCE.getSpotMarket(), reader, context));

			SpotMarketGroup desPurchaseGroup = null;
			SpotMarketGroup desSaleGroup = null;
			SpotMarketGroup fobPurchaseGroup = null;
			SpotMarketGroup fobSaleGroup = null;

			final List<SpotMarket> desPurchaseMarkets = new LinkedList<>();
			final List<SpotMarket> desSaleMarkets = new LinkedList<>();
			final List<SpotMarket> fobPurchaseMarkets = new LinkedList<>();
			final List<SpotMarket> fobSaleMarkets = new LinkedList<>();
			for (final EObject market : markets) {

				if (market instanceof SpotMarketGroup) {
					final SpotMarketGroup group = (SpotMarketGroup) market;
					switch (group.getType()) {
					case DES_PURCHASE:
						desPurchaseGroup = group;
						break;
					case DES_SALE:
						desSaleGroup = group;
						break;
					case FOB_PURCHASE:
						fobPurchaseGroup = group;
						break;
					case FOB_SALE:
						fobSaleGroup = group;
						break;
					default:
						break;

					}
				} else if (market instanceof DESPurchaseMarket) {
					desPurchaseMarkets.add((SpotMarket) market);
				} else if (market instanceof DESSalesMarket) {
					desSaleMarkets.add((SpotMarket) market);
				} else if (market instanceof FOBPurchasesMarket) {
					fobPurchaseMarkets.add((SpotMarket) market);
				} else if (market instanceof FOBSalesMarket) {
					fobSaleMarkets.add((SpotMarket) market);
				}

			}
			// Set the groups
			spotMarketsModel.setDesPurchaseSpotMarket(desPurchaseGroup);
			spotMarketsModel.setDesSalesSpotMarket(desSaleGroup);
			spotMarketsModel.setFobPurchasesSpotMarket(fobPurchaseGroup);
			spotMarketsModel.setFobSalesSpotMarket(fobSaleGroup);

			// set the markets
			if (desPurchaseGroup != null) {
				desPurchaseGroup.getMarkets().addAll(desPurchaseMarkets);
			}
			if (desSaleGroup != null) {
				desSaleGroup.getMarkets().addAll(desSaleMarkets);
			}
			if (fobPurchaseGroup != null) {
				fobPurchaseGroup.getMarkets().addAll(fobPurchaseMarkets);
			}
			if (fobSaleGroup != null) {
				fobSaleGroup.getMarkets().addAll(fobSaleMarkets);
			}

			context.run();

		}
	}

	public void importContracts(InputStream purchaseIS, InputStream salesIS) throws IOException {
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
		{

			final DefaultImportContext context = new DefaultImportContext('.');
			context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
			context.registerNamedObjectsFromSubModels();
			try (CSVReader reader = new CSVReader(',', purchaseIS)) {

				final DefaultClassImporter importer = new DefaultClassImporter();
				commercialModel.getPurchaseContracts().addAll((Collection<? extends PurchaseContract>) importer.importObjects(CommercialPackage.Literals.PURCHASE_CONTRACT, reader, context));
			}
			try (CSVReader reader = new CSVReader(',', salesIS)) {

				final DefaultClassImporter importer = new DefaultClassImporter();
				commercialModel.getSalesContracts().addAll((Collection<? extends SalesContract>) importer.importObjects(CommercialPackage.Literals.SALES_CONTRACT, reader, context));
			}
			context.run();

		}
	}

	public void importCargoes(InputStream inputStream) throws IOException {

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		final CargoImporter cargoImporter = new CargoImporter();
		final DefaultImportContext context = new DefaultImportContext('.');
		context.setRootObject(scenarioModelBuilder.getLNGScenarioModel());
		context.registerNamedObjectsFromSubModels();
		try (CSVReader reader = new CSVReader(',', inputStream)) {

			final Collection<EObject> values = cargoImporter.importObjects(CargoPackage.eINSTANCE.getCargo(), reader, context);
			// COPIED FROM CARGO IMPORTER.
			for (final EObject object : values) {
				if (object instanceof Cargo) {
					cargoModel.getCargoes().add((Cargo) object);
				} else if (object instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) object;
					cargoModel.getLoadSlots().add(loadSlot);
					// Set default pricing event if no delegate or previously set value
					context.doLater(new IDeferment() {
						@Override
						public void run(final IImportContext context) {
							if (!(loadSlot instanceof SpotSlot) && loadSlot.getContract() == null) {
								if (loadSlot.getPricingEvent() == null) {
									loadSlot.setPricingEvent(PricingEvent.START_LOAD);
								}
							}
						}

						@Override
						public int getStage() {
							return 0;
						}
					});
				} else if (object instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) object;
					cargoModel.getDischargeSlots().add(dischargeSlot);
					// Set default pricing event if no delegate or previously set value
					context.doLater(new IDeferment() {

						@Override
						public void run(final IImportContext context) {
							if (!(dischargeSlot instanceof SpotSlot) && dischargeSlot.getContract() == null) {
								if (dischargeSlot.getPricingEvent() == null) {
									dischargeSlot.setPricingEvent(PricingEvent.START_DISCHARGE);
								}
							}
						}

						@Override
						public int getStage() {
							return IMMXImportContext.STAGE_REFERENCES_RESOLVED;
						}
					});
				}
			}
			context.run();
		}
	}
}
