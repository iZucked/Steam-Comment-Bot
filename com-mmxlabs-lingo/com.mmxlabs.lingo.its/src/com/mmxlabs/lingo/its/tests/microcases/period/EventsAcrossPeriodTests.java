/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
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
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Test where an event crosses the whole period.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class EventsAcrossPeriodTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCharterOutEventAcrossPeriod() throws Exception {

		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");

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

		userSettings.setPeriodStartDate(LocalDate.of(2015, 3, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assertions.assertEquals("charter-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			{
				final Port startAt = vesselAvailability.getStartAt();
				Assertions.assertNotNull(startAt);
				Assertions.assertEquals("Ras Laffan", startAt.getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assertions.assertEquals(1, endAt.size());
				Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkDryDockEventAcrossPeriod() throws Exception {

		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");

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

		userSettings.setPeriodStartDate(LocalDate.of(2015, 3, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assertions.assertEquals("drydock-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			{
				final Port startAt = vesselAvailability.getStartAt();
				Assertions.assertNotNull(startAt);
				Assertions.assertEquals("Ras Laffan", startAt.getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assertions.assertEquals(1, endAt.size());
				Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}
			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkMaintenceEventAcrossPeriod() throws Exception {
		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");

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

		userSettings.setPeriodStartDate(LocalDate.of(2015, 3, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data

		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getVesselEvents().size());
			Assertions.assertEquals("event-2", optimiserScenario.getCargoModel().getVesselEvents().get(0).getName());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);

			{
				final Port startAt = vesselAvailability.getStartAt();
				Assertions.assertNotNull(startAt);
				Assertions.assertEquals("Ras Laffan", startAt.getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assertions.assertEquals(1, endAt.size());
				Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}
			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCargoAcrossPeriod() throws Exception {

		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");
		vessel_1.setSafetyHeel(1000);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 01, 0, 0, 0), LocalDateTime.of(2015, 9, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 1, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 1, 14), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 2, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 4, 1), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAssignmentFlags(true, false) //
				.build();
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2015, 7, 1), portFinder.findPort("Ras Laffan"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2015, 8, 1), portFinder.findPort("Mina Al Ahmadi"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
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

		userSettings.setPeriodStartDate(LocalDate.of(2015, 3, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals("L2", optimiserScenario.getCargoModel().getCargoes().get(0).getLoadName());
			{
				final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
				final Port startAt = vesselAvailability.getStartAt();
				Assertions.assertNotNull(startAt);
				Assertions.assertEquals("Ras Laffan", startAt.getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());
			}
			{
				final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
				final List<APortSet<Port>> endAt = vesselAvailability.getEndAt();
				Assertions.assertEquals(1, endAt.size());
				Assertions.assertEquals("Ras Laffan", endAt.get(0).getName());
				final ZoneId rasLaffanTimeZone = portFinder.findPort("Ras Laffan").getZoneId();
				final ZoneId utcTimeZone = ZoneId.of("UTC");
				// Vessel availabilities always in UTC
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndAfterAsDateTime());
				Assertions.assertEquals(ZonedDateTime.of(2015, 7, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getEndByAsDateTime());
			}

			// Ensure heel value matches
			Assertions.assertEquals(vessel_1.getVesselOrDelegateSafetyHeel(), optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMinVolumeAvailable(), 0.0001);
			Assertions.assertEquals(vessel_1.getVesselOrDelegateSafetyHeel(), optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMaxVolumeAvailable(), 0.0001);

			// Assert initial state can be evaluted
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}
}