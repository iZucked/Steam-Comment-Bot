/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(ShiroRunner.class)
@RequireFeature(features = { KnownFeatures.FEATURE_PURGE })
public class PurgeTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurgeAndCooldownAfterDrydockTightVoyage() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setPurgeTime(24);
		vessel.setMinSpeed(15.0);
		vessel.setMaxSpeed(15.0);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		final DryDockEvent drydock = cargoModelBuilder.makeDryDockEvent("drydock", LocalDateTime.of(2017, 12, 2, 0, 0, 0), LocalDateTime.of(2017, 12, 2, 0, 0, 0), portFinder.findPort("Freeport LNG"))
				.withDurationInDays(5) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		// Load date is too soon and we will be late. Ensure we still schedule a purge
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 8), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);

			// Basic time sequencing checks
			Event prev = null;
			for (final Event event : sequence.getEvents()) {

				if (prev != null) {
					Assertions.assertEquals(prev.getEnd(), event.getStart());
				}
				Assertions.assertFalse(event.getEnd().isBefore(event.getStart()));
				Assertions.assertTrue(event.getDuration() >= 0);

				prev = event;

			}

			// Expected sequence of events.
			final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);
			final Idle idle1 = (Idle) sequence.getEvents().get(1);
			final VesselEventVisit eventVisist1 = (VesselEventVisit) sequence.getEvents().get(2);
			final Journey journey1 = (Journey) sequence.getEvents().get(3);
			final Idle idle2 = (Idle) sequence.getEvents().get(4);
			final Purge purge1 = (Purge) sequence.getEvents().get(5);
			final Cooldown cooldown1 = (Cooldown) sequence.getEvents().get(6);
			final SlotVisit load1 = (SlotVisit) sequence.getEvents().get(7);

			// Check purge duration
			Assertions.assertEquals(24, purge1.getDuration());

			// Check heel levels
			Assertions.assertEquals(0, idle2.getHeelAtEnd());
			Assertions.assertEquals(0, purge1.getHeelAtStart());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurgeAndCooldownAfterDrydockWithIdle() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setPurgeTime(24);
		vessel.setMinSpeed(15.0);
		vessel.setMaxSpeed(15.0);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		final DryDockEvent drydock = cargoModelBuilder.makeDryDockEvent("drydock", LocalDateTime.of(2017, 12, 2, 0, 0, 0), LocalDateTime.of(2017, 12, 2, 0, 0, 0), portFinder.findPort("Freeport LNG"))
				.withDurationInDays(5) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		// Load date is too soon and we will be late. Ensure we still schedule a purge
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 20), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 30), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluatePurgeTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);

			// Basic time sequencing checks
			Event prev = null;
			for (final Event event : sequence.getEvents()) {

				if (prev != null) {
					Assertions.assertEquals(prev.getEnd(), event.getStart());
				}
				Assertions.assertFalse(event.getEnd().isBefore(event.getStart()));
				Assertions.assertTrue(event.getDuration() >= 0);

				prev = event;

			}

			// Expected sequence of events.
			final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);
			final Idle idle1 = (Idle) sequence.getEvents().get(1);
			final VesselEventVisit eventVisist1 = (VesselEventVisit) sequence.getEvents().get(2);
			final Journey journey1 = (Journey) sequence.getEvents().get(3);
			final Idle idle2 = (Idle) sequence.getEvents().get(4);
			final Purge purge1 = (Purge) sequence.getEvents().get(5);
			final Cooldown cooldown1 = (Cooldown) sequence.getEvents().get(6);
			final SlotVisit load1 = (SlotVisit) sequence.getEvents().get(7);

			// Check purge duration
			Assertions.assertEquals(24, purge1.getDuration());

			// Check heel levels
			Assertions.assertEquals(0, idle2.getHeelAtEnd());
			Assertions.assertEquals(0, purge1.getHeelAtStart());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testScheduledPurgeAndCooldownTightVoyage() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setPurgeTime(24);
		vessel.setMinSpeed(15.0);
		vessel.setMaxSpeed(15.0);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		// Load date is too soon and we will be late. Ensure we still schedule a purge
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 8), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.with(s -> ((LoadSlot) s).setSchedulePurge(true)) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluatePurgeTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);

			// Basic time sequencing checks
			Event prev = null;
			for (final Event event : sequence.getEvents()) {

				if (prev != null) {
					Assertions.assertEquals(prev.getEnd(), event.getStart());
				}
				Assertions.assertFalse(event.getEnd().isBefore(event.getStart()));
				Assertions.assertTrue(event.getDuration() >= 0);

				prev = event;

			}

			// Expected sequence of events.
			final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);
			final Idle idle1 = (Idle) sequence.getEvents().get(1);
			final Purge purge1 = (Purge) sequence.getEvents().get(2);
			final Cooldown cooldown1 = (Cooldown) sequence.getEvents().get(3);
			final SlotVisit load1 = (SlotVisit) sequence.getEvents().get(4);

			// Check purge duration
			Assertions.assertEquals(24, purge1.getDuration());

			// Check heel levels
			Assertions.assertEquals(0, idle1.getHeelAtEnd());
			Assertions.assertEquals(0, purge1.getHeelAtStart());
		});
	}

	public void evaluatePurgeTest(@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(OptimiserInjectorServiceMaker.begin() //
						.withModuleOverrideBindNamedInstance(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_SchedulePurges, boolean.class, Boolean.FALSE) //
						.make()) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, checker);
		} finally {
			runnerBuilder.dispose();
		}
	}
}
