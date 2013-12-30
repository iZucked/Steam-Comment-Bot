package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
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
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

public class CapacityViolationsCalculationsTest extends
		ShippingCalculationsTest {

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
	
		checker.setupOrdinaryFuelCosts();
	
		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	public void testPrevHeelAndMinLoadGreaterThanVesselCapacity() {
	
		Assert.fail("Not yet implemented");
	
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();
	
		msc.createDefaultCargo(msc.loadPort, msc.dischargePort);
		msc.setDefaultAvailability(msc.originPort, msc.originPort);
	
		msc.vc.setMinHeel(0);
	
		Slot loadSlot = msc.cargo.getSlots().get(0);
		Slot dischargeSlot = msc.cargo.getSlots().get(1);
	
		loadSlot.setMinQuantity(2000);
		dischargeSlot.setMaxQuantity(2000);
	
		msc.vessel.setCapacity(1000);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, // start to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to load
				SlotVisit.class, Journey.class, Idle.class, // load to discharge
				SlotVisit.class, Journey.class, Idle.class, // discharge to end
				EndEvent.class };
	
		SequenceTester checker = getDefaultTester(classes);
	
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 });
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 30, 10, 15 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0, 20, 0 });
	
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 });
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 10, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 10, 0 });
	
		// volume allocations: load 1000 at first load port (violate min load by 1000)
		// at first discharge, discharge 970 (30m3 was used to get here) emptying vessel
		// at next load, load back up to full (1000) (violate min load by 1000)
		// at next discharge, discharge fully; 30m3 was used to get here
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 1000, -970, 1000, -970 });
	
		checker.setExpectedValue(1000, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 0);
		checker.setExpectedValue(1000, Expectations.MIN_LOAD_VIOLATIONS, SlotVisit.class, 2);
	
		checker.setupOrdinaryFuelCosts();
	
		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
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
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);
	}

	@Test
	public void testVoyageRequirementsIncMinHeelViolateMaxLoad() {
		System.err.println("\n\nMaximum Load Volume Violated To Accommodate Min Heel");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();
	
		msc.vc.setMinHeel(50);
	
		msc.cargo.getSlots().get(0).setMaxQuantity(20);
	
		SequenceTester checker = getDefaultTester();
	
		// heel (50) + travel (20)  -- idle 10 comes out of heel.
		// Remaining heel can be discharged
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 70, -40 });
	
		checker.setExpectedValue(50, Expectations.MAX_LOAD_VIOLATIONS, SlotVisit.class, 0);
	
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
		((LoadSlot)cargo2.getSlots().get(0)).setArriveCold(true);
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
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);
	}

	@Test
	public void testMaxLoadIncPreviousHeelViolateMinDischarge() {
		Assert.fail("Not yet implemented");
	
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
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);
	}

	@Test
	public void testMaxDischargeAndPrevHeelViolateMinLoad() {
	
		Assert.fail("Not Yet Implemented");
	
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
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);
	}

	@Test
	public void testPrevHeelViolateMinLoadAndMaxDischarge() {
	
		Assert.fail("Not Yet Implemented - heel tracking should cause rollover");
	
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
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);
	
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);
	}

}
