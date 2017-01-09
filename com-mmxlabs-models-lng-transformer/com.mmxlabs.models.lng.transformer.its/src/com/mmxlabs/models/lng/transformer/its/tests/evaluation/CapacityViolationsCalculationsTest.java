/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.models.lng.types.PortCapability;

@RunWith(value = ShiroRunner.class)
public class CapacityViolationsCalculationsTest extends AbstractShippingCalculationsTestClass {

	@Test
	public void testMinLoadGreaterThanVesselCapacity() {

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMinQuantity(2000);
		dischargeSlot.setMaxQuantity(2000);

		msc.vessel.setCapacity(1000);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -970 });

		checker.setExpectedValue(1000, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 0);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	public void testEndHeelGreaterThanVesselCapacity() {

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);
		msc.vesselAvailability.getEndHeel().setTargetEndHeel(2000);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMaxQuantity(2000);
		dischargeSlot.setMaxQuantity(2000);

		msc.vessel.setCapacity(1000);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -970 });

		// End heel in m3 - Unable to meet end heel level
		checker.setExpectedValue(2000, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	// This currently triggers an assertion. Run tests with -ea
	@Test()
	public void testVoyageRequirementsGreaterThanVesselCapacity() {

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.cargo.getSlots().get(0).setMaxQuantity(1000);

		msc.vc.setMinHeel(0);

		msc.vessel.setCapacity(10);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 30, -0 });

		checker.setExpectedValue(20, Expectations.VESSEL_CAPACITY_VIOLATIONS, SlotVisit.class, 0);

		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	public void testVoyageRequirementsViolateMaxLoad() {
		System.err.println("\n\nMaximum Load Volume Violated due to voyage requirements");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		// change from default scenario: add a maximum load volume
		msc.cargo.getSlots().get(0).setMaxQuantity(20);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 30, -0 });

		checker.setExpectedValue(10, Expectations.MAX_LOAD_VIOLATIONS, SlotVisit.class, 0);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testVoyageRequirementsViolateMinDischarge() {
		System.err.println("\n\nMin discharge violated due to fuel constraints.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		msc.cargo.getSlots().get(0).setMaxQuantity(500);
		msc.cargo.getSlots().get(1).setMinQuantity(500);
		msc.vessel.setCapacity(1000);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 500, -470 });

		checker.setExpectedValue(30, Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 1);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Ignore("Attempting to create a case where min heel is kept on ballast leg thus causing the min discharge violation")
	@Test
	public void testVoyageRequirementsIncMinHeelViolateMinDischarge() {
		System.err.println("\n\nMin discharge violated due to fuel constraints inc min heel.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// Second cargo to force heel retention
		Cargo cargo2 = msc.createDefaultCargo(msc.loadPort, msc.dischargePort);
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		msc.vc.setMinHeel(0);

		msc.cargo.getSlots().get(0).setMaxQuantity(500);
		msc.cargo.getSlots().get(1).setMinQuantity(500);
		//
		((LoadSlot) cargo2.getSlots().get(0)).setArriveCold(true);
		cargo2.getSlots().get(0).setMaxQuantity(500);
		cargo2.getSlots().get(1).setMinQuantity(500);

		msc.vessel.setCapacity(1000);

		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, // start to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to end
				EndEvent.class };

		SequenceTester checker = getDefaultTester(classes);

		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 });
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 15, 10, 15 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 20, 20, 0 });

		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 });
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 10, 10, 0 });

		// First cargo expect min heel to be retained, second cargo can discharge
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 500, -420, 500, -470 });

		// discharge will be short by 30m3 (the fuel expenditure after loading)
		checker.setExpectedValue(80, Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 1);
		checker.setExpectedValue(30, Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 3);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testVesselCapacityViolateMinDischarge() {
		System.err.println("\n\nMin discharge violated due to max load constraint.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMaxQuantity(2000);
		dischargeSlot.setMinQuantity(1000);
		msc.vessel.setCapacity(1000);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -970 });

		// discharge will be short by 30m3 (the fuel expenditure after loading)
		checker.setExpectedValue(30, Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 1);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxLoadViolateMinDischarge() {
		System.err.println("\n\nMin discharge violated due to max load constraint.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMaxQuantity(1000);
		dischargeSlot.setMinQuantity(1000);
		msc.vessel.setCapacity(2000);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -970 });

		// discharge will be short by 30m3 (the fuel expenditure after loading)
		checker.setExpectedValue(30, Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 1);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxLoadIncPreviousHeelViolateMinDischarge() {
		System.err.println("\n\nMin load violated due to max discharge constraint.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.getStartHeel().setCvValue(21);
		msc.vesselAvailability.getStartHeel().setPricePerMMBTU(1);
		int startHeelInM3 = 500;
		msc.vesselAvailability.getStartHeel().setVolumeAvailable(startHeelInM3);

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMaxQuantity(1000);
		int minDischargeVolumeInM3 = 2000;
		dischargeSlot.setMinQuantity(minDischargeVolumeInM3);

		SequenceTester checker = getDefaultTester();

		// expected base consumptions
		// 5 = 1 { journey duration } * 15 { base fuel consumption } - 10 { LNG consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Journey.class, new Integer[] { 5, 10, 15 });

		// expected NBO consumptions of journeys
		// 10 = 1 { voyage duration } * 5 { NBO rate }
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 = (vessel empty)
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Journey.class, new Integer[] { 10, 20, 0 });

		// expected costs of journeys
		// 260 = 10 { base fuel unit cost } * 5 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 10 { LNG consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 260, 520, 150 });

		int expectedInitialVoyageBoilOffInM3 = 10;
		int expectedCargoBoilOffInM3 = 30;
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -(1500 - expectedCargoBoilOffInM3 - expectedInitialVoyageBoilOffInM3) });

		// load will be short by 500m3 + boil-off
		checker.setExpectedValue(2000 - (1500 - expectedCargoBoilOffInM3 - expectedInitialVoyageBoilOffInM3), Expectations.MIN_DISCHARGE_VIOLATIONS, SlotVisit.class, 1);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxDischargeViolateMinLoad() {
		System.err.println("\n\nMin load violated due to max discharge constraint.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMinQuantity(1060);
		dischargeSlot.setMaxQuantity(1000);

		SequenceTester checker = getDefaultTester();

		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1030, -1000 });

		// load will be short by 30m3 (the difference between discharge + 30m3 travel and min load)
		checker.setExpectedValue(30, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 0);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxDischargeAndPrevHeelViolateMinLoad() {

		System.err.println("\n\nMin load violated due to max discharge constraint.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.getStartHeel().setCvValue(21);
		msc.vesselAvailability.getStartHeel().setPricePerMMBTU(1);
		int startHeelInM3 = 500;
		msc.vesselAvailability.getStartHeel().setVolumeAvailable(startHeelInM3);

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMinQuantity(1060);
		int maxDischargeVolumeInM3 = 1000;
		dischargeSlot.setMaxQuantity(maxDischargeVolumeInM3);

		SequenceTester checker = getDefaultTester();

		// expected base consumptions
		// 5 = 1 { journey duration } * 15 { base fuel consumption } - 10 { LNG consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Journey.class, new Integer[] { 5, 10, 15 });

		// expected NBO consumptions of journeys
		// 10 = 1 { voyage duration } * 5 { NBO rate }
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 = (vessel empty)
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Journey.class, new Integer[] { 10, 20, 0 });

		// expected costs of journeys
		// 260 = 10 { base fuel unit cost } * 5 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 10 { LNG consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 260, 520, 150 });

		int expectedInitialVoyageBoilOffInM3 = 10;
		int expectedCargoBoilOffInM3 = 30;
		// Should be 540m3
		int expectedLoadVolumeInM3 = maxDischargeVolumeInM3 - (startHeelInM3 - expectedInitialVoyageBoilOffInM3) + expectedCargoBoilOffInM3;
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { expectedLoadVolumeInM3, -maxDischargeVolumeInM3 });

		// load will be short by 520m3
		checker.setExpectedValue(1060 - expectedLoadVolumeInM3, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 0);

		// Min heel in m3
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testPrevHeelViolateMinLoadAndLostHeel() {

		System.err.println("\n\nPrevious heel is larger than the max discharge causing roll over.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.getStartHeel().setCvValue(21);
		msc.vesselAvailability.getStartHeel().setPricePerMMBTU(1);
		int startHeelInM3 = 500;
		msc.vesselAvailability.getStartHeel().setVolumeAvailable(startHeelInM3);

		msc.vc.setMinHeel(0);

		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);

		loadSlot.setMinQuantity(1060);
		int maxDischargeVolumeInM3 = 400;
		dischargeSlot.setMaxQuantity(maxDischargeVolumeInM3);

		SequenceTester checker = getDefaultTester();

		// expected base consumptions
		// 5 = 1 { journey duration } * 15 { base fuel consumption } - 10 { LNG consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Journey.class, new Integer[] { 5, 10, 15 });

		// expected NBO consumptions of journeys
		// 10 = 1 { voyage duration } * 5 { NBO rate }
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 = (vessel empty)
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Journey.class, new Integer[] { 10, 20, 0 });

		// expected costs of journeys
		// 260 = 10 { base fuel unit cost } * 5 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 10 { LNG consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 260, 520, 150 });

		int expectedInitialVoyageBoilOffInM3 = 10;
		int expectedCargoBoilOffInM3 = 30;
		int expectedLoadVolumeInM3 = 0;
		int expectedCargoStartingHeelInM3 = startHeelInM3 - expectedInitialVoyageBoilOffInM3;
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { expectedLoadVolumeInM3, -maxDischargeVolumeInM3 });

		// load will be short by 520m3
		checker.setExpectedValue(1060 - expectedLoadVolumeInM3, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testBOGCausesMaxLoadViolation() {
		System.err.println("\n\nMaximum Load Volume Forces BF Idle");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a maximum load volume
		msc.cargo.getSlots().get(0).setMaxQuantity(20);

		final SequenceTester checker = getDefaultTester();

		// change from default: no NBO consumption (min heel forces BF travel except on laden leg where it is required)
		final Integer[] expectedNboJourneyConsumptions = { 0, 20, 0 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, expectedNboJourneyConsumptions);

		// change from default: mostly BF consumption (min heel forces BF travel)
		final Integer[] expectedBaseFuelJourneyConsumptions = { 15, 10, 15 };
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, expectedBaseFuelJourneyConsumptions);

		// DISCUSS:
		final Integer[] expectedNboIdleConsumptions = { 0, 10, 0 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, expectedNboIdleConsumptions);

		// expected costs of journeys
		final Integer[] expectedJourneyCosts = { 150, 520, 150 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		// Need to load 30 for min NBO and discharge nothing as we have breached our load vol
		final Integer[] expectedloadDischargeVolumes = { 30, 0 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		checker.setExpectedValue(10, Expectations.MAX_LOAD_VIOLATIONS, SlotVisit.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testRegularCharterOutLoadToOrigin_WithEndHeel_NoViolation() {
		System.err.println("\n\nTest regular charter out from load port to origin port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		CharterOutEvent event = msc.makeCharterOut(msc, scenario, msc.loadPort, msc.originPort);
		event.getHeelOptions().setVolumeAvailable(2000);

		event.setVesselAssignmentType(msc.vesselAvailability);

		// FIXME: Note - there are three idle events in a row due to the way the internal optimisation represents the transition from charter start to charter end. Not great API but this is the way it
		// works.
		Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, Idle.class, Idle.class,
				VesselEventVisit.class, Idle.class, EndEvent.class };
		SequenceTester checker = getDefaultTester(classes);

		// Set a target end heel
		msc.vesselAvailability.getEndHeel().setTargetEndHeel(2000);

		// expected durations of journeys
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 30 = 2 { journey duration } * 15 { base fuel consumption } - 0 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 30 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 300 = 10 { base fuel unit cost } * 30 { base fuel consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 150, 520, 300 });

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0, 0, 0, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 0, 0, 0 });

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0, 0, 0, 0 });

		// expected charter out duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected charter out revenue
		// 24 { revenue per day } * 1 { days }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { -24 });

		// End heel in m3 - Unable to meet end heel level
		checker.setExpectedValue(0, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testRegularCharterOutLoadToOrigin_WithEndHeel_Violation() {
		System.err.println("\n\nTest regular charter out from load port to origin port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		CharterOutEvent event = msc.makeCharterOut(msc, scenario, msc.loadPort, msc.originPort);
		event.getHeelOptions().setVolumeAvailable(1000);

		event.setVesselAssignmentType(msc.vesselAvailability);

		// FIXME: Note - there are three idle events in a row due to the way the internal optimisation represents the transition from charter start to charter end. Not great API but this is the way it
		// works.
		Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, Idle.class, Idle.class,
				VesselEventVisit.class, Idle.class, EndEvent.class };
		SequenceTester checker = getDefaultTester(classes);

		// Set a target end heel
		msc.vesselAvailability.getEndHeel().setTargetEndHeel(2000);

		// expected durations of journeys
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 30 = 2 { journey duration } * 15 { base fuel consumption } - 0 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 30 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 300 = 10 { base fuel unit cost } * 30 { base fuel consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 150, 520, 300 });

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0, 0, 0, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 0, 0, 0 });

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0, 0, 0, 0 });

		// expected charter out duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected charter out revenue
		// 24 { revenue per day } * 1 { days }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { -24 });

		// End heel in m3 - Unable to meet end heel level
		// Current check means if anything is left, then no violation.
		// checker.setExpectedValue(1000, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testDryDock() {
		System.err.println("\n\nDry dock event inserted correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();
		msc.setupCooldown(30);

		// Set a target end heel
		msc.vesselAvailability.getEndHeel().setTargetEndHeel(2000);

		// change to default: add a dry dock event 2-3 hrs after discharge window ends
		final LocalDateTime endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();
		final LocalDateTime dryDockStartByDate = endLoad.plusHours(3);
		final LocalDateTime dryDockStartAfterDate = endLoad.plusHours(2);
		DryDockEvent event = msc.vesselEventCreator.createDryDockEvent("DryDock", msc.loadPort, dryDockStartByDate, dryDockStartAfterDate);
		event.setVesselAssignmentType(msc.vesselAvailability);

		// set up a drydock pricing of 6
		msc.portCreator.setPortCost(msc.loadPort, PortCapability.DRYDOCK, 6);

		SequenceTester checker = getTesterForVesselEventPostDischargeWithEndCooldown();

		// expected dry dock duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected dry dock port cost
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { 6 });

		// End heel in m3 - Unable to meet end heel level
		checker.setExpectedValue(2000, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

}
