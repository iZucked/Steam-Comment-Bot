/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator.MinimalScenarioSetup;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.TradingConstants;

public class ShippingCalculationsTest {

	/**
	 * Data holder class to store the expected range of indices in a sequence which defines a Cargo
	 * 
	 */
	public class CargoIndexData {
		int startIndex;
		int endIndex;

		public CargoIndexData(final int start, final int end) {
			startIndex = start;
			endIndex = end;
		}
	}

	public enum Expectations {
		DURATIONS, FUEL_COSTS, HIRE_COSTS, NBO_USAGE, BF_USAGE, FBO_USAGE, PILOT_USAGE, LOAD_DISCHARGE, ROUTE_COSTS
	}

	public void checkClasses(final EList<? extends Object> objects, final Class<?>[] classes) {
		Assert.assertEquals("Unexpected number of events in sequence", classes.length, objects.size());
		for (int i = 0; i < classes.length; i++) {
			if (i >= objects.size()) {
				return;
			}
			final Object object = objects.get(i);
			final Class<?> clazz = classes[i];
			final String format = "Event #%d of schedule should be instance of %s not %s.";
			Assert.assertTrue(String.format(format, i, clazz.toString(), object.getClass().toString()), clazz.isInstance(object));
		}
	}

	private Integer getValue(final Expectations field, final Event event) {
		switch (field) {
		case DURATIONS:
			return event.getDuration();
		case HIRE_COSTS:
			return event.getHireCost();
		case FUEL_COSTS: {
			if (event instanceof FuelUsage)
				return ((FuelUsage) event).getFuelCost();
			else
				return 0;
		}
		case LOAD_DISCHARGE:
			return getLoadDischarge(event);
		case BF_USAGE:
			return getFuelConsumption(event, Fuel.BASE_FUEL);
		case FBO_USAGE:
			return getFuelConsumption(event, Fuel.FBO);
		case NBO_USAGE:
			return getFuelConsumption(event, Fuel.NBO);
		case PILOT_USAGE:
			return getFuelConsumption(event, Fuel.PILOT_LIGHT);
		default:
			return null;
		}
	}

	public void checkValues(final Expectations field, final List<? extends Event> events, final Integer[] values) {
		Assert.assertEquals(values.length, events.size());

		for (int i = 0; i < values.length; i++) {
			final Event event = events.get(i);
			final Integer expected = values[i];
			final Integer actual = getValue(field, event);
			if (expected != null && actual != null) {
				final String message = String.format("%s %s", field.name(), event.toString());
				Assert.assertEquals(message, expected, actual);
			}
		}
	}

	public int getFuelConsumption(final Event event, final Fuel fuel) {
		if (event instanceof FuelUsage) {
			int result = 0;
			for (final FuelQuantity quantity : ((FuelUsage) event).getFuels()) {
				if (quantity.getFuel() == fuel) {
					for (final FuelAmount a : quantity.getAmounts()) {
						result += a.getQuantity();
					}
				}
			}
			return result;
		}
		return 0;
	}

	public void checkPnlValues(final List<? extends Event> events, final CargoIndexData[] indices, final Integer[] pnls) {
		if (pnls != null) {
			Assert.assertEquals(indices.length, pnls.length);
			// each pnl value corresponds to a particular cargo
			for (int i = 0; i < pnls.length; i++) {
				if (pnls[i] != null) {
					for (int j = indices[i].startIndex; j <= indices[i].endIndex; j++) {
						// find the first SlotVisit in the cargo range
						if (events.get(j) instanceof SlotVisit) {
							final SlotVisit event = (SlotVisit) events.get(j);
							// and find the cargo associated with it
							final CargoAllocation ca = event.getSlotAllocation().getCargoAllocation();
							final ExtraData data = ca.getDataWithKey(TradingConstants.ExtraData_GroupValue);
							if (data != null) {
								final int pnl = data.getValueAs(Integer.class);

								Assert.assertEquals("PnL for " + event, (int) pnls[i], pnl);
							}
							continue;
						}
					}
				}
			}
		}
	}

	private int getLoadDischarge(final Event event) {
		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final CargoAllocation ca = slotVisit.getSlotAllocation().getCargoAllocation();
			final Slot slot = slotVisit.getSlotAllocation().getSlot();

			if (slot instanceof LoadSlot) {
				return ca.getLoadVolume();
			} else if (slot instanceof DischargeSlot) {
				return -ca.getDischargeVolume();
			}

		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> extractObjectsOfClass(final EList<? extends Object> objects, final Class<T> clazz) {
		final LinkedList<T> result = new LinkedList<T>();
		for (final Object object : objects) {
			if (clazz.isInstance(object)) {
				result.add((T) object);
			}
		}
		return result;
	}

	public SequenceTester getDefaultTester() {
		// expected classes of the sequence elements
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };
		return getDefaultTester(expectedClasses);
	}

	/*
	 * We need to create a barebones scenario with a single vessel schedule. Then the scenario needs to be evaluated to test correct calculation of: - Fuel costs - Port costs - Route costs - NBO rates
	 */

	public SequenceTester getDefaultTester(final Class<?>[] expectedClasses) {

		final CargoIndexData[] cargoIndices = { new CargoIndexData(3, 8) };
		final SequenceTester checker = new SequenceTester(expectedClasses, cargoIndices);

		// set default expected values to zero
		for (final Expectations field : Expectations.values()) {
			checker.setAllExpectedValues(field, 0);
		}

		// expected durations of journeys
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 1 });

		// don't care what the duration of the end event is
		checker.setExpectedValues(Expectations.DURATIONS, EndEvent.class, new Integer[] { null });

		// don't care what the duration of the start event is
		checker.setExpectedValues(Expectations.DURATIONS, StartEvent.class, new Integer[] { null });

		// expected FBO consumptions of journeys
		// none (not economical in default)
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0, 0 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 15 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 150, 520, 150 });

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 0 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 0 });

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0 });

		// expected load / discharge volumes
		// 10000 (load) = vessel capacity
		// 9970 (discharge) = 10000 - 20 { NBO journey consumption } - 10 { NBO idle consumption }
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 10000, -9970 });

		// profit / loss
		// 9970 { sales volume } * 1 { price per MMBTU } * 21 { CV } - 10000 { purchase volume } * 0.5 { price per MMBTU } * 21 { CV } - 250 { base fuel cost }
		// int [] expectedPnlValues = { 104120, 104120 };
		// int [] expectedPnlValues = { 209120 };
		// Integer [] expectedPnlValues = { null };
		// checker.setExpectedPnlValues(expectedPnlValues);

		return checker;
	}

	public class SequenceTester {
		private final Map<Expectations, Integer[]> expectedArrays = new HashMap<Expectations, Integer[]>();
		private Integer[] expectedPnlValues = null;
		private Class<?>[] classes;
		private CargoIndexData[] cargoIndices;

		public float baseFuelPricePerM3 = 10;
		public float purchasePricePerM3 = 21 * 0.5f;
		public float salesPricePerM3 = 21;

		public SequenceTester(final Class<?>[] classes, final CargoIndexData[] cargoIndices) {
			setClasses(classes);
			setCargoIndices(cargoIndices);
		}

		public void setCargoIndices(final CargoIndexData[] cargoIndices) {
			this.cargoIndices = cargoIndices;
		}

		public void setClasses(final Class<?>[] classes) {
			this.classes = classes;
			for (final Expectations field : Expectations.values()) {
				expectedArrays.put(field, new Integer[classes.length]);
				for (int i = 0; i < classes.length; i++) {
					expectedArrays.get(field)[i] = 0;
				}
			}
		}

		private Integer[] getStorageArray(final Expectations field) {
			return expectedArrays.get(field);
		}

		private <T> List<Integer> getValueIndices(final T[] array, final T value) {
			final ArrayList<Integer> result = new ArrayList<Integer>();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == value) {
					result.add(i);
				}
			}
			return result;
		}

		public void setExpectedValues(final Expectations field, final Class<?> clazz, final Integer[] values) {
			final Integer[] array = getStorageArray(field);
			int perClassIndex = 0;
			for (final int index : getValueIndices(classes, clazz)) {
				array[index] = values[perClassIndex];
				perClassIndex += 1;
			}
		}

		public Integer[] getExpectedValues(final Expectations field, final Class<?> clazz) {
			final List<Integer> indices = getValueIndices(classes, clazz);
			final Integer[] array = getStorageArray(field);
			final Integer[] result = new Integer[indices.size()];

			int perClassIndex = 0;
			for (final int index : indices) {
				result[perClassIndex] = array[index];
				perClassIndex += 1;
			}

			return result;
		}

		public void setExpectedValue(final int value, final Expectations field, final Class<?> clazz, final int index) {
			final List<Integer> indices = getValueIndices(classes, clazz);
			final Integer[] array = getStorageArray(field);
			array[indices.get(index)] = value;

		}

		public void setAllExpectedValues(final Expectations field, final Integer value) {
			final Integer[] array = getStorageArray(field);
			for (int i = 0; i < array.length; i++) {
				array[i] = value;
			}
		}

		public void setExpectedPnlValues(final Integer[] pnl) {
			expectedPnlValues = pnl;
		}

		public void additionalChecks() {
		}

		@SuppressWarnings("unchecked")
		public void check(final Sequence sequence) {
			final EList<Event> events = sequence.getEvents();
			checkClasses(events, classes);

			for (final Class<?> clazz : new HashSet<Class<?>>(Arrays.asList(classes))) {
				final List<?> objects = extractObjectsOfClass(events, clazz);

				for (final Expectations field : Expectations.values()) {
					checkValues(field, (List<? extends Event>) objects, getExpectedValues(field, clazz));
				}

			}

			setupExpectedPnl(purchasePricePerM3, salesPricePerM3, baseFuelPricePerM3);

			checkPnlValues(events, cargoIndices, expectedPnlValues);
		}

		public void setupExpectedHireCosts(final int hireRatePerHour) {
			final Integer[] durations = getStorageArray(Expectations.DURATIONS);
			final Integer[] hireCosts = getStorageArray(Expectations.HIRE_COSTS);

			for (int i = 0; i < durations.length; i++) {
				if (durations[i] != null) {
					hireCosts[i] = durations[i] * hireRatePerHour;
				} else {
					hireCosts[i] = null;
				}

			}

		}

		private Integer computeExpectedPnl(final CargoIndexData index, final double purchasePricePerM3, final double salesPricePerM3, final double bfPrice) {
			int result = 0;
			for (int i = index.startIndex; i <= index.endIndex; i++) {
				final Integer bfUsage = expectedArrays.get(Expectations.BF_USAGE)[i];
				result -= bfUsage * bfPrice;

				final Integer hireCost = expectedArrays.get(Expectations.HIRE_COSTS)[i];
				result -= hireCost;

				final Integer routeCost = expectedArrays.get(Expectations.ROUTE_COSTS)[i];
				result -= routeCost;

				final Integer loadDischargeVolume = expectedArrays.get(Expectations.LOAD_DISCHARGE)[i];
				// discharge
				if (loadDischargeVolume < 0) {
					result -= loadDischargeVolume * salesPricePerM3;
				}
				// load
				if (loadDischargeVolume > 0) {
					result -= loadDischargeVolume * purchasePricePerM3;
				}
			}
			return result;

		}

		public void setupExpectedPnl(final double purchasePricePerM3, final double salesPricePerM3, final double bfPrice) {
			final Integer[] pnl = new Integer[cargoIndices.length];

			for (int i = 0; i < cargoIndices.length; i++) {
				pnl[i] = computeExpectedPnl(cargoIndices[i], purchasePricePerM3, salesPricePerM3, bfPrice);
			}

			setExpectedPnlValues(pnl);

		}

	}

	/*
	 * We need to create a barebones scenario with a single vessel schedule. Then the scenario needs to be evaluated to test correct calculation of: - Fuel costs - Port costs - Route costs - NBO rates
	 */

	@Test
	public void testCanalRouteShorter() {
		System.err.println("\n\nUse canal which is cheaper than default route");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a canal

		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);

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
		checker.setExpectedValue(1, Expectations.ROUTE_COSTS, Journey.class, 1);

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

		final Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 1, ladenJourney.getToll());

	}

	@Test
	public void testCanalRouteLonger() {
		System.err.println("\n\nDon't use canal which is longer than default route");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a canal, but it is longer than the default route

		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 30, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);

		final SequenceTester checker = getDefaultTester();

		// no change from default scenario: canal route should be ignored

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		final Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 0, ladenJourney.getToll());
	}

	@Test
	public void testCanalRouteTooExpensive() {
		System.err.println("\n\nDon't use canal which is has a high cost associated with it");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a canal,
		// which is shorter than the default route
		// but has a high usage cost

		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		final RouteCost cost = dsc.getRouteCost(mss.vc, canal);
		cost.setLadenCost(500);

		final SequenceTester checker = getDefaultTester();

		// no change from default scenario: canal route should be ignored

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		final Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 0, ladenJourney.getToll());
	}

	@Test
	public void testCanalRouteShorterWithDelay() {
		System.err.println("\n\nUse canal which is cheaper than default route but has a delay");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a canal

		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		final VesselClassRouteParameters routeParameters = dsc.getRouteParameters(mss.vc, canal);

		routeParameters.setExtraTransitTime(2);
		routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
		routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// second journey is now 1 hr longer due to canal usage (but 10 units shorter distance)
		// so fuel usage is halved, plus extra from canal

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(12, Expectations.NBO_USAGE, Journey.class, 1);

		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(7, Expectations.BF_USAGE, Journey.class, 1);

		// second journey cost is different
		checker.setExpectedValue(322, Expectations.FUEL_COSTS, Journey.class, 1);

		// second journey faces additional canal cost
		checker.setExpectedValue(1, Expectations.ROUTE_COSTS, Journey.class, 1);

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

		final Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 1, ladenJourney.getToll());
	}

	@Test
	public void testPlentyStartHeel() {
		System.err.println("\n\nGenerous Start Heel Means NBO on First Voyage");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();

		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(1000);
		vessel.getStartHeel().setPricePerMMBTU(1);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel (not just base fuel)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// cost of first journey should be changed accordingly
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	/*
	 * Note: NBO from partial start heel is ignored by the voyage calculator because the calculator is based on fuel choices, and partial NBO is not a fuel choice. This unit test has been disabled,
	 * although the calculation it tracks is not handled correctly by the current voyage calculator. In future it may need to be re-enabled.
	 */
	@Ignore("Disabled since partial NBO is not supported.")
	@Test
	public void testLimitedStartHeel() {
		System.err.println("\n\nLimited Start Heel");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();

		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(5);
		vessel.getStartHeel().setPricePerMMBTU(1);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// cost of first journey should be changed accordingly
		checker.setExpectedValue(205, Expectations.FUEL_COSTS, Journey.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testFBOLimitedByMinHeel() {
		System.err.println("\n\nUse FBO for one trip after loading");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		final MMXRootObject scenario = dsc.buildScenario();

		// change from default scenario
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);
		// but the vessel's capacity is only 50m3 greater than its minimum heel
		// and the journeys (after loading) use a total of 40m3 NBO
		// so only 10m3 is available for FBO, which is not enough for both journeys
		mss.vc.setCapacity(60);
		mss.vc.setMinHeel(10);

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

	@Test
	public void testMinHeelForcesBfJourney() {
		System.err.println("\n\nMinimum Heel Forces BF only journey");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: make the minimum heel unattainable
		mss.vc.setMinHeel(10000);

		final SequenceTester checker = getDefaultTester();

		// change from default: no NBO consumption (min heel forces BF travel)
		final Integer[] expectedNboJourneyConsumptions = { 0, 0, 0 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, expectedNboJourneyConsumptions);

		// change from default: all BF consumption (min heel forces BF travel)
		final Integer[] expectedBaseFuelJourneyConsumptions = { 15, 30, 15 };
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, expectedBaseFuelJourneyConsumptions);

		// expected costs of journeys
		final Integer[] expectedJourneyCosts = { 150, 300, 150 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testHeelMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);

		mss.setupCooldown(1.0);
		mss.loadPort.setAllowCooldown(true);

		// but a big start heel should mean no cooldown is required
		mss.vessel.getStartHeel().setVolumeAvailable(1000);
		mss.vessel.getStartHeel().setPricePerMMBTU(1);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// cost of first journey should be changed accordingly
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	/*
	 * We need to create a barebones scenario with a single vessel schedule. Then the scenario needs to be evaluated to test correct calculation of: - Fuel costs - Port costs - Route costs - NBO rates
	 */

	@Test
	public void testBasicScenario() {
		System.err.println("\n\nBasic Scenario");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();

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
		 * // check they go between the right places and have the right distances dsc.checkJourneyGeography(originToLoad, mss.originPort, mss.loadPort); dsc.checkJourneyGeography(loadToDischarge,
		 * mss.loadPort, mss.dischargePort); dsc.checkJourneyGeography(dischargeToOrigin, mss.dischargePort, mss.originPort);
		 */

	}

	@Test
	public void testFBODesirable() {
		System.err.println("\n\nUse FBO for both trips after loading");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();

		// change from default scenario
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);

		final SequenceTester checker = getDefaultTester();
		checker.baseFuelPricePerM3 = 100;

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

		// idle LNG consumption is 10, plus 30 + 15 for journeys and 500 min heel
		final Integer[] expectedloadDischargeVolumes = { 10000, -9445 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testMaxLoadVolume() {
		System.err.println("\n\nMaximum Load Volume Limits Load & Discharge");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a maximum load volume
		mss.cargo.getLoadSlot().setMaxQuantity(500);

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
	public void testMaxDischargeVolume() {
		System.err.println("\n\nMaximum Discharge Volume Limits Load & Discharge");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a maximum load volume
		mss.cargo.getDischargeSlot().setMaxQuantity(500);

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
	public void testMinDischargeVolume() {
		System.err.println("\n\nMinimum Discharge Volume Prevents FBO");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: base fuel price more expensive, so FBO is economical
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);

		// but minimum discharge volume means that it causes a capacity violation
		mss.cargo.getDischargeSlot().setMinQuantity(9965);

		// for the moment, set min heel to zero since it causes problems in the volume calculations
		mss.vc.setMinHeel(0);

		final SequenceTester checker = getDefaultTester();
		checker.baseFuelPricePerM3 = 100;

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
	public void testMaxLoadVolumeForcesBfIdle() {
		System.err.println("\n\nMaximum Load Volume Forces BF Idle");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: add a maximum load volume
		mss.cargo.getLoadSlot().setMaxQuantity(20);

		final SequenceTester checker = getDefaultTester();

		final Integer[] expectedloadDischargeVolumes = { 20, 0 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testIdleAfterVesselReturn() {
		System.err.println("\n\nSpecified date for vessel return causes idling.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = mss.vessel.getAvailability();
		final Date endDischarge = mss.cargo.getDischargeSlot().getWindowEndWithSlotOrPortTime();

		// return 3 hrs after discharge window ends
		final Date returnDate = new Date(endDischarge.getTime() + 3 * 3600 * 1000);
		av.setEndAfter(returnDate);
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
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = mss.vessel.getAvailability();
		final Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();

		// start 3 hrs before load window begins
		final Date startDate = new Date(startLoad.getTime() - 3 * 3600 * 1000);
		av.setStartBy(startDate);
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
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = mss.vessel.getAvailability();
		final Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();
		final Date endDischarge = mss.cargo.getDischargeSlot().getWindowEndWithSlotOrPortTime();

		// start within 5 hrs before load window starts
		final Date startDate = new Date(startLoad.getTime() - 5 * 3600 * 1000);
		av.setStartAfter(startDate);
		System.err.println("Vessel to start after: " + startDate);

		// return within 5 hrs after discharge window ends
		final Date returnDate = new Date(endDischarge.getTime() + 5 * 3600 * 1000);
		av.setEndBy(returnDate);
		System.err.println("Vessel to return by: " + returnDate);

		// should have no effect on the generated schedule
		final SequenceTester checker = getDefaultTester();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Ignore("Cooldown now takes zero time")
	@Test
	public void testExtraTimeScheduledForCooldown() {
		System.err.println("\n\nExtra time should be scheduled after leaving start port for cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);

		mss.setupCooldown(1.0);
		mss.loadPort.setAllowCooldown(true);

		// change from default scenario: should insert a cooldown event
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class,
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);

		checker.setCargoIndices(new CargoIndexData[] { new CargoIndexData(4, 9) });

		// change from default: cooldown time
		final Integer[] expectedCooldownTimes = { 1 };
		checker.setExpectedValues(Expectations.DURATIONS, Cooldown.class, expectedCooldownTimes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testLongWarmupMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(3);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);

		mss.setupCooldown(1.0);
		mss.loadPort.setAllowCooldown(true);

		final SequenceTester checker = getDefaultTester();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testCooldownAdded() {
		System.err.println("\n\nCooldown event should be scheduled at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(0);
		mss.vc.setCoolingVolume(100);

		mss.setupCooldown(1.0);
		mss.loadPort.setAllowCooldown(true);

		// change from default scenario: should insert a cooldown event
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class,
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);
		checker.setCargoIndices(new CargoIndexData[] { new CargoIndexData(4, 9) });

		// change from default: cooldown time
		final Integer[] expectedCooldownTimes = { 0 };
		checker.setExpectedValues(Expectations.DURATIONS, Cooldown.class, expectedCooldownTimes);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Cooldown cooldown = extractObjectsOfClass(sequence.getEvents(), Cooldown.class).get(0);
		Assert.assertEquals("Cooldown cost", 2100, cooldown.getCost());

		checker.check(sequence);
	}

	@Test
	public void testCharterCost_IgnoredForTimeCharter() {
		System.err.println("\n\nTime Charter vessel charter cost ignored.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		final int charterRatePerDay = 0;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		mss.vessel.setTimeCharterRate(charterRatePerDay);

		final SequenceTester checker = getDefaultTester();

		checker.setupExpectedHireCosts(charterRatePerDay);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	@Test
	public void testCharterCost_SpotCharterIn() {
		System.err.println("\n\nSpot charter-in vessel charter cost added correctly.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		// Remove default vessel
		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		fleetModel.getVessels().clear();

		// Set up a charter index curve
		final int charterRatePerDay = 240;
		final DerivedIndex<Integer> spotMarketRate = PricingFactory.eINSTANCE.createDerivedIndex();
		spotMarketRate.setExpression("" + charterRatePerDay);

		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		pricingModel.getCharterIndices().add(spotMarketRate);

		// Create a charter-in market object
		final SpotMarketsModel sportMarketsModel = scenario.getSubModel(SpotMarketsModel.class);
		final EList<CharterCostModel> charteringSpotMarkets = sportMarketsModel.getCharteringSpotMarkets();

		final CharterCostModel charterModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
		charteringSpotMarkets.add(charterModel);

		charterModel.getVesselClasses().add(mss.vc);
		charterModel.setSpotCharterCount(1);
		charterModel.setCharterInPrice(spotMarketRate);

		// Spot charter-in vessels have fewer voyages
		final SequenceTester checker;
		{
			final Class<?>[] expectedClasses = { SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };
			checker = getTestCharterCost_SpotCharterInTester(expectedClasses);
		}

		final int hireCostPerHour = charterRatePerDay / 24;
		checker.setupExpectedHireCosts(hireCostPerHour);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	/**
	 * Create a different set of expectations to that of {@link #getDefaultTester()} to handle the spot charter-in in {@link #testCharterCost_SpotCharterIn()}. This vessel has no initial leg and
	 * returns to the load port rather than origin port - thus doubling the return leg time
	 * 
	 * @param expectedClasses
	 * @return
	 */
	public SequenceTester getTestCharterCost_SpotCharterInTester(final Class<?>[] expectedClasses) {

		final CargoIndexData[] cargoIndices = { new CargoIndexData(0, 4) };
		final SequenceTester checker = new SequenceTester(expectedClasses, cargoIndices);

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

		// profit / loss
		// 9970 { sales volume } * 1 { price per MMBTU } * 21 { CV } - 10000 { purchase volume } * 0.5 { price per MMBTU } * 21 { CV } - 250 { base fuel cost }
		// int [] expectedPnlValues = { 104120, 104120 };
		// int [] expectedPnlValues = { 209120 };
		// Integer [] expectedPnlValues = { null };
		// checker.setExpectedPnlValues(expectedPnlValues);

		return checker;
	}

	@Test
	public void testCharterCostUnset() {
		System.err.println("\n\nZero vessel charter cost added correctly.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		final int charterRatePerDay = 0;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		mss.vessel.setTimeCharterRate(charterRatePerDay);

		final SequenceTester checker = getDefaultTester();

		final int hireCostPerHour = charterRatePerDay / 24;
		checker.setupExpectedHireCosts(hireCostPerHour);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

		// change from default scenario: sequence daily hire rate should be set
		Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	@Test
	public void testVesselStartsAnywhere() {
		System.err.println("\n\nVessel starts anywhere.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		mss.vessel.getAvailability().getStartAt().clear();

		final SequenceTester checker = getDefaultTester();

		// change from default scenario: vessel makes only two journeys
		final Class<?>[] expectedClasses = { StartEvent.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, EndEvent.class };

		checker.setClasses(expectedClasses);

		// expected durations of journeys
		final Integer[] expectedJourneyDurations = { 2, 1 };
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, expectedJourneyDurations);

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
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
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
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		mss.vessel.getAvailability().getEndAt().clear();

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

		checker.check(sequence);

	}

	@Test
	public void testLimitedStartHeelForcesBfIdle() {
		System.err.println("\n\nLimited Start Heel, should NBO travel and BF on idle");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();

		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;

		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(10);
		vessel.getStartHeel().setPricePerMMBTU(1);

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = mss.vessel.getAvailability();
		final Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();

		// start 3 hrs before load window begins
		final Date startDate = new Date(startLoad.getTime() - 3 * 3600 * 1000);
		av.setStartBy(startDate);
		System.err.println("Vessel to start before: " + startDate);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);

		// change from default: BF idle consumption at load port after arrival
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 0);
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 0);
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

}
