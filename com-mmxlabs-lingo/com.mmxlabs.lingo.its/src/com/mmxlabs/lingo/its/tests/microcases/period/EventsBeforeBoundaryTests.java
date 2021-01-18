/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;

@ExtendWith(ShiroRunner.class)
public class EventsBeforeBoundaryTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCharterOutEventBeforeBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder
				.makeCharterOutEvent("charter-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

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
			// Assertions.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

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
	public void checkDryDockEventBeforeBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		cargoModelBuilder.makeDryDockEvent("drydock-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withDurationInDays(1) //
				.build(); //

		cargoModelBuilder.makeDryDockEvent("drydock-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.withDurationInDays(20) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

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
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluated
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
	public void checkMaintenceEventBeforeBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeMaintenanceEvent("event-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder
				.makeMaintenanceEvent("event-2", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

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

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 30, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

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
	public void checkCargoBeforeBoundary() throws Exception {

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Set a min heel - used in test results later
		vessel_1.setSafetyHeel(1000);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 2, 14), portFinder.findPortById(InternalDataConstants.PORT_MINA_AL_AHMADI), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability_1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 2,
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(24) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 4, 2), portFinder.findPortById(InternalDataConstants.PORT_MINA_AL_AHMADI), null, entity, "7") //
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

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

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

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Ras Laffan", startAt.getName());
			final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN).getZoneId();
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2015, 3, 1, 0, 0, 0, 0, rasLaffanTimeZone).withZoneSameInstant(utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Load slot is in the boundary, so make sure it is locked.
			Cargo opt_cargo2 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assertions.assertTrue(opt_cargo2.getSortedSlots().get(0).isLocked());
			Assertions.assertEquals(1, opt_cargo2.getSortedSlots().get(0).getSlotOrDelegateVesselRestrictions().size());

			// Discharge should be free
			Assertions.assertFalse(opt_cargo2.getSortedSlots().get(1).isLocked());
			Assertions.assertEquals(0, opt_cargo2.getSortedSlots().get(1).getSlotOrDelegateVesselRestrictions().size());
			Assertions.assertFalse(opt_cargo2.getSortedSlots().get(1).getSlotOrDelegateVesselRestrictionsArePermissive());

			// Ensure heel value matches
			Assertions.assertEquals(vessel_1.getVesselOrDelegateSafetyHeel(), optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMaxVolumeAvailable(), 0.0001);

			// Assert initial state can be evaluated
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
	public void checkCargoBeforeBoundaryAndVesselEndDate() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Set a min heel - used in test results later
		vessel.setSafetyHeel(1000);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2016, 8, 01, 0, 0, 0), LocalDateTime.of(2016, 8, 01, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 30, 23, 0, 0), LocalDateTime.of(2017, 4, 30, 23, 0, 0)) //
				.build();

		// Create cargo 1,
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 11, 9), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 11, 21), portFinder.findPortById(InternalDataConstants.PORT_MINA_AL_AHMADI), null, entity, "7") //
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

		userSettings.setPeriodStartDate(LocalDate.of(2017, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2017, 5));

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

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Point Fortin", startAt.getName());
			final ZoneId rasLaffanTimeZone = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN).getZoneId();
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2016, 8, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2016, 8, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Load slot is in the out section, so make sure it is locked.
			Cargo opt_cargo2 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assertions.assertTrue(opt_cargo2.isLocked());
			Assertions.assertFalse(opt_cargo2.isAllowRewiring());

			// Ensure heel value matches
			Assertions.assertEquals(0.0, optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getStartHeel().getMaxVolumeAvailable(), 0.001);

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	/**
	 * Both charters retained so ballast leg across period is present.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void checkCharterOutEventBeforeBoundaryRetained() throws Exception {
 

		final Vessel vessel_1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 2, 1, 0, 0, 0), LocalDateTime.of(2015, 2, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.build(); //

		cargoModelBuilder.makeCharterOutEvent("charter-2", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN)) //
				.withDurationInDays(20) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withVesselAssignment(vesselAvailability_1, 2) //
				.build(); //

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

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
			// Assertions.assertTrue(optimiserScenario.getCargoModel().getVesselEvents().isEmpty());

			final VesselAvailability vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			final Port startAt = vesselAvailability.getStartAt();
			Assertions.assertNotNull(startAt);
			Assertions.assertEquals("Point Fortin", startAt.getName());
			final ZoneId utcTimeZone = ZoneId.of("UTC");
			// Vessel availabilities always in UTC
			Assertions.assertEquals(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartAfterAsDateTime());
			Assertions.assertEquals(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, utcTimeZone), vesselAvailability.getStartByAsDateTime());

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

}