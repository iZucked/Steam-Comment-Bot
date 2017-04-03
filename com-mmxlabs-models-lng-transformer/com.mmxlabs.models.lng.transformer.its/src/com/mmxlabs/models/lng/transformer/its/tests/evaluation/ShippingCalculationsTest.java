/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.LddScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.StsScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;

@RunWith(value = ShiroRunner.class)
public class ShippingCalculationsTest extends AbstractShippingCalculationsTestClass {

	/*
	 * We need to create a barebones scenario with a single vessel schedule. Then the scenario needs to be evaluated to test correct calculation of: - Fuel costs - Port costs - Route costs - NBO rates
	 */

	@Test
	public void testCanalRouteShorter() {
		System.err.println("\n\nUse canal which is cheaper than default route");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal

		final Route canal = msc.portCreator.addCanal(RouteOption.SUEZ);
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, canal);
		msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, canal);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// second journey is now half as long due to canal usage
		// so fuel usage is halved

		// use half as much fuel on second journey (as default)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 1);

		// use half as much fuel on second journey (as default)
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 1);

		// second journey costs half as much (as default)
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 1);

		// second journey faces additional canal cost
		checker.setExpectedValue(1, Expectations.OVERHEAD_COSTS, Journey.class, 1);

		// second journey takes half as long (as default)
		checker.setExpectedValue(1, Expectations.DURATIONS, Journey.class, 1);

		// so second idle is 1 longer
		checker.setExpectedValue(3, Expectations.DURATIONS, Idle.class, 1);

		// and correspondingly costs more
		checker.setExpectedValue(315, Expectations.FUEL_COSTS, Idle.class, 1);

		// and requires more fuel
		checker.setExpectedValue(15, Expectations.NBO_USAGE, Idle.class, 1);

		// idle uses 15 NBO, journey uses 10
		final Integer[] expectedloadDischargeVolumes = { 10000, -9975 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testSuezCanalRouteLonger() {
		System.err.println("\n\nDon't use canal which is longer than default route");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal, but it is longer than the default route

		final Route canal = msc.portCreator.addCanal(RouteOption.SUEZ);
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 30, canal);
		msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, canal);

		final SequenceTester checker = getDefaultTester();

		// no change from default scenario: canal route should be ignored

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.setExpectedValue(0, Expectations.OVERHEAD_COSTS, Journey.class, 1);

		checker.check(sequence);

	}

	@Test
	public void testCanalRouteTooExpensive() {
		System.err.println("\n\nDon't use canal which is has a high cost associated with it");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal,
		// which is shorter than the default route
		// but has a high usage cost

		final Route canal = msc.portCreator.addCanal(RouteOption.SUEZ);
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, canal);
		msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, canal);
		final RouteCost cost = msc.getRouteCost(msc.vc, canal);
		cost.setLadenCost(500);

		final SequenceTester checker = getDefaultTester();

		// no change from default scenario: canal route should be ignored

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.setExpectedValue(0, Expectations.OVERHEAD_COSTS, Journey.class, 1);

		checker.check(sequence);

	}

	@Test
	public void testSuezCanalRouteShorterWithDelay() {
		System.err.println("\n\nUse Suez canal which is cheaper than default route but has a delay");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal

		{
			final Route suezCanal = msc.portCreator.addCanal(RouteOption.SUEZ);
			msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, suezCanal);
			msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, suezCanal);
			final VesselClassRouteParameters routeParameters = msc.getRouteParameters(msc.vc, suezCanal);

			routeParameters.setExtraTransitTime(2);
			routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
			routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));
		}

		{
			final Route panamaCanal = msc.portCreator.addCanal(RouteOption.PANAMA);
			msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 20, panamaCanal);
			msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, panamaCanal);
			final VesselClassRouteParameters routeParameters = msc.getRouteParameters(msc.vc, panamaCanal);

			routeParameters.setExtraTransitTime(2);
			routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
			routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));
		}
		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// second journey is now 1 hr longer due to canal usage (but 10 units shorter distance)
		// so fuel usage is halved, plus extra from canal

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(12, Expectations.NBO_USAGE, Journey.class, 1);

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(7, Expectations.BF_USAGE, Journey.class, 1);

		// second journey cost is different
		// 322 = 7 { BF usage } * 10 { BF price } + 12 { NBO usage } * 21 { NBO price }
		checker.setExpectedValue(322, Expectations.FUEL_COSTS, Journey.class, 1);

		// second journey faces additional canal cost
		checker.setExpectedValue(1, Expectations.OVERHEAD_COSTS, Journey.class, 1);

		// second journey takes 3hrs (instead of 2)
		checker.setExpectedValue(3, Expectations.DURATIONS, Journey.class, 1);

		// so second idle is 1hr less
		checker.setExpectedValue(1, Expectations.DURATIONS, Idle.class, 1);

		// and correspondingly costs less
		checker.setExpectedValue(105, Expectations.FUEL_COSTS, Idle.class, 1);

		// and requires less fuel
		checker.setExpectedValue(5, Expectations.NBO_USAGE, Idle.class, 1);

		// idle NBO consumption is 5, plus 12 for journey
		final Integer[] expectedloadDischargeVolumes = { 10000, -9983 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testPanamaCanalRouteShorterWithDelay() {
		System.err.println("\n\nUse panama canal which is cheaper than default route but has a delay");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal
		//
		{
			final Route suezCanal = msc.portCreator.addCanal(RouteOption.SUEZ);
			msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 30, suezCanal);
			msc.fleetCreator.assignDefaultSuezCanalData(msc.vc, suezCanal);
			final VesselClassRouteParameters routeParameters = msc.getRouteParameters(msc.vc, suezCanal);

			routeParameters.setExtraTransitTime(2);
			routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
			routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));
		}

		{
			final Route panamaCanal = msc.portCreator.addCanal(RouteOption.PANAMA);
			msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, panamaCanal);
			msc.fleetCreator.assignDefaultPanamaCanalData(msc.vc, panamaCanal);
			final VesselClassRouteParameters routeParameters = msc.getRouteParameters(msc.vc, panamaCanal);

			routeParameters.setExtraTransitTime(2);
			routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
			routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));
		}

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// second journey is now 1 hr longer due to canal usage (but 10 units shorter distance)
		// so fuel usage is halved, plus extra from canal

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(12, Expectations.NBO_USAGE, Journey.class, 1);

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(7, Expectations.BF_USAGE, Journey.class, 1);

		// second journey cost is different
		// 322 = 7 { BF usage } * 10 { BF price } + 12 { NBO usage } * 21 { NBO price }
		checker.setExpectedValue(322, Expectations.FUEL_COSTS, Journey.class, 1);

		// second journey faces additional canal cost
		checker.setExpectedValue(1, Expectations.OVERHEAD_COSTS, Journey.class, 1);

		// second journey takes 3hrs (instead of 2)
		checker.setExpectedValue(3, Expectations.DURATIONS, Journey.class, 1);

		// so second idle is 1hr less
		checker.setExpectedValue(1, Expectations.DURATIONS, Idle.class, 1);

		// and correspondingly costs less
		checker.setExpectedValue(105, Expectations.FUEL_COSTS, Idle.class, 1);

		// and requires less fuel
		checker.setExpectedValue(5, Expectations.NBO_USAGE, Idle.class, 1);

		// idle NBO consumption is 5, plus 12 for journey
		final Integer[] expectedloadDischargeVolumes = { 10000, -9983 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		Assert.assertEquals(RouteOption.DIRECT, ((Journey) sequence.getEvents().get(1)).getRoute().getRouteOption());
		Assert.assertEquals(RouteOption.PANAMA, ((Journey) sequence.getEvents().get(4)).getRoute().getRouteOption());
		Assert.assertEquals(RouteOption.DIRECT, ((Journey) sequence.getEvents().get(7)).getRoute().getRouteOption());

		checker.check(sequence);

	}

	@Test
	public void testFBOLimitedByMinHeel() {
		System.err.println("\n\nUse FBO for one trip after loading");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario
		final CostModel costModel = scenario.getReferenceModel().getCostModel();

		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);
		// base fuel is now 10x more expensive, so FBO is economical

		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// but the vessel's capacity is only 50m3 greater than its minimum heel
		// and the journeys (after loading) use a total of 40m3 NBO
		// so only 10m3 is available for FBO, which is not enough for both journeys
		msc.vc.setCapacity(60);
		msc.vc.setMinHeel(10);

		// Create second cargo to require arriving cold
		Cargo secondCargo = msc.createDefaultCargo(msc.loadPort, msc.dischargePort);

		// and send the vessel back to the origin port at end of itinerary
		VesselAvailability va = msc.setDefaultAvailability(msc.originPort, msc.originPort);
		secondCargo.setVesselAssignmentType(va);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		final List<Journey> journeys = extractObjectsOfClass(sequence.getEvents(), Journey.class);

		// in this scenario, there should be FBO used on one or other of the journey legs after loading
		// but not both

		int fboUsages = 0;
		for (int i = 1; i < 3; i++) {
			if (getFuelConsumption(journeys.get(i), Fuel.FBO) > 0) {
				fboUsages += 1;
			}
		}
		Assert.assertEquals("Exactly one leg uses FBO", 1, fboUsages);
	}

	/*
	 * We need to create a barebones scenario with a single vessel schedule. Then the scenario needs to be evaluated to test correct calculation of: - Fuel costs - Port costs - Route costs - NBO rates
	 */

	@Test
	public void testBasicScenario() {
		System.err.println("\n\nBasic Scenario");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final SequenceTester checker = getDefaultTester();

		/*
		 * evaluate and get a schedule note: this involves - initialising a transformer using a TransformerExtensionTest module - transforming the scenario and running an optimiser on the transformed
		 * data - using additional indirection to inject members into an optimiser exporter
		 */

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
		/*
		 * // extract the three journeys List<Journey> journeys = extractObjectsOfClass(events, Journey.class); Journey originToLoad = journeys.get(0); Journey loadToDischarge = journeys.get(1);
		 * Journey dischargeToOrigin = journeys.get(2);
		 * 
		 * // check they go between the right places and have the right distances msc.checkJourneyGeography(originToLoad, msc.originPort, msc.loadPort); msc.checkJourneyGeography(loadToDischarge,
		 * msc.loadPort, msc.dischargePort); msc.checkJourneyGeography(dischargeToOrigin, msc.dischargePort, msc.originPort);
		 */

	}

	@Test
	public void testFBODesirable() {
		System.err.println("\n\nUse FBO for both trips after loading");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario
		final CostModel costModel = scenario.getReferenceModel().getCostModel();

		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		final SequenceTester checker = getDefaultTester();
		checker.baseFuelPricePerMT = 100;

		// change from default scenario
		// second and third journeys now use LNG only (no start heel means that first journey has to be on base fuel only)
		final Integer[] expectedFboJourneyConsumptions = { 0, 10, 5 };
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, expectedFboJourneyConsumptions);

		// second and third journeys now use LNG only
		final Integer[] expectedNboJourneyConsumptions = { 0, 20, 10 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, expectedNboJourneyConsumptions);

		// second and third journeys now use LNG only
		final Integer[] expectedBaseFuelJourneyConsumptions = { 15, 0, 0 };
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, expectedBaseFuelJourneyConsumptions);

		// first journey costs 10x as much, other journeys change costing too
		final Integer[] expectedJourneyCosts = { 1500, 630, 315 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		// idle LNG consumption is 10, plus 30 + 15 for journeys and no safety heel retained
		final Integer[] expectedloadDischargeVolumes = { 10000, -9945 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxLoadVolume() {
		System.err.println("\n\nMaximum Load Volume Limits Load & Discharge");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a maximum load volume
		msc.cargo.getSlots().get(0).setMaxQuantity(500);
		// no minimum heel
		msc.vc.setMinHeel(0);

		final SequenceTester checker = getDefaultTester();

		// expected load / discharge volumes:
		// 500 (load) = { new maximum load value }
		// 470 (discharge) = 500 { load } - 30 { consumption }
		final Integer[] expectedloadDischargeVolumes = { 500, -470 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testMaxLoadVolumeMMBTU() {
		System.err.println("\n\nMaximum Load Volume Limits Load & Discharge");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a maximum load volume
		LoadSlot loadSlot = (LoadSlot) msc.cargo.getSlots().get(0);
		loadSlot.setMaxQuantity(10500);
		loadSlot.setCargoCV(21);
		loadSlot.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		// no minimum heel
		msc.vc.setMinHeel(0);

		final SequenceTester checker = getDefaultTester();

		// expected load / discharge volumes:
		// 500 (load) = { new maximum load value }
		// 470 (discharge) = 500 { load } - 30 { consumption }
		final Integer[] expectedloadDischargeVolumes = { 500, -470 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testMaxDischargeVolumeMMBTu() {
		System.err.println("\n\nMaximum Discharge Volume Limits Load & Discharge");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a maximum load volume
		DischargeSlot dischargeSlot = (DischargeSlot) msc.cargo.getSlots().get(1);
		dischargeSlot.setMaxQuantity(10500);
		dischargeSlot.setVolumeLimitsUnit(VolumeUnits.MMBTU);

		final SequenceTester checker = getDefaultTester();

		// expected load / discharge volumes
		// 530 (load) = 500 { discharge } + 30 { consumption }
		// 500 (discharge) =
		final Integer[] expectedloadDischargeVolumes = { 530, -500 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testMaxDischargeVolume() {
		System.err.println("\n\nMaximum Discharge Volume Limits Load & Discharge");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a maximum load volume
		msc.cargo.getSlots().get(1).setMaxQuantity(500);

		final SequenceTester checker = getDefaultTester();

		// expected load / discharge volumes
		// 530 (load) = 500 { discharge } + 30 { consumption }
		// 500 (discharge) =
		final Integer[] expectedloadDischargeVolumes = { 530, -500 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testMinDischargeVolumeMMBTu() {
		System.err.println("\n\nMinimum Discharge Volume Prevents FBO");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: base fuel price more expensive, so FBO is economical
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// but minimum discharge volume means that it causes a capacity violation
		DischargeSlot dischargeSlot = (DischargeSlot) msc.cargo.getSlots().get(1);
		msc.cargo.getSlots().get(1).setMinQuantity(209265);
		dischargeSlot.setVolumeLimitsUnit(VolumeUnits.MMBTU);

		// for the moment, set min heel to zero since it causes problems in the volume calculations
		msc.vc.setMinHeel(0);

		final SequenceTester checker = getDefaultTester();
		checker.baseFuelPricePerMT = 100;

		final Integer[] expectedloadDischargeVolumes = { 10000, -9970 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		// first & last journeys cost 10x as much
		final Integer[] expectedJourneyCosts = { 1500, 1420, 1500 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMinDischargeVolume() {
		System.err.println("\n\nMinimum Discharge Volume Prevents FBO");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: base fuel price more expensive, so FBO is economical
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		final BaseFuelCost fuelPrice = costModel.getBaseFuelCosts().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// but minimum discharge volume means that it causes a capacity violation
		msc.cargo.getSlots().get(1).setMinQuantity(9965);

		// for the moment, set min heel to zero since it causes problems in the volume calculations
		msc.vc.setMinHeel(0);

		final SequenceTester checker = getDefaultTester();
		checker.baseFuelPricePerMT = 100;

		final Integer[] expectedloadDischargeVolumes = { 10000, -9970 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		// first & last journeys cost 10x as much
		final Integer[] expectedJourneyCosts = { 1500, 1420, 1500 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testIdleAfterVesselReturn() {
		System.err.println("\n\nSpecified date for vessel return causes idling.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final LocalDateTime endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();

		// return 3 hrs after discharge window ends
		final LocalDateTime returnDate = endDischarge.plusHours(3);
		av.setEndAfter(returnDate);
		av.unsetEndBy();
		System.err.println("Vessel to return after: " + returnDate);

		final SequenceTester checker = getDefaultTester();

		// change from default: BF idle consumption at base port after return
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 2);
		// change from default: idle at base port after return
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 2);
		// change from default: idle cost at base port
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 2);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testIdleAfterVesselStart() {
		System.err.println("\n\nSpecified date for vessel start causes idling.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final LocalDateTime startLoad = msc.cargo.getSlots().get(0).getWindowStartWithSlotOrPortTime().toLocalDateTime();

		// start 3 hrs before load window begins
		final LocalDateTime startDate = startLoad.minusHours(3);
		av.setStartBy(startDate);
		av.setStartAfter(startDate.minusHours(5));
		System.err.println("Vessel to start before: " + startDate);

		final SequenceTester checker = getDefaultTester();

		// change from default: BF idle consumption at load port after arrival
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 0);
		// change from default: idle at base port after return
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 0);
		// change from default: idle cost at base port
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testIgnoreStartAfterAndEndBy() {
		System.err.println("\n\nNo effects of in-bounds values for vessel start-after and end-by");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final LocalDateTime startLoad = msc.cargo.getSlots().get(0).getWindowStartWithSlotOrPortTime().toLocalDateTime();
		final LocalDateTime endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();

		// start within 5 hrs before load window starts
		final LocalDateTime startDate = startLoad.minusHours(5);
		av.setStartAfter(startDate);
		System.err.println("Vessel to start after: " + startDate);

		// return within 5 hrs after discharge window ends
		final LocalDateTime returnDate = endDischarge.plusHours(5);
		av.setEndBy(returnDate);
		System.err.println("Vessel to return by: " + returnDate);

		// should have no effect on the generated schedule
		final SequenceTester checker = getDefaultTester();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testLongWarmupMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: cooldown times and volumes specified
		msc.vc.setWarmingTime(3);
		// msc.vc.setCoolingTime(1);
		msc.vc.setCoolingVolume(100);

		msc.setupCooldown(1.0);
		msc.loadPort.setAllowCooldown(true);

		final SequenceTester checker = getDefaultTester();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testCooldownAdded() {
		System.err.println("\n\nCooldown event should be scheduled at load port.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: cooldown times and volumes specified
		msc.vc.setWarmingTime(0);
		// msc.vc.setCoolingTime(0);
		msc.vc.setCoolingVolume(100);

		msc.setupCooldown(1.0);
		msc.loadPort.setAllowCooldown(true);

		// change from default scenario: should insert a cooldown event
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class,
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);
		// change from default: cooldown time
		final Integer[] expectedCooldownTimes = { 0 };
		checker.setExpectedValues(Expectations.DURATIONS, Cooldown.class, expectedCooldownTimes);

		// cooldown cost
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, Cooldown.class, new Integer[] { 2100 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);

		final Cooldown cooldown = extractObjectsOfClass(sequence.getEvents(), Cooldown.class).get(0);
		Assert.assertEquals("Cooldown cost", 2100, cooldown.getCost());

	}

	@Test
	public void testCooldownAddedLumpSum() {
		System.err.println("\n\nCooldown event should be scheduled at load port.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: cooldown times and volumes specified
		msc.vc.setWarmingTime(0);
		// msc.vc.setCoolingTime(0);
		msc.vc.setCoolingVolume(100);

		msc.setupCooldownLumpSum("2100");
		msc.loadPort.setAllowCooldown(true);

		// change from default scenario: should insert a cooldown event
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class,
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);
		// change from default: cooldown time
		final Integer[] expectedCooldownTimes = { 0 };
		checker.setExpectedValues(Expectations.DURATIONS, Cooldown.class, expectedCooldownTimes);

		// cooldown cost
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, Cooldown.class, new Integer[] { 2100 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);

		final Cooldown cooldown = extractObjectsOfClass(sequence.getEvents(), Cooldown.class).get(0);
		Assert.assertEquals("Cooldown cost", 2100, cooldown.getCost());

	}

	@Test
	public void testCharterCost_TimeCharter() {
		System.err.println("\n\nTime Charter vessel charter cost ignored.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final int charterRatePerDay = 240000;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		msc.vesselAvailability.setTimeCharterRate("" + charterRatePerDay);

		final SequenceTester checker = getDefaultTester();
		checker.hireCostPerHour = charterRatePerDay / 24;

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		// Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	@Test
	public void testCharterCost_SpotCharterIn() {

		System.err.println("\n\nSpot charter-in vessel charter cost added correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// Remove default vessel
		final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
		fleetModel.getVessels().clear();

		final CargoModel cargoModel = scenario.getCargoModel();
		cargoModel.getVesselAvailabilities().clear();

		cargoModel.getCargoes().forEach(c -> c.setVesselAssignmentType(null));
		// Cannot null as final
		// msc.vessel = null;
		// msc.vesselAvailability = null;

		// Set up a charter index curve
		final int charterRatePerDay = 240;

		// Create a charter-in market object
		final SpotMarketsModel sportMarketsModel = scenario.getReferenceModel().getSpotMarketsModel();
		final EList<CharterInMarket> charteringSpotMarkets = sportMarketsModel.getCharterInMarkets();

		final CharterInMarket charterModel = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charteringSpotMarkets.add(charterModel);
		charterModel.setName("market-" + msc.vc.getName());

		charterModel.setVesselClass(msc.vc);
		charterModel.setSpotCharterCount(1);
		charterModel.setCharterInRate("" + charterRatePerDay);

		// Spot charter-in vessels have fewer voyages
		final SequenceTester checker;
		{
			final Class<?>[] expectedClasses = { SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };
			checker = getTestCharterCost_SpotCharterInTester(expectedClasses);
		}

		checker.hireCostPerHour = charterRatePerDay / 24;

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		// Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	/**
	 * Create a different set of expectations to that of {@link #getDefaultTester()} to handle the spot charter-in in {@link #testCharterCost_SpotCharterIn()}. This vessel has no initial leg and
	 * returns to the load port rather than origin port - thus doubling the return leg time
	 * 
	 * @param expectedClasses
	 * @return
	 */
	@Override
	public SequenceTester getTestCharterCost_SpotCharterInTester(final Class<?>[] expectedClasses) {

		final SequenceTester checker = new SequenceTester(expectedClasses);

		// set default expected values to zero
		for (final Expectations field : Expectations.values()) {
			checker.setAllExpectedValues(field, 0);
		}

		// expected durations of journeys
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 2, 2 });

		// don't care what the duration of the end event is
		checker.setExpectedValues(Expectations.DURATIONS, EndEvent.class, new Integer[] { null });

		// don't care what the duration of the start event is
		checker.setExpectedValues(Expectations.DURATIONS, StartEvent.class, new Integer[] {});

		// expected FBO consumptions of journeys
		// none (not economical in default)
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 30 = 2 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 10, 30 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 300 = 10 { base fuel unit cost } * 30 { base fuel consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 520, 300 });

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 2, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 10, 0 });

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 210, 0 });

		// expected load / discharge volumes
		// 10000 (load) = vessel capacity
		// 9970 (discharge) = 10000 - 20 { NBO journey consumption } - 10 { NBO idle consumption }
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 10000, -9970 });

		return checker;
	}

	@Test
	public void testCharterCostUnset() {
		System.err.println("\n\nZero vessel charter cost added correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final int charterRatePerDay = 0;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		msc.vesselAvailability.setTimeCharterRate("" + charterRatePerDay);

		final SequenceTester checker = getDefaultTester();

		checker.hireCostPerHour = charterRatePerDay / 24;

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		// Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	@Test
	public void testVesselStartsAnywhere() {
		System.err.println("\n\nVessel starts anywhere.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.getStartAt().clear();

		// change from default scenario: vessel makes only two journeys
		final Class<?>[] expectedClasses = { StartEvent.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);
		// Missing the journey so shift default indices by one

		// expected durations of journeys
		final Integer[] expectedJourneyDurations = { 2, 1 };
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, expectedJourneyDurations);

		final Integer[] expectedIdleDurations = { 0, 2, 0 };
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, expectedIdleDurations);

		// expected FBO consumptions of journeys
		// none (not economical in default)
		final Integer[] expectedFboJourneyConsumptions = { 0, 0 };
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, expectedFboJourneyConsumptions);

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 =
		final Integer[] expectedNboJourneyConsumptions = { 20, 0 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, expectedNboJourneyConsumptions);

		final Integer[] expectedBaseFuelJourneyConsumptions = { 10, 15 };
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, expectedBaseFuelJourneyConsumptions);

		// expected costs of journeys
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		final Integer[] expectedJourneyCosts = { 520, 150 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testVesselEndsAnywhere() {
		System.err.println("\n\nVessel ends anywhere - travels back to load port for end.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.getEndAt().clear();

		final SequenceTester checker = getDefaultTester();

		// change from default: final journey is 2hrs
		checker.setExpectedValue(2, Expectations.DURATIONS, Journey.class, 2);
		// change from default: final journey consumes double the base fuel
		checker.setExpectedValue(30, Expectations.BF_USAGE, Journey.class, 2);
		// change from default: final journey costs double
		checker.setExpectedValue(300, Expectations.FUEL_COSTS, Journey.class, 2);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		// Fails with a missin g Journel event - understandable as there is no journey, but why does it now not appear?
		checker.check(sequence);

	}

	@Test
	public void testDryDock() {
		System.err.println("\n\nDry dock event inserted correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change to default: add a dry dock event 2-3 hrs after discharge window ends
		final LocalDateTime endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();
		final LocalDateTime dryDockStartByDate = endLoad.plusHours(3);
		final LocalDateTime dryDockStartAfterDate = endLoad.plusHours(2);
		DryDockEvent event = msc.vesselEventCreator.createDryDockEvent("DryDock", msc.loadPort, dryDockStartByDate, dryDockStartAfterDate);
		event.setVesselAssignmentType(msc.vesselAvailability);

		// set up a drydock pricing of 6
		msc.portCreator.setPortCost(msc.loadPort, PortCapability.DRYDOCK, 6);

		SequenceTester checker = getTesterForVesselEventPostDischarge();

		// expected dry dock duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected dry dock port cost
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { 6 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testMaintenance() {
		System.err.println("\n\nMaintenance event inserted correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change to default: add a dry dock event 2-3 hrs after discharge window ends
		final LocalDateTime endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();
		final LocalDateTime maintenanceDockStartByDate = endLoad.plusHours(3);
		final LocalDateTime maintenanceDockStartAfterDate = endLoad.plusHours(2);
		MaintenanceEvent event = msc.vesselEventCreator.createMaintenanceEvent("Maintenance", msc.loadPort, maintenanceDockStartByDate, maintenanceDockStartAfterDate);
		event.setVesselAssignmentType(msc.vesselAvailability);

		// set up a drydock pricing of 6
		msc.portCreator.setPortCost(msc.loadPort, PortCapability.MAINTENANCE, 3);

		SequenceTester checker = getTesterForVesselEventPostDischarge();

		// expected dry dock duration
		checker.setExpectedValues(Expectations.DURATIONS, VesselEventVisit.class, new Integer[] { 24 });

		// expected dry dock port cost
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, VesselEventVisit.class, new Integer[] { 3 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testFixedPortCosts() {
		System.err.println("\n\nTest fixed port costs are added to the itinerary cost appropriately.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		SequenceTester checker = getDefaultTester();

		int loadPortCost = 30;
		msc.pricingCreator.setPortCost(msc.loadPort, PortCapability.LOAD, loadPortCost);

		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, SlotVisit.class, new Integer[] { loadPortCost, 0 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testPortFuelCosts() {
		System.err.println("\n\nTest port fuel costs are added to the itinerary cost appropriately.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		SequenceTester checker = getDefaultTester();

		int ladenBaseConsumption = 72;
		int ballastBaseConsumption = 48;
		Integer[] portDurations = new Integer[] { 24, 48 };

		// set the in-port laden fuel consumption for the vessel class
		msc.vc.getLadenAttributes().setInPortBaseRate(ladenBaseConsumption);
		msc.vc.getBallastAttributes().setInPortBaseRate(ballastBaseConsumption);
		// set the durations of the load visit & discharge visit
		for (int i = 0; i < portDurations.length; i++) {
			msc.cargo.getSortedSlots().get(i).setDuration(portDurations[i]);
		}

		// change from default: base fuel usage at ports, and duration spent there
		checker.setExpectedValues(Expectations.BF_USAGE, SlotVisit.class, new Integer[] { ladenBaseConsumption * portDurations[0] / 24, portDurations[1] * ballastBaseConsumption / 24 });
		checker.setExpectedValues(Expectations.DURATIONS, SlotVisit.class, portDurations);

		// change from default: idle times at discharge port and end port are now zero (and fuel consumptions zero accordingly)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 0, 0 });
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 0, 0 });
		checker.setupOrdinaryFuelCosts();

		// less base fuel usage on idle means higher discharge at discharge port
		checker.setExpectedValue(-9980, Expectations.LOAD_DISCHARGE, SlotVisit.class, 1);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testStartDateChosenForLeastIdleTime() {
		System.err.println("\n\nStart time should be chosen to minimise idle time at first visit.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final Slot loadSlot = msc.cargo.getSlots().get(0);
		final LocalDateTime loadDate = loadSlot.getWindowStartWithSlotOrPortTime().toLocalDateTime();

		final double maxSpeed = msc.vc.getMaxSpeed();
		final int firstIdle = 1;
		Integer travelTime = msc.getTravelTime(msc.originPort, msc.loadPort, null, (int) maxSpeed);
		final LocalDateTime startAfterDate = loadDate.minusHours(5 * travelTime);
		final LocalDateTime startByDate = loadDate.minusHours(travelTime + firstIdle);

		msc.vesselAvailability.setStartAfter(startAfterDate);
		msc.vesselAvailability.setStartBy(startByDate);

		SequenceTester checker = getDefaultTester();

		// force idle time
		checker.setExpectedValue(firstIdle, Expectations.DURATIONS, Idle.class, 0);
		checker.setExpectedValue(5 * firstIdle, Expectations.BF_USAGE, Idle.class, 0);
		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testGeneratedCharterOut() {
		System.err.println("\n\nIdle at end should permit generated charter out event.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96, scenario.getReferenceModel().getPortModel().getPorts());

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final LocalDateTime endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();

		// return 37 hrs after discharge window ends
		final LocalDateTime returnDate = endDischarge.plusHours(37);
		av.setEndAfter(returnDate);
		av.unsetEndBy();
		System.err.println("Vessel to return after: " + returnDate);

		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Idle.class, GeneratedCharterOut.class,
				Journey.class, Idle.class, EndEvent.class };
		final SequenceTester checker = getDefaultTester(expectedClasses);

		// change from default: one fewer idle

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 0 });

		// expected fuel costs
		// 0 = no idle
		// 30 = 21 { LNG cost } * 10 { LNG consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0, 0 });

		// change from default: generated charter out
		checker.setExpectedValues(Expectations.DURATIONS, GeneratedCharterOut.class, new Integer[] { 36 });

		// expected charter out overhead
		// -144 = 1.5 { days } * 96 { rate }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, GeneratedCharterOut.class, new Integer[] { -144 });

		final Schedule schedule = ScenarioTools.evaluate(scenario, true);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testGeneratedCharterOutOnTimeCharterVessel() {
		System.err.println("\n\nIdle at end should permit generated charter out event.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96, scenario.getReferenceModel().getPortModel().getPorts());

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final LocalDateTime endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime().toLocalDateTime();

		final int charterRatePerDay = 240000;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		msc.vesselAvailability.setTimeCharterRate("" + charterRatePerDay);

		// return 37 hrs after discharge window ends
		final LocalDateTime returnDate = endDischarge.plusHours(37);
		av.setEndAfter(returnDate);
		av.unsetEndBy();
		System.err.println("Vessel to return after: " + returnDate);

		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Idle.class, GeneratedCharterOut.class,
				Journey.class, Idle.class, EndEvent.class };
		final SequenceTester checker = getDefaultTester(expectedClasses);

		checker.hireCostPerHour = charterRatePerDay / 24;
		// change from default: one fewer idle

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0, 0 });

		// expected fuel costs
		// 0 = no idle
		// 30 = 21 { LNG cost } * 10 { LNG consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0, 0 });

		// change from default: generated charter out
		checker.setExpectedValues(Expectations.DURATIONS, GeneratedCharterOut.class, new Integer[] { 36 });

		// expected charter out overhead
		// -144 = 1.5 { days } * 96 { rate }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, GeneratedCharterOut.class, new Integer[] { -144 });

		final Schedule schedule = ScenarioTools.evaluate(scenario, true);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testNotGeneratedCharterOut() {
		System.err.println("\n\nIdle at end should not permit generated charter out event.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96, scenario.getReferenceModel().getPortModel().getPorts());

		SequenceTester checker = getDefaultTester();

		// no changes should occur from default

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testRegularCharterOutLoadToOrigin() {
		System.err.println("\n\nTest regular charter out from load port to origin port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		CharterOutEvent event = msc.makeCharterOut(msc.loadPort, msc.originPort);

		event.setVesselAssignmentType(msc.vesselAvailability);
		// FIXME: Note - there are three idle events in a row due to the way the internal optimisation represents the transition from charter start to charter end. Not great API but this is the way it
		// works.
		Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, VesselEventVisit.class, Idle.class,
				EndEvent.class };
		SequenceTester checker = getDefaultTester(classes);

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

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	// test doesn't work yet
	public void testStsVoyage() {
		System.err.println("\n\nSTS journey");
		final DefaultScenarioCreator msc = new StsScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		SequenceTester checker = getStsTesterLoad();
		checker.check(sequence);
	}

	/**
	 * Tests a simple load / discharge / discharge cargo to make sure the figures are correct.
	 */
	@Test
	public void testLddVoyage() {
		System.err.println("\n\nLDD journey");
		final DefaultScenarioCreator msc = new LddScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		@SuppressWarnings("unused")
		final Sequence sequence = schedule.getSequences().get(0);

	}

	private SequenceTester getStsTesterLoad() {
		Class<?>[] expectedClasses = { StartEvent.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };

		final SequenceTester checker = new SequenceTester(expectedClasses);

		// set default expected values to zero
		for (final Expectations field : Expectations.values()) {
			checker.setAllExpectedValues(field, 0);
		}

		// expected durations of journeys
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, Journey.class, new Integer[] { 2, 2 });

		// don't care what the duration of the end event is
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, EndEvent.class, new Integer[] { null });

		// don't care what the duration of the start event is
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, StartEvent.class, new Integer[] { null });

		// expected FBO consumptions of journeys
		// none (not economical in default)
		checker.setExpectedValuesIfMatching(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Journey.class, new Integer[] { 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Journey.class, new Integer[] { 10, 30 });

		// expected costs of journeys
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 300 = 30 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 520, 300 });

		// expected durations of idles
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0 });

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0 });

		// expected load / discharge volumes
		// 10000 (load) = vessel capacity
		// 9970 (discharge) = 10000 - 20 { NBO journey consumption } - 10 { NBO idle consumption }
		checker.setExpectedValuesIfMatching(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 10000, -9970 });

		// Min heel in m3
		checker.setExpectedValue(500, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		return checker;

	}
}
