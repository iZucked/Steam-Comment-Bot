/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
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
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

import junit.framework.AssertionFailedError;

public class AbstractShippingCalculationsTestClass {

	/**
	 * Data holder class to store the expected range of indices in a sequence which defines a single P&L item (e.g. a Cargo)
	 * 
	 */
	public class PnlChunkIndexData {
		int startIndex;
		int endIndex;
		boolean isCargo;

		public PnlChunkIndexData(final int start, final int end, final boolean cargo) {
			startIndex = start;
			endIndex = end;
			isCargo = cargo;
		}
	}

	public enum Expectations {
		DURATIONS, FUEL_COSTS, HIRE_COSTS, NBO_USAGE, BF_USAGE, FBO_USAGE, PILOT_USAGE, LOAD_DISCHARGE, OVERHEAD_COSTS, MIN_LOAD_VIOLATIONS, MAX_LOAD_VIOLATIONS, MIN_DISCHARGE_VIOLATIONS, MAX_HEEL_VIOLATIONS, MIN_HEEL_VIOLATIONS, VESSEL_CAPACITY_VIOLATIONS, LOST_HEEL_VIOLATIONS, COOLDOWN_VIOLATION, HEEL_COST, HEEL_REVENUE
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
		case COOLDOWN_VIOLATION: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.FORCED_COOLDOWN);
				return (value == null) ? 0 : 1;
			} else {
				return null;
			}
		}
		case MIN_LOAD_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MIN_LOAD);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MAX_LOAD_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MAX_LOAD);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MIN_DISCHARGE_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MIN_DISCHARGE);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MIN_HEEL_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MIN_HEEL);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case MAX_HEEL_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.MAX_HEEL);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case VESSEL_CAPACITY_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.VESSEL_CAPACITY);
				return (value == null) ? 0 : (int) (long) value;
			} else {
				return null;
			}
		}
		case LOST_HEEL_VIOLATIONS: {
			if (event instanceof CapacityViolationsHolder) {
				final Long value = ((CapacityViolationsHolder) event).getViolations().get(CapacityViolationType.LOST_HEEL);
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

		final List<String> result = new ArrayList<String>();

		for (int i = 0; i < values.length; i++) {
			final Event event = events.get(i);
			final Integer expected = values[i];
			final Integer actual = getValue(field, event);
			// null values are a "not applicable" or "ignore this"
			if (expected != null && actual != null) {
				if (!expected.equals(actual)) {
					final String message = String.format("%s expected %d was %d for [%d] %s", field.name(), expected, actual, i, event.toString());
					result.add(message);
				}
			}
		}

		return result;
	}

	public int getFuelConsumption(final Event event, final Fuel fuel) {
		if (event instanceof FuelUsage) {
			int result = 0;
			final FuelUnit unit = (fuel == Fuel.NBO || fuel == Fuel.FBO) ? FuelUnit.M3 : FuelUnit.MT;

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

	private GroupProfitAndLoss getPnlGroup(final Event event) {
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

	public Long getEventPnl(final Event event) {
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
		final ArrayList<Integer> boundaries = new ArrayList<Integer>();
		int index = 0;
		GroupProfitAndLoss lastGroup = null;

		// find the boundaries at which the PnL group changes
		for (final Event event : events) {
			final GroupProfitAndLoss data = getPnlGroup(event);
			if (data != null && data != lastGroup) {
				boundaries.add(index);
				lastGroup = data;
			}
			index++;
		}

		boundaries.add(index - 1);

		final PnlChunkIndexData[] result = new PnlChunkIndexData[boundaries.size() - 1];
		// construct Pnl chunks from the boundaries
		for (int i = 0; i < boundaries.size() - 1; i++) {
			final int start = boundaries.get(i);
			final int end = boundaries.get(i + 1) - 1;

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
						final Event event = events.get(j);
						final Long pnl = getEventPnl(event);
						if (pnl != null && pnls[i] != null) {
							// if ((int) pnls[i] != pnl.intValue()) {
							// failures.add(String.format("PnL expected %d was %d for [%d] %s", pnls[i], pnl.intValue(), j, event.toString()));
							// }
							// allow small rounding error due to floating point arithmetic
							if (Math.abs((int) pnls[i] - pnl.intValue()) > 1) {
//								failures.add(String.format("PnL expected %d was %d for [%d] %s", pnls[i], pnl.intValue(), j, event.toString()));
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
			final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
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

	public class SequenceTester {
		/** The expected values for each event in the sequence, keyed by expected field */
		private final Map<Expectations, Integer[]> expectedArrays = new HashMap<Expectations, Integer[]>();
		/** The expected PnL values for each PnL group */
		private Integer[] expectedPnlValues = null;
		/** The expected classes of each event in the sequence */
		private Class<?>[] classes;
		/** Information on where each PnL group starts & ends, and whether it is a cargo or not */
		private PnlChunkIndexData[] cargoIndices;

		public float baseFuelPricePerMT = 10;
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
		public void setExpectedValuesIfMatching(final Expectations field, final Class<?> clazz, final Integer[] values) {
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

			setupExpectedPnl(purchasePricePerM3, salesPricePerM3, baseFuelPricePerMT);

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
				fuelCosts[i] = (int) (bfUsage[i] * baseFuelPricePerMT + (fboUsage[i] + nboUsage[i]) * salesPricePerM3);
			}
		}

		private Integer computeExpectedCargoPnl(final PnlChunkIndexData index, final double purchasePricePerM3, final double salesPricePerM3, final double bfPrice) {
			final boolean chargeForLngFuel = !index.isCargo;
			int result = 0;
			for (int i = index.startIndex; i <= index.endIndex; i++) {
				final Integer bfUsage = expectedArrays.get(Expectations.BF_USAGE)[i];
				result -= bfUsage * bfPrice;

				// if (chargeForLngFuel) {
				// final Integer lngUsage = expectedArrays.get(Expectations.NBO_USAGE)[i] + expectedArrays.get(Expectations.FBO_USAGE)[i];
				// result -= lngUsage * salesPricePerM3;
				// }

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

				final Integer heelCost = expectedArrays.get(Expectations.HEEL_COST)[i];
				result -= heelCost;

				final Integer heelRevenue = expectedArrays.get(Expectations.HEEL_REVENUE)[i];
				result += heelRevenue;
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

	public SequenceTester getTesterForVesselEventPostDischarge() {
		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, VesselEventVisit.class,
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

	public SequenceTester getTesterForVesselEventPostDischargeWithEndCooldown() {
		final Class<?>[] classes = { StartEvent.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, SlotVisit.class, Journey.class, Idle.class, VesselEventVisit.class,
				Journey.class, Idle.class, Cooldown.class, EndEvent.class };
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
}
