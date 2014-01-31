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

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.LddScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.StsScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;

public class ShippingCalculationsTest {

	/**
	 * Data holder class to store the expected range of indices in a sequence which defines a single P&L item (e.g. a Cargo)
	 * 
	 */
	public class PnlChunkIndexData {
		int startIndex;
		int endIndex;
		boolean isCargo;

		public PnlChunkIndexData(final int start, final int end, boolean cargo) {
			startIndex = start;
			endIndex = end;
			isCargo = cargo;
		}
	}

	public enum Expectations {
		DURATIONS, FUEL_COSTS, HIRE_COSTS, NBO_USAGE, BF_USAGE, FBO_USAGE, PILOT_USAGE, LOAD_DISCHARGE, OVERHEAD_COSTS, MIN_LOAD_VIOLATIONS, MAX_LOAD_VIOLATIONS, MIN_DISCHARGE_VIOLATIONS, MIN_HEEL_VIOLATIONS, VESSEL_CAPACITY_VIOLATIONS, LOST_HEEL_VIOLATIONS
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
			return event.getCharterCost();
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
		case OVERHEAD_COSTS: {
			if (event instanceof Journey)
				return ((Journey) event).getToll();
			else if (event instanceof Cooldown)
				return ((Cooldown) event).getCost();
			else if (event instanceof SlotVisit) {
				return ((SlotVisit) event).getPortCost();
			} else
				// TODO: extract the overhead costs or revenue for drydock events, charter outs etc.
				return null;
		}
		case MIN_LOAD_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MIN_LOAD);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MAX_LOAD_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MAX_LOAD);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MIN_DISCHARGE_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MIN_DISCHARGE);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MIN_HEEL_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				return 0;
			} else {
				return null;
			}
		}
		case VESSEL_CAPACITY_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.VESSEL_CAPACITY);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case LOST_HEEL_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.LOST_HEEL);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		default:
			return null;
		}
	}

	/**
	 * Checks the values of a particular expectation field against the expected values, returning a list of failure messages if any of them are not equal. If the expected or computed value of a field
	 * is null, ignore the comparison and do not produce a failure message.
	 * 
	 * @param field
	 *            The field to check
	 * @param events
	 *            The events whose field values need checking
	 * @param values
	 *            The values of the given field for the list of events (in the same order as the events)
	 * @return A
	 */
	public List<String> checkValues(final Expectations field, final List<? extends Event> events, final Integer[] values) {
		Assert.assertEquals(values.length, events.size());

		List<String> result = new ArrayList<String>();

		for (int i = 0; i < values.length; i++) {
			final Event event = events.get(i);
			final Integer expected = values[i];
			final Integer actual = getValue(field, event);
			// null values are a "not applicable" or "ignore this"
			if (expected != null && actual != null) {
				final String message = String.format("%s expected %d was %d for [%d] %s", field.name(), expected, actual, i, event.toString());
				if (!expected.equals(actual)) {
					result.add(message);
				}
			}
		}

		return result;
	}

	public int getFuelConsumption(final Event event, final Fuel fuel) {
		if (event instanceof FuelUsage) {
			int result = 0;
			FuelUnit unit = (fuel == Fuel.NBO || fuel == Fuel.FBO) ? FuelUnit.M3 : FuelUnit.MT;

			for (final FuelQuantity quantity : ((FuelUsage) event).getFuels()) {
				if (quantity.getFuel() == fuel) {
					for (final FuelAmount a : quantity.getAmounts()) {
						if (a.getUnit() == unit) {
							result += a.getQuantity();
						}
					}
				}
			}
			return result;
		}
		return 0;
	}

	private GroupProfitAndLoss getPnlGroup(Event event) {
		ProfitAndLossContainer container = null;
		if (event instanceof SlotVisit) {
			// and find the cargo associated with it
			container = ((SlotVisit) event).getSlotAllocation().getCargoAllocation();
		}

		if (event instanceof ProfitAndLossContainer) {
			container = (ProfitAndLossContainer) event;
		}

		if (container != null) {
			final GroupProfitAndLoss data = container.getGroupProfitAndLoss();
			return data;
		}

		return null;
	}

	public Long getEventPnl(Event event) {
		final GroupProfitAndLoss data = getPnlGroup(event);

		if (data != null) {
			return data.getProfitAndLoss();
		}

		return null;
	}

	/**
	 * Figure out the separate PnL groups in the itinerary
	 * 
	 * @param events
	 * @return
	 */
	public PnlChunkIndexData[] getPnlChunks(final List<? extends Event> events) {
		ArrayList<Integer> boundaries = new ArrayList<Integer>();
		int index = 0;
		GroupProfitAndLoss lastGroup = null;

		// find the boundaries at which the PnL group changes
		for (Event event : events) {
			final GroupProfitAndLoss data = getPnlGroup(event);
			if (data != null && data != lastGroup) {
				boundaries.add(index);
				lastGroup = data;
			}
			index++;
		}

		boundaries.add(index - 1);

		PnlChunkIndexData[] result = new PnlChunkIndexData[boundaries.size() - 1];
		// construct Pnl chunks from the boundaries
		for (int i = 0; i < boundaries.size() - 1; i++) {
			int start = boundaries.get(i);
			int end = boundaries.get(i + 1) - 1;

			// check if this chunk is a cargo
			boolean isCargo = false;
			for (int j = start; j < end; j++) {
				if (events.get(j) instanceof SlotVisit) {
					isCargo = true;
				}
			}

			result[i] = new PnlChunkIndexData(start, end, isCargo);
		}

		return result;
	}

	public List<String> checkPnlValues(final List<? extends Event> events, final PnlChunkIndexData[] indices, final Integer[] pnls) {
		final List<String> failures = new ArrayList<String>();

		if (pnls != null) {
			Assert.assertEquals(indices.length, pnls.length);
			// each pnl value corresponds to a particular cargo
			for (int i = 0; i < pnls.length; i++) {
				if (pnls[i] != null) {
					for (int j = indices[i].startIndex; j <= indices[i].endIndex; j++) {
						Event event = events.get(j);
						Long pnl = getEventPnl(event);
						if (pnl != null && pnls[i] != null) {
							if ((int) pnls[i] != pnl.intValue()) {
								failures.add(String.format("PnL expected %d was %d for [%d] %s", pnls[i], pnl.intValue(), j, event.toString()));
							}
							continue;
						}
					}
				}
			}
		}

		return failures;
	}

	private int getLoadDischarge(final Event event) {
		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
			final CargoAllocation ca = slotAllocation.getCargoAllocation();
			final Slot slot = slotAllocation.getSlot();

			if (slot instanceof LoadSlot) {
				return slotAllocation.getVolumeTransferred();
			} else if (slot instanceof DischargeSlot) {
				return -slotAllocation.getVolumeTransferred();
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

		final SequenceTester checker = new SequenceTester(expectedClasses);

		// set default expected values to zero
		for (final Expectations field : Expectations.values()) {
			checker.setAllExpectedValues(field, 0);
		}

		// expected durations of journeys
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 1 });

		// don't care what the duration of the end event is
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, EndEvent.class, new Integer[] { null });

		// don't care what the duration of the start event is
		checker.setExpectedValuesIfMatching(Expectations.DURATIONS, StartEvent.class, new Integer[] { null });

		// expected FBO consumptions of journeys
		// none (not economical in default)
		checker.setExpectedValuesIfMatching(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 0, 0 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValuesIfMatching(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 15 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValuesIfMatching(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 150, 520, 150 });

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

		return checker;
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

	public class SequenceTester {
		/** The expected values for each event in the sequence, keyed by expected field */
		private final Map<Expectations, Integer[]> expectedArrays = new HashMap<Expectations, Integer[]>();
		/** The expected PnL values for each PnL group */
		private Integer[] expectedPnlValues = null;
		/** The expected classes of each event in the sequence */
		private Class<?>[] classes;
		/** Information on where each PnL group starts & ends, and whether it is a cargo or not */
		private PnlChunkIndexData[] cargoIndices;

		public float baseFuelPricePerM3 = 10;
		public float purchasePricePerM3 = 21 * 0.5f;
		public float salesPricePerM3 = 21;

		public int hireCostPerHour = 0;

		public SequenceTester(final Class<?>[] classes) {
			setClasses(classes);
		}

		public void setCargoIndices(final PnlChunkIndexData[] cargoIndices) {
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

		/**
		 * Set the expected values for a field, for the appropriate class of vessel event. Do nothing if the length of the provided values does not match the number of expected elements of the
		 * specified class.
		 * 
		 * @param field
		 * @param clazz
		 * @param values
		 */
		public void setExpectedValuesIfMatching(Expectations field, Class<?> clazz, Integer[] values) {
			int count = 0;

			for (int i = 0; i < classes.length; i++) {
				if (classes[i] == clazz) {
					count += 1;
				}
			}

			if (count == values.length) {
				setExpectedValues(field, clazz, values);
			} else {
				System.err.println(String.format("Attempt to set values for %s (%s) failed", field.name(), clazz.getName()));
			}
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
			final List<String> failures = new ArrayList<String>();

			checkClasses(events, classes);

			setupExpectedHireCosts(hireCostPerHour);
			setCargoIndices(getPnlChunks(events));

			for (final Class<?> clazz : new HashSet<Class<?>>(Arrays.asList(classes))) {
				final List<?> objects = extractObjectsOfClass(events, clazz);

				for (final Expectations field : Expectations.values()) {
					failures.addAll(checkValues(field, (List<? extends Event>) objects, getExpectedValues(field, clazz)));
				}

			}

			setupExpectedPnl(purchasePricePerM3, salesPricePerM3, baseFuelPricePerM3);

			failures.addAll(checkPnlValues(events, cargoIndices, expectedPnlValues));

			if (!failures.isEmpty()) {
				String error = failures.get(0);
				for (int i = 1; i < failures.size(); i++) {
					error += "\n" + failures.get(i);
				}
				throw new AssertionFailedError(error);
			}
		}

		private void setupExpectedHireCosts(final int hireRatePerHour) {
			final Integer[] durations = getStorageArray(Expectations.DURATIONS);
			final Integer[] hireCosts = getStorageArray(Expectations.HIRE_COSTS);

			for (int i = 0; i < durations.length; i++) {
				if (hireRatePerHour == 0) {
					hireCosts[i] = 0;
				} else if (durations[i] != null) {
					hireCosts[i] = durations[i] * hireRatePerHour;
				} else {
					hireCosts[i] = 0;
				}

			}

		}

		public void setupOrdinaryFuelCosts() {
			final Integer[] bfUsage = getStorageArray(Expectations.BF_USAGE);
			final Integer[] nboUsage = getStorageArray(Expectations.NBO_USAGE);
			final Integer[] fboUsage = getStorageArray(Expectations.FBO_USAGE);
			final Integer[] fuelCosts = getStorageArray(Expectations.FUEL_COSTS);

			for (int i = 0; i < fuelCosts.length; i++) {
				fuelCosts[i] = (int) (bfUsage[i] * baseFuelPricePerM3 + (fboUsage[i] + nboUsage[i]) * salesPricePerM3);
			}
		}

		private Integer computeExpectedCargoPnl(final PnlChunkIndexData index, final double purchasePricePerM3, final double salesPricePerM3, final double bfPrice) {
			final boolean chargeForLngFuel = !index.isCargo;
			int result = 0;
			for (int i = index.startIndex; i <= index.endIndex; i++) {
				final Integer bfUsage = expectedArrays.get(Expectations.BF_USAGE)[i];
				result -= bfUsage * bfPrice;

				if (chargeForLngFuel) {
					final Integer lngUsage = expectedArrays.get(Expectations.NBO_USAGE)[i] + expectedArrays.get(Expectations.FBO_USAGE)[i];
					result -= lngUsage * salesPricePerM3;
				}

				final Integer hireCost = expectedArrays.get(Expectations.HIRE_COSTS)[i];
				result -= hireCost;

				final Integer routeCost = expectedArrays.get(Expectations.OVERHEAD_COSTS)[i];
				result -= routeCost;

				final Integer loadDischargeVolume = expectedArrays.get(Expectations.LOAD_DISCHARGE)[i];
				// Oops! test fails if there is a capacity violation causing a negative load or discharge volume....
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

		/**
		 * Sets up the expected P&L for all cargoes, based on the purchase price, sales price, base fuel price, fuel costs and load / discharge volumes for those cargoes.
		 * 
		 * @param purchasePricePerM3
		 * @param salesPricePerM3
		 * @param bfPrice
		 */
		private void setupExpectedPnl(final double purchasePricePerM3, final double salesPricePerM3, final double bfPrice) {
			final Integer[] pnl = new Integer[cargoIndices.length];

			for (int i = 0; i < cargoIndices.length; i++) {
				pnl[i] = computeExpectedCargoPnl(cargoIndices[i], purchasePricePerM3, salesPricePerM3, bfPrice);
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
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal

		final Route canal = msc.portCreator.addCanal("canal");
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, canal);
		msc.fleetCreator.assignDefaultCanalData(msc.vc, canal);

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
	public void testCanalRouteLonger() {
		System.err.println("\n\nDon't use canal which is longer than default route");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal, but it is longer than the default route

		final Route canal = msc.portCreator.addCanal("canal");
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 30, canal);
		msc.fleetCreator.assignDefaultCanalData(msc.vc, canal);

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

		final Route canal = msc.portCreator.addCanal("canal");
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, canal);
		msc.fleetCreator.assignDefaultCanalData(msc.vc, canal);
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
	public void testCanalRouteShorterWithDelay() {
		System.err.println("\n\nUse canal which is cheaper than default route but has a delay");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: add a canal

		final Route canal = msc.portCreator.addCanal("canal");
		msc.portCreator.setDistance(msc.loadPort, msc.dischargePort, 10, canal);
		msc.fleetCreator.assignDefaultCanalData(msc.vc, canal);
		final VesselClassRouteParameters routeParameters = msc.getRouteParameters(msc.vc, canal);

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
	public void testPlentyStartHeel() {
		System.err.println("\n\nGenerous Start Heel Means NBO on First Voyage and LNG Rollover");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(5000);
		vesselAvailability.getStartHeel().setCvValue(21);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel (not just base fuel)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(0, Expectations.FBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// change from default scenario
		// first load should be only 5010
		// 5010 = 10000 [vessel capacity] - (5000 [start heel] - 10 [journey boiloff])
		checker.setExpectedValue(5010, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);

		// cost of first journey should be changed accordingly
		// 10m3 * 21 (CV) * 1 (price) = 210
		// 5mt * 10 (price) = 50
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
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(5);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);

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
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario
		final PricingModel pricingModel = scenario.getPricingModel();
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical

		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// but the vessel's capacity is only 50m3 greater than its minimum heel
		// and the journeys (after loading) use a total of 40m3 NBO
		// so only 10m3 is available for FBO, which is not enough for both journeys
		msc.vc.setCapacity(60);
		msc.vc.setMinHeel(10);

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

	@Ignore("Edge case: min heel is perilously close to vessel capacity")
	@Test
	public void testMinHeelForcesBfSupplementAndHeelout() {
		System.err.println("\n\nMinimum Heel Forces BF supplement and heel out");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: make the minimum heel unattainable - thus causing a capacity violation
		// Cause a heel out and BF supplement to be used
		msc.vc.setMinHeel(10000);

		final SequenceTester checker = getDefaultTester();

		// change from default: no NBO consumption (min heel forces BF travel except on laden leg where it is required)
		final Integer[] expectedNboJourneyConsumptions = { 0, 20, 0 };
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, expectedNboJourneyConsumptions);

		// change from default: mostly BF consumption (min heel forces BF travel)
		final Integer[] expectedBaseFuelJourneyConsumptions = { 15, 10, 15 };
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, expectedBaseFuelJourneyConsumptions);

		// expected costs of journeys
		final Integer[] expectedJourneyCosts = { 150, 520, 150 };
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, expectedJourneyCosts);
		checker.setExpectedValue(20, Expectations.MIN_HEEL_VIOLATIONS, SlotVisit.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testHeelMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: cooldown times and volumes specified
		msc.vc.setWarmingTime(0);
		// msc.vc.setCoolingTime(1);
		msc.vc.setCoolingVolume(100);

		msc.setupCooldown(1.0);
		msc.loadPort.setAllowCooldown(true);

		// but a big start heel should mean no cooldown is required
		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(1000);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);
		vesselAvailability.getStartHeel().setCvValue(21);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);

		// cost of first journey should be changed accordingly
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);

		// first load amount is reduced because of leftover fuel from the start heel
		// 9010 = 10000 [ vessel capacity ] - (1000 [ start heel ] - 10 [boiloff] )
		checker.setExpectedValue(9010, Expectations.LOAD_DISCHARGE, SlotVisit.class, 0);

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
		final PricingModel pricingModel = scenario.getPricingModel();
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

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

		// NBO on ballast Min heel in m3
		checker.setExpectedValue(500, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

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
	public void testMinDischargeVolume() {
		System.err.println("\n\nMinimum Discharge Volume Prevents FBO");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: base fuel price more expensive, so FBO is economical
		final PricingModel pricingModel = scenario.getPricingModel();
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		msc.fleetCreator.setBaseFuelPrice(fuelPrice, 100);

		// but minimum discharge volume means that it causes a capacity violation
		msc.cargo.getSlots().get(1).setMinQuantity(9965);

		// for the moment, set min heel to zero since it causes problems in the volume calculations
		msc.vc.setMinHeel(0);

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

	/*
	 * Discussion needed about whether this test is meaningful and what the behaviour should be if it is. The case is: maximum load quantity provides enough LNG fuel to reach the discharge port on NBO
	 * but *not* enough to idle on NBO while there.
	 */
	@Ignore("Discuss desired behaviour before re-enabling this test")
	@Test
	public void testMaxLoadVolumeForcesBfIdle() {
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

		// Expect -10 on discharge (negated for test API)
		final Integer[] expectedloadDischargeVolumes = { 20, 10 };
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, expectedloadDischargeVolumes);

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
		final Date endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

		// return 3 hrs after discharge window ends
		final Date returnDate = new Date(endDischarge.getTime() + 3 * 3600 * 1000);
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
		final Date startLoad = msc.cargo.getSlots().get(0).getWindowStartWithSlotOrPortTime();

		// start 3 hrs before load window begins
		final Date startDate = msc.addHours(startLoad, -3);
		av.setStartBy(startDate);
		av.setStartAfter(msc.addHours(startDate, -5));
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
		final Date startLoad = msc.cargo.getSlots().get(0).getWindowStartWithSlotOrPortTime();
		final Date endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

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
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario: cooldown times and volumes specified
		msc.vc.setWarmingTime(0);
		// msc.vc.setCoolingTime(1);
		msc.vc.setCoolingVolume(100);

		msc.setupCooldown(1.0);
		msc.loadPort.setAllowCooldown(true);

		// change from default scenario: should insert a cooldown event
		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, Cooldown.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class,
				EndEvent.class };

		final SequenceTester checker = getDefaultTester(expectedClasses);

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
	public void testCharterCost_TimeCharter() {
		System.err.println("\n\nTime Charter vessel charter cost ignored.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final int charterRatePerDay = 240000;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		msc.vesselAvailability.setTimeCharterRate(charterRatePerDay);

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
		final FleetModel fleetModel = scenario.getFleetModel();
		fleetModel.getVessels().clear();

		final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();
		scenarioFleetModel.getVesselAvailabilities().clear();
		// Cannot null as final
		// msc.vessel = null;
		// msc.vesselAvailability = null;

		// Set up a charter index curve
		final int charterRatePerDay = 240;
		final CharterIndex spotMarketRate = PricingFactory.eINSTANCE.createCharterIndex();
		final DerivedIndex<Integer> spotMarketRateData = PricingFactory.eINSTANCE.createDerivedIndex();
		spotMarketRateData.setExpression("" + charterRatePerDay);
		spotMarketRate.setData(spotMarketRateData);

		final PricingModel pricingModel = scenario.getPricingModel();
		pricingModel.getCharterIndices().add(spotMarketRate);

		// Create a charter-in market object
		final SpotMarketsModel sportMarketsModel = scenario.getSpotMarketsModel();
		final EList<CharterCostModel> charteringSpotMarkets = sportMarketsModel.getCharteringSpotMarkets();

		final CharterCostModel charterModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
		charteringSpotMarkets.add(charterModel);

		charterModel.getVesselClasses().add(msc.vc);
		charterModel.setSpotCharterCount(1);
		charterModel.setCharterInPrice(spotMarketRate);

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
		msc.vesselAvailability.setTimeCharterRate(charterRatePerDay);

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

		checker.check(sequence);

	}

	// @Ignore("Test description is inconsistent with coded expectations")
	@Test
	public void testLimitedStartHeelIsCapacityViolation() {
		System.err.println("\n\nLimited Start Heel, should still NBO travel and idle - Capacity Violation");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change from default scenario

		final VesselAvailability vesselAvailability = msc.vesselAvailability;
		vesselAvailability.getStartHeel().setVolumeAvailable(10);
		vesselAvailability.getStartHeel().setPricePerMMBTU(1);
		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final Date startLoad = msc.cargo.getSlots().get(0).getWindowStartWithSlotOrPortTime();

		// start 3 hrs before load window begins
		final Date startDate = msc.addHours(startLoad, -3);
		vesselAvailability.setStartBy(startDate);
		vesselAvailability.setStartAfter(msc.addHours(startLoad, -5));
		System.err.println("Vessel to start before: " + startDate);

		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);

		// change from default: BF idle consumption at load port after arrival
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Idle.class, 0);
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 0);
		checker.setExpectedValue(210, Expectations.FUEL_COSTS, Idle.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	private SequenceTester getTesterForVesselEventPostDischarge() {
		Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, VesselEventVisit.class,
				Journey.class, Idle.class, EndEvent.class };
		final SequenceTester checker = getDefaultTester(classes);

		// expected durations of journeys
		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 1 });

		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 2 { duration in hours } * 10 { NBO rate }
		// 0 (vessel empty)
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 0, 0 });

		// expected base consumptions
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		// 10 = 2 { journey duration } * 15 { base fuel consumption } - 20 { LNG consumption }
		// 30 = 2 { journey duration } * 15 { base fuel consumption } - 0 { LNG consumption }
		// 15 = 1 { journey duration } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 10, 30, 15 });

		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }
		// 300 = 10 { base fuel unit cost } * 30 { base fuel consumption }
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Journey.class, new Integer[] { 150, 520, 300, 150 });

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

		// idle costs
		// 210 = 10 { LNG consumption } * 21 { LNG CV } * 1 { LNG cost per MMBTU }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210, 0, 0 });

		return checker;

	}

	@Test
	public void testDryDock() {
		System.err.println("\n\nDry dock event inserted correctly.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// change to default: add a dry dock event 2-3 hrs after discharge window ends
		final Date endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();
		final Date dryDockStartByDate = new Date(endLoad.getTime() + 3 * 3600 * 1000);
		final Date dryDockStartAfterDate = new Date(endLoad.getTime() + 2 * 3600 * 1000);
		msc.vesselEventCreator.createDryDockEvent("DryDock", msc.loadPort, dryDockStartByDate, dryDockStartAfterDate);

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
		final Date endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();
		final Date maintenanceDockStartByDate = new Date(endLoad.getTime() + 3 * 3600 * 1000);
		final Date maintenanceDockStartAfterDate = new Date(endLoad.getTime() + 2 * 3600 * 1000);
		msc.vesselEventCreator.createMaintenanceEvent("Maintenance", msc.loadPort, maintenanceDockStartByDate, maintenanceDockStartAfterDate);

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
		final Date loadDate = loadSlot.getWindowStartWithSlotOrPortTime();

		final double maxSpeed = msc.vc.getMaxSpeed();
		final int firstIdle = 1;
		final Date startAfterDate = msc.addHours(loadDate, -5 * msc.getTravelTime(msc.originPort, msc.loadPort, null, (int) maxSpeed));
		final Date startByDate = msc.addHours(loadDate, -msc.getTravelTime(msc.originPort, msc.loadPort, null, (int) maxSpeed) - firstIdle);

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
	public void testUseDefaultFinalIdlingWhenEndTimeUnspecified() {
		System.err.println("\n\nUnspecified end time should result in idling due to a defined minimum time between last event and end.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.vesselAvailability.unsetEndAfter();
		msc.vesselAvailability.unsetEndBy();

		SequenceTester checker = getDefaultTester();

		final int lastIdleHours = SchedulerBuilder.minDaysFromLastEventToEnd * 24 - checker.getExpectedValues(Expectations.DURATIONS, Journey.class)[2];
		;
		checker.setExpectedValue(lastIdleHours, Expectations.DURATIONS, Idle.class, 2);
		checker.setExpectedValue(lastIdleHours * 5, Expectations.BF_USAGE, Idle.class, 2);
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

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96);

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final Date endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

		// return 37 hrs after discharge window ends
		final Date returnDate = new Date(endDischarge.getTime() + 37l * 3600l * 1000l);
		av.setEndAfter(returnDate);
		av.unsetEndBy();
		System.err.println("Vessel to return after: " + returnDate);

		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, GeneratedCharterOut.class,
				EndEvent.class };
		final SequenceTester checker = getDefaultTester(expectedClasses);

		// change from default: one fewer idle

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10 });

		// expected fuel costs
		// 0 = no idle
		// 30 = 21 { LNG cost } * 10 { LNG consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210 });

		// change from default: generated charter out
		checker.setExpectedValues(Expectations.DURATIONS, GeneratedCharterOut.class, new Integer[] { 36 });

		// expected charter out overhead
		// -144 = 1.5 { days } * 96 { rate }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, GeneratedCharterOut.class, new Integer[] { -144 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testGeneratedCharterOutOnTimeCharterVessel() {
		System.err.println("\n\nIdle at end should permit generated charter out event.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96);

		// change from default scenario: set a "return after" date
		// somewhat later than the end of the discharge window
		final VesselAvailability av = msc.vesselAvailability;
		final Date endDischarge = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

		final int charterRatePerDay = 240000;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		msc.vesselAvailability.setTimeCharterRate(charterRatePerDay);

		// return 37 hrs after discharge window ends
		final Date returnDate = new Date(endDischarge.getTime() + 37l * 3600l * 1000l);
		av.setEndAfter(returnDate);
		av.unsetEndBy();
		System.err.println("Vessel to return after: " + returnDate);

		final Class<?>[] expectedClasses = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, GeneratedCharterOut.class,
				EndEvent.class };
		final SequenceTester checker = getDefaultTester(expectedClasses);

		checker.hireCostPerHour = charterRatePerDay / 24;
		// change from default: one fewer idle

		// expected durations of idles
		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2 });

		// expected base idle consumptions
		// 0 = no idle (start)
		// 0 = no idle (idle on NBO)
		// 0 = no idle (end)
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0 });

		// expected NBO idle consumptions
		// 10 = 2 { idle duration } * 5 { idle NBO rate }
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10 });

		// expected fuel costs
		// 0 = no idle
		// 30 = 21 { LNG cost } * 10 { LNG consumption }
		checker.setExpectedValues(Expectations.FUEL_COSTS, Idle.class, new Integer[] { 0, 210 });

		// change from default: generated charter out
		checker.setExpectedValues(Expectations.DURATIONS, GeneratedCharterOut.class, new Integer[] { 36 });

		// expected charter out overhead
		// -144 = 1.5 { days } * 96 { rate }
		checker.setExpectedValues(Expectations.OVERHEAD_COSTS, GeneratedCharterOut.class, new Integer[] { -144 });

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	@Test
	public void testNotGeneratedCharterOut() {
		System.err.println("\n\nIdle at end should not permit generated charter out event.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		msc.pricingCreator.createDefaultCharterCostModel(msc.vc, 1, 96);

		SequenceTester checker = getDefaultTester();

		// no changes should occur from default

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);

	}

	public CharterOutEvent makeCharterOut(MinimalScenarioCreator msc, MMXRootObject scenario, Port startPort, Port endPort) {
		// change to default: add a charter out event 2-3 hrs after discharge window ends
		final Date endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();
		final Date charterStartByDate = new Date(endLoad.getTime() + 3 * 3600 * 1000);
		final Date charterStartAfterDate = new Date(endLoad.getTime() + 2 * 3600 * 1000);
		int charterOutRate = 24;
		CharterOutEvent event = msc.vesselEventCreator.createCharterOutEvent("CharterOut", startPort, endPort, charterStartByDate, charterStartAfterDate, charterOutRate);
		event.getHeelOptions().setVolumeAvailable(0);
		event.getHeelOptions().setCvValue(21);
		event.getHeelOptions().setPricePerMMBTU(1);

		return event;
	}

	@Test
	public void testRegularCharterOutLoadToOrigin() {
		System.err.println("\n\nTest regular charter out from load port to origin port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		CharterOutEvent event = makeCharterOut(msc, scenario, msc.loadPort, msc.originPort);

		// FIXME: Note - there are three idle events in a row due to the way the internal optimisation represents the transition from charter start to charter end. Not great API but this is the way it
		// works.
		Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, Idle.class, Idle.class,
				VesselEventVisit.class, Idle.class, EndEvent.class };
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

	@Test
	public void testRegularCharterOutLoadToLoadNoHeel() {
		System.err.println("\n\nTest regular charter out from load port to load port.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		CharterOutEvent event = makeCharterOut(msc, scenario, msc.loadPort, msc.loadPort);

		SequenceTester checker = getTesterForVesselEventPostDischarge();
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

		CharterOutEvent event = makeCharterOut(msc, scenario, msc.loadPort, msc.loadPort);

		event.getHeelOptions().setVolumeAvailable(20);

		SequenceTester checker = getTesterForVesselEventPostDischarge();

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
		// NBO on ballast - Charter out volume - ballast NBO
		checker.setExpectedValue(10, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}

	@Test
	public void testCharterOutRollover() {
		System.err.println("\n\nTest LNG heel from early charter out event is rolled over.");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// add a charter out event prior to the first cargo.
		final Date startLoad = msc.getFirstAppointment().getSecond();
		final Date charterStartByDate = msc.addHours(startLoad, -25);
		final Date charterStartAfterDate = msc.addHours(startLoad, -25);
		int charterOutRate = 24;
		CharterOutEvent event = msc.vesselEventCreator.createCharterOutEvent("CharterOut", msc.originPort, msc.originPort, charterStartByDate, charterStartAfterDate, charterOutRate);

		// set the charter out required end heel to 5000 (and set some other things)
		event.getHeelOptions().setVolumeAvailable(5000);
		event.getHeelOptions().setCvValue(21);
		event.getHeelOptions().setPricePerMMBTU(1);

		// recalculate the vessel availability based on the new timetable
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		// we now expect an idle and a vessel event visit before the first journey
		Class<?>[] expectedClasses = { StartEvent.class, Idle.class, VesselEventVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class,
				Idle.class, EndEvent.class };

		SequenceTester checker = getDefaultTester(expectedClasses);

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

		final Sequence sequence = schedule.getSequences().get(0);

	}

	@Test
	public void testHeelRollover() {
		System.err.println("\n\nTest min heel rollover: LNG travel due to expensive BF");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// create an additional cargo
		msc.createDefaultCargo(msc.loadPort, msc.dischargePort);
		// and send the vessel back to the origin port at end of itinerary
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		msc.vc.setMinHeel(500);

		final PricingModel pricingModel = scenario.getPricingModel();
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);

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

		SequenceTester checker = getDefaultTester(classes);

		checker.setExpectedValues(Expectations.DURATIONS, Journey.class, new Integer[] { 1, 2, 2, 2, 1 });
		checker.setExpectedValues(Expectations.BF_USAGE, Journey.class, new Integer[] { 15, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Journey.class, new Integer[] { 0, 10, 10, 10, 5 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Journey.class, new Integer[] { 0, 20, 20, 20, 10 });

		checker.setExpectedValues(Expectations.DURATIONS, Idle.class, new Integer[] { 0, 2, 2, 2, 0 });
		checker.setExpectedValues(Expectations.BF_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.FBO_USAGE, Idle.class, new Integer[] { 0, 0, 0, 0, 0 });
		checker.setExpectedValues(Expectations.NBO_USAGE, Idle.class, new Integer[] { 0, 10, 10, 10, 0 });

		// volume allocations: load 10000 at first load port (loading from empty)
		// at first discharge, retain 530m3 (500 min heel plus 30m3 travel fuel) and 40m3 was used to get here
		// at next load, load back up to full (500 min heel minus 10m3 idle fuel was on board)
		// at next discharge, retain 515m3 (500 min heel plus 15m3 travel fuel) and 40m3 was used to get here
		checker.setExpectedValues(Expectations.LOAD_DISCHARGE, SlotVisit.class, new Integer[] { 10000, -9430, 9510, -9445 });

		// Min heel in m3
//		checker.setExpectedValue(500, Expectations.LOST_HEEL_VIOLATIONS, EndEvent.class, 0);

		checker.baseFuelPricePerM3 = 100;
		checker.setupOrdinaryFuelCosts();

		final Sequence sequence = schedule.getSequences().get(0);
		checker.check(sequence);
	}

	@Test
	@Ignore("This doesn't actually test anything yet - and involves a known issue.")
	public void testEventHeelRollover() {
		System.err.println("\n\nTest min heel rollover for maintenance event: LNG travel due to expensive BF");

		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// create a maintenance event after the cargo
		Port eventPort = msc.loadPort;

		msc.createDefaultMaintenanceEvent("Maintenance", eventPort, null);

		// and recalculate the vessel availability
		msc.setDefaultAvailability(msc.originPort, msc.originPort);

		// force a heel rollover at the maintenance port
		msc.vc.setMinHeel(500);

		final PricingModel pricingModel = scenario.getPricingModel();
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);

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

}
