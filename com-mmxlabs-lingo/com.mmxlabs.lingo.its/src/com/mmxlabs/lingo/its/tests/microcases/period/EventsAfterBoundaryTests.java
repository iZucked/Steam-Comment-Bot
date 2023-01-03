/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;

@ExtendWith(ShiroRunner.class)
public class EventsAfterBoundaryTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCharterOutEventAfterBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter_1 = cargoModelBuilder.makeVesselCharter(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		final CharterOutEvent charterOutEvent = cargoModelBuilder
				.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withVesselAssignment(vesselCharter_1, 1) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		Assertions.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

		final VesselCharter vesselCharter = optimiserScenario.getCargoModel().getVesselCharters().get(0);
		final List<APortSet<Port>> endAt = vesselCharter.getEndAt();
		Assertions.assertEquals(1, endAt.size());
		Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
		final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
		final ZoneId utcTimeZone = ZoneId.of("UTC");
		// Vessel charters always in UTC
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndAfterAsDateTime());
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndByAsDateTime());

		// Assert initial state can be evaluted
		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		// Validate the initial sequences are valid
		Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkDryDockEventAfterBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter_1 = cargoModelBuilder.makeVesselCharter(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		final DryDockEvent charterOutEvent = cargoModelBuilder
				.makeDryDockEvent("drydock-1", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselCharter_1, 1) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		Assertions.assertTrue(optimiserScenario.getCargoModel().getCargoes().isEmpty());

		final VesselCharter vesselCharter = optimiserScenario.getCargoModel().getVesselCharters().get(0);
		final List<APortSet<Port>> endAt = vesselCharter.getEndAt();
		Assertions.assertEquals(1, endAt.size());
		Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
		final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
		final ZoneId utcTimeZone = ZoneId.of("UTC");
		// Vessel charters always in UTC
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndAfterAsDateTime());
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndByAsDateTime());

		// Assert initial state can be evaluated
		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		// Validate the initial sequences are valid
		Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkMaintenceEventAfterBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter_1 = cargoModelBuilder.makeVesselCharter(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		final MaintenanceEvent event = cargoModelBuilder
				.makeMaintenanceEvent("charter-1", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselCharter_1, 1) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		Assertions.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

		final VesselCharter vesselCharter = optimiserScenario.getCargoModel().getVesselCharters().get(0);
		final List<APortSet<Port>> endAt = vesselCharter.getEndAt();
		Assertions.assertEquals(1, endAt.size());
		Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
		final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
		final ZoneId utcTimeZone = ZoneId.of("UTC");
		// Vessel charters always in UTC
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndAfterAsDateTime());
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndByAsDateTime());

		// Assert initial state can be evaluted
		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		// Validate the initial sequences are valid
		Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCargoAfterBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter_1 = cargoModelBuilder.makeVesselCharter(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_MINA_AL_AHMADI), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselCharter_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2011, 5, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		Assertions.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

		final VesselCharter vesselCharter = optimiserScenario.getCargoModel().getVesselCharters().get(0);
		final List<APortSet<Port>> endAt = vesselCharter.getEndAt();
		Assertions.assertEquals(1, endAt.size());
		Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
		final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
		final ZoneId utcTimeZone = ZoneId.of("UTC");
		// Vessel charters always in UTC
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndAfterAsDateTime());
		Assertions.assertEquals(ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselCharter.getEndByAsDateTime());

		// Assert initial state can be evaluted
		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		// Validate the initial sequences are valid
		Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void evaluateCharterOutEventWithRelocationAsBoundaryEvent() throws Exception {

//		// Load in the basic scenario from CSV
//
//		final @NonNull String urlRoot = getClass().getResource("/referencedata/reference-data-1/").toString();
//		final CSVImporter importer = new CSVImporter();
//		importer.importPortData(urlRoot);
//		importer.importCostData(urlRoot);
//		importer.importEntityData(urlRoot);
//		importer.importFleetData(urlRoot);
//		importer.importMarketData(urlRoot);
//		importer.importPromptData(urlRoot);
//		importer.importMarketData(urlRoot);
//
//		final IScenarioDataProvider scenarioDataProvider = importer.doImport();
//		;
//		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
//
//		// Create finder and builder
//		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
//		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);
//
//		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
//		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
//		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();
//
//		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
//		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
//		// final SpotMarketsModelBuilder spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();
//
//		// Create the required basic elements
//		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter_1 = cargoModelBuilder.makeVesselCharter(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 5, 29), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel_1, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 6, 27), portFinder.findPortById(InternalDataConstants.PORT_MINA_AL_AHMADI), null, entity, "7") //
				.withRestrictedVessels(vessel_1, true) //
				.build() //
				.withVesselAssignment(vesselCharter_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		final CharterOutEvent charterOutEvent = cargoModelBuilder
				.makeCharterOutEvent("charter-1", LocalDateTime.of(2016, 7, 3, 0, 0, 0), LocalDateTime.of(2016, 7, 6, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_TANNGUH)) //
				.withVesselAssignment(vesselCharter_1, 1) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2016, 2, 1));
		userSettings.setPeriodEnd(YearMonth.of(2016, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings,  lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		// Validate the initial sequences are valid
		Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}
}