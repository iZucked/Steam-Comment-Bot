/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
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
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Test where an event crosses the whole period.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class EventsAcrossPeriodTests extends AbstractMicroTestCase {

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void checkCharterOutEventAcrossPeriod() throws Exception {

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
				.withEndWindow(LocalDateTime.of(2015, 9, 01, 0, 0, 0), LocalDateTime.of(2015, 9, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 1, 1, 0, 0, 0), LocalDateTime.of(2015, 1, 1, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.build(); //

		cargoModelBuilder.makeCharterOutEvent("charter-2", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withDurationInDays(100) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder.makeCharterOutEvent("charter-3", LocalDateTime.of(2015, 7, 1, 0, 0, 0), LocalDateTime.of(2015, 7, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 3));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assert.assertEquals("charter-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			{
				final List<APortSet<Port>> startAt = vesselAvailability.getStartAt();
				Assert.assertEquals(1, startAt.size());
				Assert.assertEquals("Ras Laffan", startAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assert.assertEquals(1, endAt.size());
				Assert.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}

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
	public void checkDryDockEventAcrossPeriod() throws Exception {

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
				.withEndWindow(LocalDateTime.of(2015, 9, 01, 0, 0, 0), LocalDateTime.of(2015, 9, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeDryDockEvent("drydock-1", LocalDateTime.of(2015, 1, 1, 0, 0, 0), LocalDateTime.of(2015, 1, 1, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.build(); //
		cargoModelBuilder.makeDryDockEvent("drydock-2", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withDurationInDays(100) //
				.build(); //
		cargoModelBuilder.makeDryDockEvent("drydock-3", LocalDateTime.of(2015, 7, 1, 0, 0, 0), LocalDateTime.of(2015, 7, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 3));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assert.assertEquals("drydock-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			{
				final List<APortSet<Port>> startAt = vesselAvailability.getStartAt();
				Assert.assertEquals(1, startAt.size());
				Assert.assertEquals("Ras Laffan", startAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assert.assertEquals(1, endAt.size());
				Assert.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}
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
	public void checkMaintenceEventAcrossPeriod() throws Exception {
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
				.withEndWindow(LocalDateTime.of(2015, 9, 01, 0, 0, 0), LocalDateTime.of(2015, 9, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeMaintenanceEvent("event-1", LocalDateTime.of(2015, 1, 1, 0, 0, 0), LocalDateTime.of(2015, 1, 1, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.build(); //

		cargoModelBuilder.makeMaintenanceEvent("event-2", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withDurationInDays(100) //
				.build(); //

		cargoModelBuilder.makeMaintenanceEvent("event-3", LocalDateTime.of(2015, 7, 1, 0, 0, 0), LocalDateTime.of(2015, 7, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 3));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assert.assertEquals("event-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);

			{
				final List<APortSet<Port>> startAt = vesselAvailability.getStartAt();
				Assert.assertEquals(1, startAt.size());
				Assert.assertEquals("Ras Laffan", startAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assert.assertEquals(1, endAt.size());
				Assert.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}
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
	public void checkCargoAcrossPeriod() throws Exception {

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
		vesselClass.setMinHeel(1000);

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 01, 0, 0, 0), LocalDateTime.of(2015, 9, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 1, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 1, 14), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 2, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 4, 1), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAssignmentFlags(true, false) //
				.build();
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2015, 7, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2015, 8, 1), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 3));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals("L2", optimiserScenario.getCargoModel().getCargoes().get(0).getLoadName());
			{
				final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
				final List<APortSet<Port>> startAt = vesselAvailability.getStartAt();
				Assert.assertEquals(1, startAt.size());
				Assert.assertEquals("Ras Laffan", startAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assert.assertEquals(1, endAt.size());
				Assert.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = ZoneId.of(portFinder.findPort("Ras Laffan").getTimeZone());
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assert.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}

			// Ensure heel value matches
			Assert.assertEquals(vessel_1.getVesselClass().getMinHeel(), optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getVolumeAvailable(), 0.0);

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}
}