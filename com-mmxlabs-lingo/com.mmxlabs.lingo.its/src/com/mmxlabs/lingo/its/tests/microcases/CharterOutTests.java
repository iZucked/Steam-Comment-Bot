/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class CharterOutTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	/**
	 * Test: Test if the optimisation charter out a vessel if assigned i.e There is not cargo but only a charter out event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCharterOutOnly() throws Exception {

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 29, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 10, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(500_000) //
				.withDurationInDays(10) //
				.withOptional(false) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check that's a cargoes free scenario
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});
	}

	/**
	 * Test: Test if the optimisation charter out a vessel if assigned and optional i.e There is not cargo but only a charter out event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutOnlyWithAssignment() throws Exception {

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 29, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 10, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(5_000_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check that's a cargoes free scenario
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});

		Assert.assertTrue(true);
	}

	/**
	 * Test: Test if the optimisation charter out a vessel if optional i.e There is not cargo but only a charter out event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutOnly() throws Exception {

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(0);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 29, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(500_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check that's a cargoes free scenario
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});

		Assert.assertTrue(true);
	}

	/**
	 * Test: Test if the optimisation charter out a vessel when it's a reasonable option at the end of a sequence i.e There is not cargo but a charter out event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCharterOutAtEnd() throws Exception {

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 24, 0, 0, 0), LocalDateTime.of(2017, 12, 29, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(5_000_000) //
				.withDurationInDays(10) //
				.withOptional(false) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 12, 4), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check that's a cargoes free scenario
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});
	}

	/*
	 * ** Test: Test if the optimisation does NOT charter out a vessel because of resulting violation and lateness i.e The vessel's schedule is full of cargo event and the charter out fee is way too
	 * low
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutAtEnd() throws Exception {
		Assert.assertTrue(true);

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(0);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 31, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 25, 0, 0, 0), LocalDateTime.of(2017, 12, 25, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000_000) //
				.withOptional(true) //
				.withDurationInDays(5) //
				.withAllowedVessels(vessel) //
				.build();

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 12, 4), portFinder.findPort("Sabine Pass LNG"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 12, 20), portFinder.findPort("Sabine Pass LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check if the cargo is scheduled
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the optional charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});

	}

	/**
	 * Test: Test if the optimisation charter out a vessel when it's a reasonable option at the end of a sequence i.e There is not cargo but a charter out event. The charter out event is optional and
	 * assigned
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutAtEndWithAssignment() throws Exception {

		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 31, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 25, 0, 0, 0), LocalDateTime.of(2017, 12, 25, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(5_000_000) //
				.withDurationInDays(5) //
				.withOptional(true) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 12, 4), portFinder.findPort("Sabine Pass LNG"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 12, 20), portFinder.findPort("Sabine Pass LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check if the cargo is scheduled
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());
		});
	}

	/*
	 * ** Test: Test if the optimisation does NOT charter out a vessel because of resulting violation and lateness i.e The vessel's schedule is full of cargo event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutBest() throws Exception {
		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(0);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvents
		final CharterOutEvent charterOutEvent1 = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_1", LocalDateTime.of(2017, 12, 05, 0, 0, 0), LocalDateTime.of(2017, 12, 05, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(5000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		// The best one that should be choose
		final CharterOutEvent charterOutEvent2 = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_best", LocalDateTime.of(2017, 12, 05, 0, 0, 0), LocalDateTime.of(2017, 12, 05, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check that the scenario is cargo free
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check that their is only only charter out event schedule
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());

			// Check if the correct charter out event is planned
			if (charterOuts.size() == 1) {
				Assert.assertEquals("charter_out_test_best", charterOuts.get(0).getName());
			}
		});
	}

	/*
	 * ** Test: Test if the optimisation does NOT charter out a vessel because of resulting violation and lateness i.e The vessel's schedule is full of cargo event
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOptionalCharterOutNoAvailability() throws Exception {
		// Create the required basic elements
		// Create a vessel and its availability
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 24, 0, 0, 0), LocalDateTime.of(2017, 12, 29, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 12, 30), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check if the cargo is scheduled
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			// Check if the charter out event is not present
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(0, charterOuts.size());
		});
	}

	/**
	 * We check if the charter out event is present in the resulting cargoModel export After not been included in the optimisation due to period constrain.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExportOptionalCharterOutNotInPeriod() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 12, 1, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2018, 3, 1, 0, 0, 0), LocalDateTime.of(2018, 3, 2, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		evaluateWithLSOTest(true, plan -> {
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 12, 1));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2018, 3));
		}, null, scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			final Sequence sequence = schedule.getSequences().get(0);

			// Check charter out not in the schedule
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(0, charterOuts.size());

			// Check unusedElement
			Assert.assertEquals(0, schedule.getUnusedElements().size());

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, schedule);

			Assert.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that everything is still there
			VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assert.assertNull(availability);
		}, null);
	}

	/**
	 * We check if the charter out event is present in the resulting cargoModel export After not been included in the optimisation due to period constrain.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExportOptionalCharterOutInPeriod() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 12, 1, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2018, 3, 1, 0, 0, 0), LocalDateTime.of(2018, 3, 2, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		evaluateWithLSOTest(true, plan -> {
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 12, 1));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2018, 4));
		}, null, scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			final Sequence sequence = schedule.getSequences().get(0);

			// Check charter out in the schedule
			List<CharterOutEvent> charterOuts = findCOEvents(schedule.getSequences().get(0));
			Assert.assertEquals(1, charterOuts.size());

			// Check no unusedElement
			Assert.assertEquals(0, schedule.getUnusedElements().size());

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, schedule);

			Assert.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that the charter out was not exported out
			VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assert.assertNotNull(availability);
		}, null);
	}

	private List<CharterOutEvent> findCOEvents(Sequence sequence) {
		List<CharterOutEvent> charterOuts = new ArrayList<CharterOutEvent>();
		for (Event e : sequence.getEvents()) {
			if (e instanceof VesselEventVisitImpl) {
				VesselEvent vesselEvent = ((VesselEventVisitImpl) e).getVesselEvent();
				if (vesselEvent instanceof CharterOutEvent) {
					charterOuts.add((CharterOutEvent) (vesselEvent));
				}
			}
		}
		return charterOuts;
	}
}