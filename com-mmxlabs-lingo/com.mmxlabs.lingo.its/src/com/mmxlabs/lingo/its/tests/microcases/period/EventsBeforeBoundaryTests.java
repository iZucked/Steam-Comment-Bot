/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;

@RunWith(value = ShiroRunner.class)
public class EventsBeforeBoundaryTests extends AbstractMicroTestCase {

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkCharterOutEventBeforeBoundary() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder.makeCharterOutEvent("charter-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Assert.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkDryDockEventBeforeBoundary() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		cargoModelBuilder.makeDryDockEvent("drydock-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withDurationInDays(1) //
				.build(); //

		cargoModelBuilder.makeDryDockEvent("drydock-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.withDurationInDays(20) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assert.assertEquals("drydock-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkMaintenceEventBeforeBoundary() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeMaintenanceEvent("event-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder.makeMaintenanceEvent("event-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkCargoBeforeBoundary() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		// Set a min heel - used in test results later
		vesselClass.setMinHeel(1000);

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 1, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 2, 14), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 2,
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 3, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(24) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 4, 2), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(24) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2015, 3, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Load slot is in the boundary, so make sure it is locked.
			Cargo opt_cargo2 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assert.assertTrue(opt_cargo2.getSortedSlots().get(0).isLocked());
			Assert.assertEquals(1, opt_cargo2.getSortedSlots().get(0).getAllowedVessels().size());

			// Discharge should be free
			Assert.assertFalse(opt_cargo2.getSortedSlots().get(1).isLocked());
			Assert.assertEquals(0, opt_cargo2.getSortedSlots().get(1).getAllowedVessels().size());

			// Ensure heel value matches
			Assert.assertEquals(vessel_1.getVesselClass().getMinHeel(), optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMaxVolumeAvailable(), 0.0);

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkCargoBeforeBoundaryAndVesselEndDate() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		// Set a min heel - used in test results later
		vesselClass.setMinHeel(1000);

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2016, 8, 01, 0, 0, 0), LocalDateTime.of(2016, 8, 01, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 30, 23, 0, 0), LocalDateTime.of(2017, 4, 30, 23, 0, 0)) //
				.build();

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 11, 9), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 11, 21), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2017, 1));
		userSettings.setPeriodEnd(YearMonth.of(2017, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Point Fortin", startAt.getName());
			final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Point Fortin").getTimeZone());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2016, 8, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2016, 8, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Load slot is in the out section, so make sure it is locked.
			Cargo opt_cargo2 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assert.assertTrue(opt_cargo2.isLocked());
			Assert.assertFalse(opt_cargo2.isAllowRewiring());

			// Ensure heel value matches
			Assert.assertEquals(0.0, optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMaxVolumeAvailable(), 0.0);

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

	/**
	 * Both charters retained so ballast leg across period is present.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkCharterOutEventBeforeBoundaryRetained() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder.makeCharterOutEvent("charter-2", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Assert.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assert.assertNotNull(startAt);
			Assert.assertEquals("Point Fortin", startAt.getName());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assert.assertEquals(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assert.assertEquals(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}

}