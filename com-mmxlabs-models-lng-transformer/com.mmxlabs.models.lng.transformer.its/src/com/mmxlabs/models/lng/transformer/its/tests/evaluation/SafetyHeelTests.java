/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

@RunWith(value = ShiroRunner.class)
public class SafetyHeelTests extends AbstractShippingCalculationsTestClass {

	@Test
	public void testStartEventLimitedHeel() {
		System.err.println("\n\n Limited Start Heel forces a cooldown");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, // start to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to end
				EndEvent.class };
		// change from default scenario
		final SequenceTester checker = getDefaultTester(classes);

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(1);
		// FIXME: These need to match the cargo defaults....
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		msc.vc.setMinHeel(5000);

		// Push up base fuel price for force NBO+FBO
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);

		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);
		checker.baseFuelPricePerMT = 100;

		msc.vessel.getVesselClass().setWarmingTime(0);

		msc.setupCooldown(20.0);

		// change from default scenario
		// first journey should use NBO and base fuel (not just base fuel)

		// Orphan Ballast
		// int totalOprhanLNGUsed = 0;
		{
			// Forced BF use
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 0;
			final int expectedFBO = expectedDuration * 0;
			final int expectedBF = expectedDuration * 15;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 0);

			// cost of first journey should be changed accordingly
			// final int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 100;
			// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 0);

			// totalOprhanLNGUsed += expectedNBO;
			// totalOprhanLNGUsed += expectedFBO;
		}
		final int expectedHeelRolledOver = 0;

		int totalLNGUsed = 0;
		// Laden leg
		{
			final int expectedDuration = 2;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 5;
			final int expectedBF = expectedDuration * 0;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 1);

			// final int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 100;
			// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 1);

			totalLNGUsed += expectedNBO;
			totalLNGUsed += expectedFBO;
		}
		// Laden idle -- same as default
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			// checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Idle.class, 1);
			// checker.setExpectedValue(0, Expectations.BF_USAGE, Idle.class, 1);
			//
			// int expectedCosts = (expectedNBO) * 21;
			// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Idle.class, 1);

			totalLNGUsed += expectedNBO;
		}
		// Ballast leg
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 5;
			final int expectedBF = expectedDuration * 0;

			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 2);

			// 15m3 * 21 (CV) * 1 (price) = 315 (per time units)
			// final int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 100;
			// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 2);

			totalLNGUsed += expectedNBO;
			totalLNGUsed += expectedFBO;
		}
		// No ballast idle

		final int vesselCapacity = (int) (msc.vessel.getVesselOrVesselClassCapacity() * msc.vessel.getVesselOrVesselClassFillCapacity());
		final int maxQuantity = msc.cargo.getSlots().get(0).getMaxQuantity();
		final int expectedLoadVolume = Math.min(vesselCapacity, maxQuantity) - expectedHeelRolledOver;
		// change from default scenario
		// first load should be only 5010
		// 5010 = 10000 [vessel capacity] - (5000 [start heel] - 10 [journey boiloff])
		checker.setExpectedValue(expectedLoadVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);
		final int expectedDischargeVolume = expectedLoadVolume + expectedHeelRolledOver - totalLNGUsed;
		checker.setExpectedValue(-expectedDischargeVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 1);

		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testStartEventExcessHeel() {
		System.err.println("\n\nGenerous Start Heel Means NBO on First Voyage and LNG Rollover");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario
		final SequenceTester checker = getDefaultTester();

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(5000);
		// FIXME: These need to match the cargo defaults....
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		// Push up base fuel price for force NBO+FBO
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);

		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);
		checker.baseFuelPricePerMT = 100;

		// change from default scenario
		// first journey should use NBO and base fuel (not just base fuel)

		// Orphan Ballast
		int totalOprhanLNGUsed = 0;
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 5;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(0, Expectations.BF_USAGE, Journey.class, 0);

			// cost of first journey should be changed accordingly
			// 15m3 * 21 (CV) * 1 (price) = 315
			final int expectedCosts = (expectedNBO + expectedFBO) * 21;
			checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 0);

			totalOprhanLNGUsed += expectedNBO;
			totalOprhanLNGUsed += expectedFBO;
		}
		final int expectedHeelRolledOver = 5000 - totalOprhanLNGUsed;

		int totalLNGUsed = 0;
		// Laden leg
		{
			final int expectedDuration = 2;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 5;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(0, Expectations.BF_USAGE, Journey.class, 1);

			// 15m3 * 21 (CV) * 1 (price) = 315 (per time units)
			final int expectedCosts = (expectedNBO + expectedFBO) * 21;
			checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 1);

			totalLNGUsed += expectedNBO;
			totalLNGUsed += expectedFBO;
		}
		// Laden idle -- same as default
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			// checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Idle.class, 1);
			// checker.setExpectedValue(0, Expectations.BF_USAGE, Idle.class, 1);
			//
			// int expectedCosts = (expectedNBO) * 21;
			// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Idle.class, 1);

			totalLNGUsed += expectedNBO;
		}
		// Ballast leg
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 5;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(0, Expectations.BF_USAGE, Journey.class, 2);

			// 15m3 * 21 (CV) * 1 (price) = 315 (per time units)
			final int expectedCosts = (expectedNBO + expectedFBO) * 21;
			checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 2);

			totalLNGUsed += expectedNBO;
			totalLNGUsed += expectedFBO;
		}
		// No ballast idle

		final int vesselCapacity = (int) (msc.vessel.getVesselOrVesselClassCapacity() * msc.vessel.getVesselOrVesselClassFillCapacity());
		final int maxQuantity = msc.cargo.getSlots().get(0).getMaxQuantity();
		final int expectedLoadVolume = Math.min(vesselCapacity, maxQuantity) - expectedHeelRolledOver;
		// change from default scenario
		// first load should be only 5010
		// 5010 = 10000 [vessel capacity] - (5000 [start heel] - 10 [journey boiloff])
		checker.setExpectedValue(expectedLoadVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);
		final int expectedDischargeVolume = expectedLoadVolume + expectedHeelRolledOver - totalLNGUsed;
		checker.setExpectedValue(-expectedDischargeVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 1);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	// Not valid as we try to force heel use if available
	// @Test
	// public void testStartEventHeelIgnored() {
	// System.err.println("\n\nPlenty of heel, but base fuel is cheaper");
	// final MinimalScenarioCreator msc = new MinimalScenarioCreator();
	// final LNGScenarioModel scenario = msc.buildScenario();
	//
	// // change from default scenario
	// final SequenceTester checker = getDefaultTester();
	//
	// final VesselAvailability vesselAvailability = msc.vesselAvailability;
	// vesselAvailability.getStartHeel().setVolumeAvailable(5000);
	// // FIXME: These need to match the cargo defaults....
	// vesselAvailability.getStartHeel().setCvValue(21);
	// vesselAvailability.getStartHeel().setPricePerMMBTU(1);
	//
	// // change from default scenario
	//
	// // Orphan Ballast
	// {
	// int expectedDuration = 1;
	// int expectedNBO = expectedDuration * 0;
	// int expectedFBO = expectedDuration * 0;
	// int expectedBF = expectedDuration * 15;
	// checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 0);
	// checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 0);
	// checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 0);
	//
	// int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 10;
	// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 0);
	//
	// }
	// int expectedHeelRolledOver = 0;
	//
	// int totalLNGUsed = 0;
	// // Laden leg
	// {
	// int expectedDuration = 2;
	// int expectedNBO = expectedDuration * 10;
	// int expectedFBO = expectedDuration * 0;
	// int expectedBF = expectedDuration * 5;
	// checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 1);
	// checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 1);
	// checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 1);
	//
	// int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 10;
	// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 1);
	//
	// totalLNGUsed += expectedNBO;
	// totalLNGUsed += expectedFBO;
	// }
	// // Laden idle -- same as default
	// {
	// int expectedDuration = 1;
	// int expectedNBO = expectedDuration * 10;
	// // checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Idle.class, 1);
	// // checker.setExpectedValue(0, Expectations.BF_USAGE, Idle.class, 1);
	// //
	// // int expectedCosts = (expectedNBO) * 21;
	// // checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Idle.class, 1);
	//
	// totalLNGUsed += expectedNBO;
	// }
	// // Ballast leg
	// {
	// int expectedDuration = 1;
	// int expectedNBO = expectedDuration * 0;
	// int expectedFBO = expectedDuration * 0;
	// int expectedBF = expectedDuration * 15;
	// checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 2);
	// checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 2);
	// checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 2);
	//
	// int expectedCosts = (expectedNBO + expectedFBO) * 21 + expectedBF * 10;
	// checker.setExpectedValue(expectedCosts, Expectations.FUEL_COSTS, Journey.class, 2);
	//
	// totalLNGUsed += expectedNBO;
	// totalLNGUsed += expectedFBO;
	// }
	// // No ballast idle
	//
	// int vesselCapacity = (int) (msc.vessel.getVesselOrVesselClassCapacity() * msc.vessel.getVesselOrVesselClassFillCapacity());
	// int maxQuantity = msc.cargo.getSlots().get(0).getMaxQuantity();
	// int expectedLoadVolume = Math.min(vesselCapacity, maxQuantity) - expectedHeelRolledOver;
	// // change from default scenario
	// // first load should be only 5010
	// // 5010 = 10000 [vessel capacity] - (5000 [start heel] - 10 [journey boiloff])
	// checker.setExpectedValue(expectedLoadVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);
	// int expectedDischargeVolume = expectedLoadVolume + expectedHeelRolledOver - totalLNGUsed;
	// checker.setExpectedValue(-expectedDischargeVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 1);
	//
	// final Schedule schedule = ScenarioTools.evaluate(scenario);
	// ScenarioTools.printSequences(schedule);
	//
	// final Sequence sequence = schedule.getSequences().get(0);
	//
	// checker.check(sequence);
	// }

	@Test
	public void testStartEventHeelForcedToStayCold() {
		System.err.println("\n\nPlenty of heel, but base fuel is cheaper, but we will warm up if we do not use NBO");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario
		final SequenceTester checker = getDefaultTester();

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(500);
		// FIXME: These need to match the cargo defaults....
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		// Set up cooldown infrastructure
		msc.vessel.getVesselClass().setWarmingTime(0);
		msc.vessel.getVesselClass().setCoolingVolume(500);
		msc.setupCooldown(0);

		// change from default scenario

		// Orphan Ballast
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 0;
			final int expectedBF = expectedDuration * 5;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 0);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 0);

		}
		// Laden leg
		{
			final int expectedDuration = 2;
			final int expectedNBO = expectedDuration * 10;
			final int expectedFBO = expectedDuration * 0;
			final int expectedBF = expectedDuration * 5;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 1);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 1);
		}
		// Laden idle -- same as default
		// Ballast leg
		{
			final int expectedDuration = 1;
			final int expectedNBO = expectedDuration * 0;
			final int expectedFBO = expectedDuration * 0;
			final int expectedBF = expectedDuration * 15;
			checker.setExpectedValue(expectedNBO, Expectations.NBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(expectedFBO, Expectations.FBO_USAGE, Journey.class, 2);
			checker.setExpectedValue(expectedBF, Expectations.BF_USAGE, Journey.class, 2);
		}
		// No ballast idle

		final int[] legLNG = new int[3];
		for (int i = 0; i < legLNG.length; ++i) {
			final Integer[] journeyNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Journey.class);
			if (journeyNBO[i] != null) {
				legLNG[i] += journeyNBO[i].intValue();
			}
			final Integer[] journeyFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Journey.class);
			if (journeyFBO[i] != null) {
				legLNG[i] += journeyFBO[i].intValue();
			}
			final Integer[] idleNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Idle.class);
			if (idleNBO[i] != null) {
				legLNG[i] += idleNBO[i].intValue();
			}
			final Integer[] idleFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Idle.class);
			if (idleFBO[i] != null) {
				legLNG[i] += idleFBO[i].intValue();
			}
		}

		final int expectedHeelRolledOver = (int) vesselAvailability.getStartHeel().getVolumeAvailable() - legLNG[0];
		final int totalLNGUsed = legLNG[1] + legLNG[2];

		final int vesselCapacity = (int) (msc.vessel.getVesselOrVesselClassCapacity() * msc.vessel.getVesselOrVesselClassFillCapacity());
		final int maxQuantity = msc.cargo.getSlots().get(0).getMaxQuantity();
		final int expectedLoadVolume = Math.min(vesselCapacity, maxQuantity) - expectedHeelRolledOver;
		// change from default scenario
		// first load should be only 5010
		// 5010 = 10000 [vessel capacity] - (5000 [start heel] - 10 [journey boiloff])
		checker.setExpectedValue(expectedLoadVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);
		final int expectedDischargeVolume = expectedLoadVolume + expectedHeelRolledOver - totalLNGUsed;
		checker.setExpectedValue(-expectedDischargeVolume, Expectations.LOAD_DISCHARGE, SlotVisit.class, 1);

		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testCargoHeelRollover() {
		System.err.println("\n\nTest min heel rollover: LNG travel due to expensive BF");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// create an additional cargo
		Cargo secondCargo = msc.createDefaultCargo(msc.loadPort, msc.dischargePort);
		secondCargo.setVesselAssignmentType(msc.vesselAvailability);
		// and send the vessel back to the origin port at end of itinerary
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(500);
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		msc.vc.setMinHeel(500);

		final CostModel costModel = scenario.getReferenceModel().getCostModel();

		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);

		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, // start to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to end
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(classes);

		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 });
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 5, 10, 10, 10, 5 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 10, 20, 20, 20, 10 });

		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 });
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 10, 10, 0 });

		final int[] legLNG = new int[5];
		for (int i = 0; i < legLNG.length; ++i) {
			final Integer[] journeyNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Journey.class);
			if (journeyNBO[i] != null) {
				legLNG[i] += journeyNBO[i].intValue();
			}
			final Integer[] journeyFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Journey.class);
			if (journeyFBO[i] != null) {
				legLNG[i] += journeyFBO[i].intValue();
			}
			final Integer[] idleNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Idle.class);
			if (idleNBO[i] != null) {
				legLNG[i] += idleNBO[i].intValue();
			}
			final Integer[] idleFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Idle.class);
			if (idleFBO[i] != null) {
				legLNG[i] += idleFBO[i].intValue();
			}
		}

		// volume allocations: load 10000 at first load port (loading from empty)
		// at first discharge, retain 530m3 (500 min heel plus 30m3 travel fuel) and 40m3 was used to get here
		// at next load, load back up to full (500 min heel minus 10m3 idle fuel was on board)
		// at next discharge, retain 515m3 (500 min heel plus 15m3 travel fuel) and 40m3 was used to get here
		final int startheel = (int) vesselAvailability.getStartHeel().getVolumeAvailable();
		final int orphanHeel = startheel - legLNG[0];
		final int firstLoad = 10000 - orphanHeel; // No start event heel rollover
		final int firstDischarge = firstLoad + orphanHeel - (legLNG[1] + legLNG[2]) - msc.vc.getMinHeel();
		final int secondLoad = 10000 - msc.vc.getMinHeel();
		final int secondDischarges = secondLoad + msc.vc.getMinHeel() - (legLNG[3] + legLNG[4]); // Can arrive warm

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { firstLoad, -firstDischarge, secondLoad, -secondDischarges });

		checker.baseFuelPricePerMT = 100;
		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	public void testCargoHeelRolloverWithCooldown() {
		System.err.println("\n\nTest min heel rollover: LNG travel due to expensive BF");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// create an additional cargo
		@SuppressWarnings("unused")
		final Cargo secondCargo = msc.createDefaultCargo(msc.loadPort, msc.dischargePort);
		// and send the vessel back to the origin port at end of itinerary
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		secondCargo.setVesselAssignmentType(msc.vesselAvailability);
		
		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(500);
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		msc.vc.setMinHeel(500);

		msc.vessel.getVesselClass().setWarmingTime(0);
		msc.vessel.getVesselClass().setCoolingVolume(500);

		final CostModel costModel = scenario.getReferenceModel().getCostModel();

		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);

		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// Set up cooldown infrastructure
		msc.setupCooldown(0);
		// Place a mnin discharge to force base fuel on ballast
		msc.cargo.getSlots().get(1).setMinQuantity(9970);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, // start to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to load
				Cooldown.class, SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to end
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(classes);

		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 });
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 0, 10, 30, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 5, 0, 0, 10, 5 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 10, 20, 0, 20, 10 });

		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 });
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 10, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 10, 0 });

		final int[] legLNG = new int[5];
		for (int i = 0; i < legLNG.length; ++i) {
			final Integer[] journeyNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Journey.class);
			if (journeyNBO[i] != null) {
				legLNG[i] += journeyNBO[i].intValue();
			}
			final Integer[] journeyFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Journey.class);
			if (journeyFBO[i] != null) {
				legLNG[i] += journeyFBO[i].intValue();
			}
			final Integer[] idleNBO = checker.getExpectedValues(Expectations.NBO_USAGE, Idle.class);
			if (idleNBO[i] != null) {
				legLNG[i] += idleNBO[i].intValue();
			}
			final Integer[] idleFBO = checker.getExpectedValues(Expectations.FBO_USAGE, Idle.class);
			if (idleFBO[i] != null) {
				legLNG[i] += idleFBO[i].intValue();
			}
		}

		final int startheel = (int) vesselAvailability.getStartHeel().getVolumeAvailable();
		final int orphanHeel = startheel - legLNG[0];
		final int firstLoad = 10000 - orphanHeel;
		// No safety heel
		final int firstDischarge = firstLoad + orphanHeel - (legLNG[1] + legLNG[2]);
		final int secondLoad = 10000;
		final int secondDischarges = secondLoad - (legLNG[3] + legLNG[4]); // Can arrive warm

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { firstLoad, -firstDischarge, secondLoad, -secondDischarges });

		// Min heel in m3
		// checker.setExpectedValue(500, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		checker.baseFuelPricePerMT = 100;
		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	public void testEventHeelRollover() {
		System.err.println("\n\nTest min heel rollover for maintenance event: LNG travel due to expensive BF");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// create a maintenance event after the cargo
		final Port eventPort = msc.loadPort;

		VesselEvent event = msc.createDefaultMaintenanceEvent("Maintenance", eventPort, null);
		event.setVesselAssignmentType(msc.vesselAvailability);

		// and recalculate the vessel availability
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		// force a heel rollover at the maintenance port
		msc.vc.setMinHeel(500);

		final CostModel costModel = scenario.getReferenceModel().getCostModel();

		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);

		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		/*
		 * final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, // start to load SlotVisit.class, Journey.class, Idle.class, // load to discharge SlotVisit.class, Journey.class,
		 * Idle.class, // discharge to load SlotVisit.class, Journey.class, Idle.class, // load to discharge SlotVisit.class, Journey.class, Idle.class, // discharge to end EndEvent.class };
		 * 
		 * SequenceTester checker = getDefaultTester(classes);
		 * 
		 * checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 }); checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 0, 0,
		 * 0, 0 }); checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 10, 10, 10, 5 }); checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[]
		 * { 0, 20, 20, 20, 10 });
		 * 
		 * checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 }); checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0
		 * }); checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 }); checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 10,
		 * 10, 0 });
		 * 
		 * // volume allocations: load 10000 at first load port (loading from empty) // at first discharge, retain 530m3 (500 min heel plus 30m3 travel fuel) and 40m3 was used to get here // at next
		 * load, load back up to full (500 min heel minus 10m3 idle fuel was on board) // at next discharge, retain 515m3 (500 min heel plus 15m3 travel fuel) and 40m3 was used to get here
		 * checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 10000, -9430, 9510, -9445 });
		 * 
		 * checker.baseFuelPricePerM3 = 100; checker.setupOrdinaryFuelCosts();
		 * 
		 * final Sequence sequence = schedule.getSequences().get(0); checker.check(sequence);
		 */
	}

	@Test
	public void testCharterOutRollover() {
		System.err.println("\n\nTest LNG heel from early charter out event is rolled over.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// add a charter out event prior to the first cargo.
		final LocalDateTime startLoad = msc.getFirstAppointment().getSecond().withZoneSameInstant(ZoneId.of(msc.originPort.getTimeZone())).toLocalDateTime();
		final LocalDateTime charterStartByDate = startLoad.minusHours(25);
		final LocalDateTime charterStartAfterDate = startLoad.minusHours(25);
		final int charterOutRate = 24;
		final CharterOutEvent event = msc.vesselEventCreator.createCharterOutEvent("CharterOut", msc.originPort, msc.originPort, charterStartByDate, charterStartAfterDate, charterOutRate);
		event.setVesselAssignmentType(msc.vesselAvailability);
		// set the charter out required end heel to 5000 (and set some other things)
		event.getHeelOptions().setVolumeAvailable(5000);
		event.getHeelOptions().setCvValue(21);
		event.getHeelOptions().setPricePerMMBTU(1);

		// recalculate the vessel availability based on the new timetable
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		// we now expect an idle and a vessel event visit before the first journey
		final Class<?>[] expectedClasses = { StartEvent.class, Idle.class, VesselEventVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class,
				Journey.class, Idle.class, EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);

		// expected charter out duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected charter out revenue
		// 24 { revenue per day } * 1 { days }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { -24 });

		// new idle event at the start of the itinerary
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 0, 2, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 0, 10, 0 });

		// first journey runs on NBO and BF
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// expected load volume reduced due to roll over from LNG left at end of charter out
		// 5010 = 10000 [vessel capacity] - (5000 [leftover heel] - 10 [journey boiloff])
		checker.setExpectedValue(5010, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);

		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testRegularCharterOutLoadToLoadNoHeel() {
		System.err.println("\n\nTest regular charter out from load port to load port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		@SuppressWarnings("unused")
		final CharterOutEvent event = msc.makeCharterOut(msc, scenario, msc.loadPort, msc.loadPort);
		event.setVesselAssignmentType(msc.vesselAvailability);
		
		final SequenceTester checker = getTesterForVesselEventPostDischarge();
		// SequenceTester checker = getDefaultTester();

		// expected charter out duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected charter out revenue
		// 24 { revenue per day } * 1 { days }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { -24 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testRegularCharterOutLoadToLoadWithHeel() {
		System.err.println("\n\nTest regular charter out from load port to load port. LNG heel should be used for return journey");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final CharterOutEvent event = msc.makeCharterOut(msc, scenario, msc.loadPort, msc.loadPort);
		event.setVesselAssignmentType(msc.vesselAvailability);

		event.getHeelOptions().setCvValue(21);
		event.getHeelOptions().setPricePerMMBTU(1);
		event.getHeelOptions().setVolumeAvailable(40);

		final SequenceTester checker = getTesterForVesselEventPostDischarge();

		// expected charter out duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected charter out revenue
		// 24 { revenue per day } * 1 { days }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { -24 });

		// final journey should use NBO
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 3);

		// final journey should use less base fuel
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 3);

		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

}
