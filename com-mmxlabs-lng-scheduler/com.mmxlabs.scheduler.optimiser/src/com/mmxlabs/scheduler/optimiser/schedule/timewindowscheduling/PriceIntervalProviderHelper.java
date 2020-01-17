/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateFuelCosts;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper.ApproximateFuelCostLegData;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

public class PriceIntervalProviderHelper {

	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private @NonNull Provider<IPriceIntervalProducer> priceIntervalProducerProvider;

	@Inject
	private @NonNull IVesselProvider vesselProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private SchedulerCalculationUtils schedulerCalculationUtils;

	private final Set<PricingEventType> loadPricingEventTypeSet = new HashSet<>(
			Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_LOAD, PricingEventType.END_OF_LOAD_WINDOW, PricingEventType.START_OF_LOAD, PricingEventType.START_OF_LOAD_WINDOW, }));

	private final Set<PricingEventType> dischargePricingEventTypeSet = new HashSet<>(Arrays.asList(
			new PricingEventType[] { PricingEventType.END_OF_DISCHARGE, PricingEventType.END_OF_DISCHARGE_WINDOW, PricingEventType.START_OF_DISCHARGE, PricingEventType.START_OF_DISCHARGE_WINDOW, }));

	private final @NonNull PriceIntervalsComparator priceIntervalComparator = new PriceIntervalsComparator();

	/**
	 * Compares price intervals based on price
	 */
	private static final class PriceIntervalsComparator implements Comparator<IntervalData> {
		@Override
		public int compare(final IntervalData o1, final IntervalData o2) {
			if (o1 == null || o2 == null) {
				return 0;
			} else {
				return Integer.compare(o1.price, o2.price);
			}
		}
	}

	/**
	 * Produce a list of the difference between purchase and sales price at hour points, when slots in a cargo are not price independent
	 * 
	 * @param start
	 * @param end
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervalProvider
	 * @param dischargePriceIntervalProvider
	 * @param portTimeWindowRecord
	 * @return
	 */
	@NonNull
	public List<int @NonNull []> buildComplexPriceIntervals(final int start, final int end, @NonNull final ILoadOption load, @NonNull final IDischargeOption discharge,
			@NonNull final IPriceIntervalProvider loadPriceIntervalProvider, @NonNull final IPriceIntervalProvider dischargePriceIntervalProvider,
			@NonNull final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int @NonNull [] @NonNull [] intervals = getOverlappingWindows(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, start, end, portTimeWindowRecord);
		final List<int @NonNull []> bestIntervals = new LinkedList<>();
		for (final int @NonNull [] interval : intervals) {
			final int loadPricingTime = shiftTimeByTimezoneToUTC(interval[0], load, portTimeWindowRecord, getPriceEventFromSlotOrContract(load, portTimeWindowRecord));
			final int purchasePrice = getPriceFromLoadOrDischargeCalculator(load, load, discharge, loadPricingTime);
			final int dischargePricingTime = shiftTimeByTimezoneToUTC(interval[0], discharge, portTimeWindowRecord, getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord));
			final int salesPrice = getPriceFromLoadOrDischargeCalculator(discharge, load, discharge, dischargePricingTime);
			final int difference = salesPrice - purchasePrice;
			bestIntervals.add(new int[] { interval[0], difference });
		}
		bestIntervals.add(new int[] { intervals[intervals.length - 1][1], Integer.MIN_VALUE });
		return bestIntervals;
	}

	/**
	 * Produces a list of the time a purchase price changes for a load slot
	 * 
	 * @param slot
	 * @param intervals
	 * @param curve
	 * @param start
	 * @param end
	 * @param offsetInHours
	 * @param portTimeWindowRecord
	 * @return
	 */
	@NonNull
	public List<int @NonNull []> getPriceIntervalsList(final @NonNull ILoadOption slot, final @NonNull IIntegerIntervalCurve intervals, final @Nullable ICurve curve, final int start, final int end,
			final int offsetInHours, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		return buildIntervalsList(slot, intervals, start, end, portTimeWindowRecord);
	}

	/**
	 * Produces a list of the time a sales price changes for a discharge slot
	 * 
	 * @param slot
	 * @param intervals
	 * @param curve
	 * @param start
	 * @param end
	 * @param offsetInHours
	 * @param portTimeWindowRecord
	 * @return
	 */
	public List<int @NonNull []> getPriceIntervalsList(final @NonNull IDischargeOption slot, final @NonNull IIntegerIntervalCurve intervals, final @Nullable ICurve curve, final int start,
			final int end, final int offsetInHours, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {		
		return buildIntervalsList(slot, intervals, start, end, portTimeWindowRecord);
	}

	/**
	 * Provides a list of hours indicating a change in price for a particular slot
	 * 
	 * @param start
	 * @param end
	 * @param slot
	 * @param intervals
	 * @param portTimeWindowsRecord
	 * @return
	 */
	@NonNull
	public List<@NonNull Integer> buildDateChangeCurveAsIntegerList(final int start, final int end, final @NonNull IPortSlot slot, final int @NonNull [] intervals,
			final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO - get rid of PriceEventType (change to int[]?)
		final List<@NonNull Integer> shifted = new LinkedList<>();
		final PricingEventType pricingEventType = getPriceEventTypeFromPortSlot(slot, portTimeWindowsRecord);
		if (isStartOfWindow(pricingEventType)) {
			shifted.add(start);
		} else if (isEndOfWindow(pricingEventType)) {
			shifted.add(start);
		} else if (isPricingDateSpecified(slot, pricingEventType)) {
			shifted.add(start);
		} else {
			shifted.add(start);
			for (int h = 1; h < intervals.length; h++) {
				IPortSlot pricingSlot = slot;
				if (slot instanceof ILoadOption && dischargePricingEventTypeSet.contains(pricingEventType)) {
					pricingSlot = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
				} else if (slot instanceof IDischargeOption && loadPricingEventTypeSet.contains(pricingEventType)) {
					pricingSlot = getFirstLoadOption(portTimeWindowsRecord.getSlots());
				}
				assert pricingSlot != null;
				final int date = getShiftedDate(pricingSlot, portTimeWindowsRecord, intervals[h], pricingEventType);
				if (date < end) {
					shifted.add(date);
				}
			}
		}
		shifted.add(end);
		return shifted;
	}

	public List<int @NonNull []> getEndElementPriceIntervals(@NonNull final List<@NonNull Integer> times) {
		final List<int @NonNull []> intervals = new LinkedList<>();
		for (int i = 0; i < times.size(); i++) {
			intervals.add(new int[] { times.get(i), // time
					0 // 0 for a price of zero (we don't care about price, only cost for end elements
			});
		}
		intervals.add(new int[] { times.get(times.size() - 1) + 1, Integer.MIN_VALUE });
		return intervals;
	}

	int @NonNull [] getCargoBoundsWithCanalTrimming(final int purchaseStart, final int purchaseEnd, final int salesStart, final int salesEnd, final int loadDuration, final int minTimeCanal) {
		final int minSalesStart = getMinDischargeGivenCanal(purchaseStart, salesStart, loadDuration, minTimeCanal);
		return new int[] { purchaseStart, processEndTime(purchaseStart), minSalesStart, processEndTime(Math.max(minSalesStart, salesEnd)) };
	}

	public int getMinDischargeGivenCanal(final int purchaseStart, final int salesStart, final int loadDuration, final int minTimeCanal) {
		final int minSalesStart = Math.max(purchaseStart + minTimeCanal + loadDuration, salesStart);
		return minSalesStart;
	}

	public int[] getIdealLoadAndDischargeTimesGivenCanal(final int purchaseStartInclusive, final int purchaseEndInclusive, int salesStartInclusive, final int salesEndInclusive, final int loadDuration,
			final int canalMaxSpeed, final int canalNBOSpeed) {

		// set min start dates
		int purchase = salesStartInclusive - canalMaxSpeed - loadDuration;
		if (purchase < purchaseStartInclusive) {
			purchase = purchaseStartInclusive;
		}
		if (purchase > purchaseEndInclusive) {
			purchase = purchaseEndInclusive;
		}

		salesStartInclusive = Math.max(purchaseStartInclusive + canalMaxSpeed + loadDuration, salesStartInclusive);
		int discharge = salesStartInclusive;
		{
			// we are able vary our speeds
			final int canalDifference = canalNBOSpeed - canalMaxSpeed;
			if (canalDifference > 0) {
				final int remainder = purchase - purchaseStartInclusive - canalDifference;
				if (remainder >= 0) {
					purchase -= canalDifference;
				} else {
					purchase = purchaseStartInclusive;
					// Recalculate diff as salesStartInclusive may not be based on max canal speed
					final int diff = (purchase + canalNBOSpeed + loadDuration) - salesStartInclusive;
					if (diff > 0) {
						discharge = Math.min(salesStartInclusive + diff, salesEndInclusive); // note: remainder is -ve
					}
				}
			}
		}
		return new int[] { purchase, discharge };
	}

	boolean isFeasibleTravelTime(@NonNull final IntervalData purchase, @NonNull final IntervalData sales, final int loadDuration, final long time) {
		final long minArrivalTime = purchase.start + time + loadDuration;
		if (minArrivalTime <= sales.end) {
			return true;
		} else {
			return false;
		}
	}

	@NonNull
	public LadenRouteData getBestCanalDetails(@NonNull final IntervalData purchase, @NonNull final IntervalData sales, final int loadDuration, @NonNull final LadenRouteData[] sortedCanalTimes) {
		for (final LadenRouteData canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal.ladenTimeAtMaxSpeed)) {
				return canal;
			}
		}
		return sortedCanalTimes[sortedCanalTimes.length - 1];
	}

	public NonNullPair<LadenRouteData, Long> getTotalEstimatedJourneyCost(@NonNull final IntervalData purchase, @NonNull final IntervalData sales, final int loadDuration, final int salesPrice,
			final long charterRatePerDay, @NonNull final LadenRouteData[] sortedCanalTimes, final long boiloffRateM3, final IVessel vessel, final int cv, final boolean isLaden) {
		assert sortedCanalTimes.length > 0;
		final int equivalenceFactor = vessel.getTravelBaseFuel().getEquivalenceFactor();
		long bestMargin = Long.MAX_VALUE;
		LadenRouteData bestCanal = null;

		for (final LadenRouteData canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal.ladenTimeAtMaxSpeed)) {
				final long cost = getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, boiloffRateM3, vessel, cv, equivalenceFactor, canal, charterRatePerDay, isLaden);
				if (cost < bestMargin) {
					bestMargin = cost;
					bestCanal = canal;
				}
			}
		}
		if (bestCanal == null) {
			final long fastest = Long.MAX_VALUE;
			for (final LadenRouteData canal : sortedCanalTimes) {
				if (canal.ladenTimeAtMaxSpeed < fastest) {
					bestCanal = canal;
				}
			}
			if (bestCanal != null) {
				bestMargin = getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, boiloffRateM3, vessel, cv, equivalenceFactor, bestCanal, charterRatePerDay, isLaden);
			}
		}
		assert bestCanal != null;
		return new NonNullPair<LadenRouteData, Long>(bestCanal, bestMargin);
	}

	public long getTotalEstimatedCostForRoute(final IntervalData purchase, final IntervalData sales, final int salesPrice, final int loadDuration, final long boiloffRateM3,
			final @NonNull IVessel vessel, final int cv, final int equivalenceFactor, final LadenRouteData canal, final long charterRatePerDay, final boolean isLaden) {
		final int[] times = getIdealLoadAndDischargeTimesGivenCanal(purchase.start, purchase.end, sales.start, sales.end, loadDuration, (int) canal.ladenTimeAtMaxSpeed,
				(int) canal.ladenTimeAtNBOSpeed);

		ApproximateFuelCostLegData inputData = new ApproximateFuelCostLegData();
		inputData.salesPrice = salesPrice;
		inputData.boiloffRateM3 = boiloffRateM3;
		inputData.vessel = vessel;
		inputData.cv = cv;
		inputData.times = times;
		inputData.distance = canal.ladenRouteDistance;
		inputData.equivalenceFactor = equivalenceFactor;
		inputData.baseFuelPricesPerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, times[0]);
		inputData.canalTransitTime = canal.transitTime;
		inputData.durationAtPort = loadDuration;
		inputData.isLaden = isLaden;
		inputData.forceBaseFuel = false; // not using this here right now
		inputData.includeIdleBunkerCosts = false; // not using this here right now

		ApproximateFuelCosts legFuelCosts = ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
		
		final long charterCost = Calculator.costFromDailyRateAndTimeInHours(charterRatePerDay, times[1] - times[0]);
		final long cost = canal.ladenRouteCost + legFuelCosts.getBoilOffCost() + legFuelCosts.getJourneyBunkerCost() + charterCost;
		return cost;
	}

	int getMinIndexOfPriceIntervalList(final IntervalData @NonNull [] purchaseIntervals) {
		return getMinIndex(purchaseIntervals, priceIntervalComparator);
	}

	private static <T> int getMinIndex(final @NonNull List<? extends T> coll, final @NonNull Comparator<? super T> comp) {
		if (coll.isEmpty()) {
			return -1;
		}
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll.get(0);
		for (final T o : coll) {
			if (comp.compare(o, bestObj) < 0) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}

	private static <T> int getMinIndex(final T @NonNull [] coll, final @NonNull Comparator<? super T> comp) {
		if (coll.length == 0) {
			return -1;
		}
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll[0];
		for (final T o : coll) {
			if (comp.compare(o, bestObj) < 0) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}

	int getMaxIndexOfPriceIntervalList(final IntervalData[] salesIntervals) {
		return getMaxIndex(salesIntervals, priceIntervalComparator);
	}

	private static <T> int getMaxIndex(final @NonNull List<? extends T> coll, final @NonNull Comparator<? super T> comp) {
		if (coll.isEmpty()) {
			return -1;
		}
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll.get(0);
		for (final T o : coll) {
			if (comp.compare(o, bestObj) > 0) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}

	private static <T> int getMaxIndex(final T[] coll, final Comparator<? super T> comp) {
		if (coll.length == 0) {
			return -1;
		}
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll[0];
		for (final T o : coll) {
			if (comp.compare(o, bestObj) > 0) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}

	long @NonNull [] getBestCanalDetails(final long @NonNull [] @NonNull [] times, final int maxTime) {
		for (final long[] canal : times) {
			if (maxTime >= canal[0]) {
				return canal;
			}
		}
		return times[times.length];
	}

	int getMinimumPossibleTimeForCargoIntervals(final @NonNull List<int[]> loadPriceIntervals, final @NonNull List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(0)[0] - loadPriceIntervals.get(loadPriceIntervals.size() - 1)[0];
	}

	int getMaximumPossibleTimeForCargoIntervals(final @NonNull List<int[]> loadPriceIntervals, final @NonNull List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(dischargePriceIntervals.size() - 1)[0] - loadPriceIntervals.get(0)[0] - 1;
	}

	public int getMinTravelTimeAtMaxSpeed(@NonNull final LadenRouteData @NonNull [] canalTimes) {
		int min = Integer.MAX_VALUE;
		for (final LadenRouteData ladenRouteData : canalTimes) {
			if (ladenRouteData.ladenTimeAtMaxSpeed < min) {
				min = (int) ladenRouteData.ladenTimeAtMaxSpeed;
			}
		}
		return min;
	}

	@Nullable
	IVessel getVessel(@NonNull final IResource resource) {
		final IVesselAvailability availability = vesselProvider.getVesselAvailability(resource);
		if (availability == null) {
			return null;
		}
		final IVessel vessel = availability.getVessel();
		if (vessel == null) {
			return null;
		}
		return vessel;
	}

	/**
	 * Produce a list of the difference between purchase and sales price at hour points, when slots in a cargo are not price independent
	 * 
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervalProvider
	 * @param dischargePriceIntervalProvider
	 * @param portTimeWindowRecord
	 * @param dateFromLoad
	 * @return
	 */
	@NonNull
	List<int @NonNull []> getComplexPriceIntervals(final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge, final @NonNull IPriceIntervalProvider loadPriceIntervalProvider,
			final @NonNull IPriceIntervalProvider dischargePriceIntervalProvider, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final boolean dateFromLoad) {
		ITimeWindow timeWindow;
		if (dateFromLoad) {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		} else {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		}

		final List<int @NonNull []> intervals = getFeasibleIntervalSubSet(timeWindow.getInclusiveStart(), timeWindow.getExclusiveEnd(),
				priceIntervalProducerProvider.get().getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
						(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, dateFromLoad));
		return intervals;
	}

	@NonNull
	List<int @NonNull []> getFeasibleIntervalSubSet(final int inclusiveStart, final int exclusiveEnd, @NonNull final List<int @NonNull []> intervals) {
		final List<int @NonNull []> list = new LinkedList<>();
		for (int i = 0; i < intervals.size(); i++) {
			if (list.isEmpty()) {
				// first interval
				if (intervals.get(i)[0] == inclusiveStart) {
					// easy case
					list.add(new int[] { inclusiveStart, intervals.get(i)[1] });
				} else if (intervals.get(i)[0] > inclusiveStart) {
					// start is less than the start of the interval
					if (i > 0) {
						// create trimmed forward interval
						list.add(new int[] { inclusiveStart, intervals.get(i - 1)[1] });
						if (intervals.get(i)[0] < exclusiveEnd) {
							list.add(intervals.get(i));
						}
					} else {
						// no previous interval, so use current
						list.add(new int[] { inclusiveStart, intervals.get(i)[1] });
					}
				}
			} else if (intervals.get(i)[0] < exclusiveEnd) {
				list.add(intervals.get(i));
			}
		}
		if (list.isEmpty()) {
			// start is greater than all intervals we have in the list
			list.add(new int[] { inclusiveStart, intervals.get(intervals.size() - 1)[1] });
		}
		list.add(new int[] { exclusiveEnd, Integer.MIN_VALUE });
		return list;
	}

	int @NonNull [] @NonNull [] getOverlappingWindows(final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge, final @NonNull IPriceIntervalProvider loadPriceIntervalProvider,
			final @NonNull IPriceIntervalProvider dischargePriceIntervalProvider, final int start, final int end, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final List<Integer> loadIntervals = loadPriceIntervalProvider.getPriceHourIntervals(load, start, end, portTimeWindowsRecord);
		final List<Integer> dischargeIntervals = dischargePriceIntervalProvider.getPriceHourIntervals(discharge, start, end, portTimeWindowsRecord);
		final IntegerIntervalCurve integerIntervalCurve = new IntegerIntervalCurve();
		integerIntervalCurve.addAll(loadIntervals);
		integerIntervalCurve.addAll(dischargeIntervals);
		return integerIntervalCurve.getIntervalsAs2dArray(start, end);
	}

	@NonNull
	PricingEventType getPriceEventTypeFromPortSlot(final @NonNull IPortSlot slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final PricingEventType pricingEventType = (slot instanceof ILoadOption) ? getPriceEventFromSlotOrContract((ILoadOption) slot, portTimeWindowsRecord)
				: getPriceEventFromSlotOrContract((IDischargeOption) slot, portTimeWindowsRecord);
		return pricingEventType;
	}

	@NonNull
	Pair<ILoadOption, IDischargeOption> getLoadAndDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, @NonNull final List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData) {
		ILoadOption load = null;
		IDischargeOption discharge = null;
		for (final Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
			if (ILoadOption.class.isAssignableFrom(slotRow.getSecond())) {
				load = (ILoadOption) slotRow.getFirst();
			} else if (IDischargeOption.class.isAssignableFrom(slotRow.getSecond())) {
				discharge = (IDischargeOption) slotRow.getFirst();
			}
		}
		return new Pair<ILoadOption, IDischargeOption>(load, discharge);
	}

	boolean isLoadPricingEventTime(final @NonNull ILoadOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		@Nullable
		final PricingEventType pricingEventType = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		final PricingEventType pet = pricingEventType == null ? slot.getPricingEvent() : pricingEventType;
		return isLoadPricingEventTime(pet);
	}

	private boolean isLoadPricingEventTime(final @NonNull PricingEventType pet) {
		if (loadPricingEventTypeSet.contains(pet)) {
			return true;
		}
		return false;
	}

	boolean isLoadPricingEventTime(final @NonNull IDischargeOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final PricingEventType pet = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent()
				: slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isLoadPricingEventTime(pet);
	}

	boolean isDischargePricingEventTime(final @NonNull ILoadOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final PricingEventType pet = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent()
				: slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isDischargePricingEventTime(pet);
	}

	boolean isDischargePricingEventTime(final @NonNull IDischargeOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final PricingEventType pet = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent()
				: slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isDischargePricingEventTime(pet);
	}

	private boolean isDischargePricingEventTime(final PricingEventType pet) {
		if (dischargePricingEventTypeSet.contains(pet)) {
			return true;
		}
		return false;
	}

	/**
	 * Build a list of price intervals.
	 * @param slot - the slot we are building the price intervals for (also where we get the price calculator + price curve from).
	 * @param intervals - a list of time points to consider.
	 * @param start - the start of the time range to consider.
	 * @param end - the end of the time range to consider.
	 * @param portTimeWindowsRecord - the port times window record for the cargo which slot is part of.
	 * @return a list of price intervals with the first element of the integer array the start of the bucket, and the second element the price.
	 */
	@NonNull
	List<int @NonNull []> buildIntervalsList(final @NonNull IPortSlot slot, final @NonNull IIntegerIntervalCurve intervals, final int start, final int end,
			final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		final List<int @NonNull []> priceIntervals = new LinkedList<>();

		final ILoadOption loadOption = getFirstLoadOption(portTimeWindowsRecord.getSlots());
		assert loadOption != null;

		final IDischargeOption dischargeOption = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
		assert dischargeOption != null;

		final PricingEventType pricingEventType = getPriceEventTypeFromPortSlot(slot, portTimeWindowsRecord);

		final int startUTCEquiv = shiftTimeByTimezoneToUTC(start, slot, portTimeWindowsRecord, pricingEventType);
		final int endUTCEquiv = shiftTimeByTimezoneToUTC(end, slot, portTimeWindowsRecord, pricingEventType);

		final int[] hourIntervals = intervals.getIntervalRange(startUTCEquiv, endUTCEquiv);
		int transferDate = start;
		if (isStartOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, startUTCEquiv) });
			priceIntervals.add(getEndInterval(end));
		} else if (isEndOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, endUTCEquiv) });
			priceIntervals.add(getEndInterval(end));
		} else if (isPricingDateSpecified(slot, pricingEventType)) {
			final int slotPricingDate = getDateFromSlotOrContract(slot, portTimeWindowsRecord);
			assert slotPricingDate != IPortSlot.NO_PRICING_DATE;
			final int slotPricingDateUTCEquiv = timeZoneToUtcOffsetProvider.UTC(slotPricingDate, slot.getPort());
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, slotPricingDateUTCEquiv) });
			priceIntervals.add(getEndInterval(end));
		} else {
			// first add start
			int slotDuration = getDuration(slot, portTimeWindowsRecord);
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption,
					shiftTimeByTimezoneToUTC(start + (isEndOfEvent(pricingEventType) ? slotDuration : 0), slot, portTimeWindowsRecord, pricingEventType)) });
			for (int h = 1; h < hourIntervals.length - 1; h++) {
				final int date = hourIntervals[h];
				if (isStartOfEvent(pricingEventType)) {
					transferDate = date;
				} else if (isEndOfEvent(pricingEventType)) {
					transferDate = date;
				}
				final int windowDate = shiftTimeByTimezoneToLocalTime(transferDate, slot, portTimeWindowsRecord, pricingEventType);
				if (windowDate < end && windowDate > start) {
					priceIntervals.add(new int[] { windowDate, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, date + (isEndOfEvent(pricingEventType) ? slotDuration : 0)) });
				}
			}
			priceIntervals.add(getEndInterval(end));
		}
		return priceIntervals;
	}

	void createAndSetTimeWindow(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IPortSlot slot, final int start, final int end) {
		final ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(slot);
		final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(start, end, timeWindow.getExclusiveEndFlex());
		portTimeWindowRecord.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
	}

	private int shiftTimeByTimezoneToUTC(final int time, final @NonNull IPortSlot slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull PricingEventType pricingEventType) {
		IPortSlot slotToUse;
		if (slot instanceof ILoadOption) {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = slot;
			} else {
				slotToUse = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
			}
		} else {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = getFirstLoadOption(portTimeWindowsRecord.getSlots());
			} else {
				slotToUse = slot;
			}
		}
		assert slotToUse != null;
		final int shifted = timeZoneToUtcOffsetProvider.UTC(time, slotToUse.getPort());
		return shifted;
	}

	private int shiftTimeByTimezoneToLocalTime(final int time, final @NonNull IPortSlot slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord,
			@NonNull final PricingEventType pricingEventType) {
		IPortSlot slotToUse;
		if (slot instanceof ILoadOption) {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = slot;
			} else {
				slotToUse = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
			}
		} else {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = getFirstLoadOption(portTimeWindowsRecord.getSlots());
			} else {
				slotToUse = slot;
			}
		}
		assert slotToUse != null;
		return timeZoneToUtcOffsetProvider.localTime(time, slotToUse.getPort());
	}

	private int getDateFromSlotOrContract(final @NonNull IPortSlot slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		int date = IPortSlot.NO_PRICING_DATE;
		if (slot instanceof ILoadOption) {
			if (((ILoadOption) slot).getLoadPriceCalculator().getCalculatorPricingEventType((ILoadOption) slot, portTimeWindowsRecord) == PricingEventType.DATE_SPECIFIED) {
				date = ((ILoadOption) slot).getLoadPriceCalculator().getCalculatorPricingDate((ILoadOption) slot, portTimeWindowsRecord);
			} else {
				date = ((ILoadOption) slot).getPricingDate();
			}
		} else if (slot instanceof IDischargeOption) {
			if (((IDischargeOption) slot).getDischargePriceCalculator().getCalculatorPricingEventType((IDischargeOption) slot, portTimeWindowsRecord) == PricingEventType.DATE_SPECIFIED) {
				date = ((IDischargeOption) slot).getDischargePriceCalculator().getCalculatorPricingDate((IDischargeOption) slot, portTimeWindowsRecord);
			} else {
				date = ((IDischargeOption) slot).getPricingDate();
			}
		}
		return date;
	}

	boolean isPricingDateSpecified(final @NonNull IPortSlot portSlot, final @NonNull PricingEventType pricingEventType) {
		if (portSlot instanceof ILoadOption) {
			return (pricingEventType == PricingEventType.DATE_SPECIFIED || ((ILoadOption) portSlot).getPricingDate() != IPortSlot.NO_PRICING_DATE);
		} else if (portSlot instanceof IDischargeOption) {
			return (pricingEventType == PricingEventType.DATE_SPECIFIED || ((IDischargeOption) portSlot).getPricingDate() != IPortSlot.NO_PRICING_DATE);
		}
		return false;
	}

	private int getPriceFromLoadOrDischargeCalculator(final @NonNull IPortSlot slot, @NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption, final int timeInHours) {
		if (slot instanceof ILoadOption) {
			return ((ILoadOption) slot).getLoadPriceCalculator().getEstimatedPurchasePrice(loadOption, dischargeOption, timeInHours);
		} else if (slot instanceof IDischargeOption) {
			return ((IDischargeOption) slot).getDischargePriceCalculator().getEstimatedSalesPrice(loadOption, dischargeOption, timeInHours);
		} else {
			throw new IllegalStateException("A price can only be obtained from a load or discharge option");
		}
	}

	@NonNull
	static List<Integer> getFixedStartEndIntervals(final int start, final int end) {
		final List<Integer> priceIntervals = new LinkedList<Integer>();
		priceIntervals.add(start);
		priceIntervals.add(end);
		return priceIntervals;
	}

	private int getShiftedDate(final @NonNull IPortSlot slot, final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final int date, final @NonNull PricingEventType pricingEventType) {
		int transferDate = date;
		if (isStartOfEvent(pricingEventType)) {
			transferDate = date; // no change
		} else if (isEndOfEvent(pricingEventType)) {
			final int duration = getDuration(slot, portTimeWindowsRecord);
			transferDate = date - duration; // note: remove offset
		}
		return timeZoneToUtcOffsetProvider.localTime(transferDate, slot.getPort());
	}

	private int getDuration(@NonNull final IPortSlot slot, @NonNull final IPortTimeWindowsRecord portTimeWindowsRecord) {
		int duration = 0;
		if (slot instanceof ILoadOption && isLoadPricingEventTime((ILoadOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(slot);
		} else if (slot instanceof ILoadOption && isDischargePricingEventTime((ILoadOption) slot, portTimeWindowsRecord)) {
			final IDischargeOption firstDischargeOption = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
			assert firstDischargeOption != null;
			duration = portTimeWindowsRecord.getSlotDuration(firstDischargeOption);
		} else if (slot instanceof IDischargeOption && isDischargePricingEventTime((IDischargeOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(slot);
		} else if (slot instanceof IDischargeOption && isLoadPricingEventTime((IDischargeOption) slot, portTimeWindowsRecord)) {
			final ILoadOption firstLoadOption = getFirstLoadOption(portTimeWindowsRecord.getSlots());
			assert firstLoadOption != null;
			duration = portTimeWindowsRecord.getSlotDuration(firstLoadOption);
		}
		return duration;
	}

	public static final int @NonNull [] getEndInterval(final int end) {
		return new int[] { end, Integer.MIN_VALUE };
	}

	public static boolean isStartOfEvent(final @NonNull PricingEventType pet) {
		if (pet == PricingEventType.START_OF_LOAD || pet == PricingEventType.START_OF_DISCHARGE) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfEvent(final @NonNull PricingEventType pet) {
		if (pet == PricingEventType.END_OF_LOAD || pet == PricingEventType.END_OF_DISCHARGE) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfWindow(final @NonNull PricingEventType pet) {
		if (pet == PricingEventType.END_OF_LOAD_WINDOW || pet == PricingEventType.END_OF_DISCHARGE_WINDOW) {
			return true;
		}
		return false;
	}

	public static boolean isStartOfWindow(final PricingEventType pet) {
		if (pet == PricingEventType.START_OF_LOAD_WINDOW || pet == PricingEventType.START_OF_DISCHARGE_WINDOW) {
			return true;
		}
		return false;
	}

	@NonNull
	public static PricingEventType getPriceEventFromSlotOrContract(final @NonNull ILoadOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		@Nullable
		final PricingEventType pricingEventType = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowRecord);

		return pricingEventType == null ? slot.getPricingEvent() : pricingEventType;
	}

	@NonNull
	public static PricingEventType getPriceEventFromSlotOrContract(final @NonNull IDischargeOption slot, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		final PricingEventType pricingEventType = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowRecord);
		return pricingEventType == null ? slot.getPricingEvent() : pricingEventType;
	}

	/**
	 * Provides the date range for the highest price period in a given range
	 * 
	 * @return
	 */
	@NonNull
	public Pair<Integer, Integer> getHighestPriceInterval(final List<int[]> intervals) {
		int start = -1;
		int end = -1;
		int best = -Integer.MAX_VALUE;
		for (int i = 0; i < intervals.size(); i++) {
			final int[] currInterval = intervals.get(i);
			final int price = currInterval[1];
			if (price != Integer.MIN_VALUE && price > best) {
				start = currInterval[0];
				end = intervals.get(i + 1)[0];
				best = price;
			}
		}
		return new Pair<>(start, getEndInterval(start, end));
	}

	/**
	 * Provides the date range for the highest price period in a given range
	 * 
	 * @return
	 */
	@NonNull
	public Pair<Integer, Integer> getHighestPriceInterval(final @NonNull IPriceIntervalProvider priceIntervalProvider, final int startOfRange, final int endOfRange, final @NonNull IPortSlot slot,
			final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		return getHighestPriceInterval(priceIntervalProvider.getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
	}

	/**
	 * Provides the date range for the lowest price period in a given range
	 * 
	 * @return
	 */
	@NonNull
	public Pair<@NonNull Integer, @NonNull Integer> getLowestPriceInterval(final List<int @NonNull []> intervals) {
		int start = -1;
		int end = -1;
		int best = Integer.MAX_VALUE;
		for (int i = 0; i < intervals.size(); i++) {
			final int[] currInterval = intervals.get(i);
			final int price = currInterval[1];
			if (price != Integer.MIN_VALUE && price < best) {
				start = currInterval[0];
				end = intervals.get(i + 1)[0];
				best = price;
			}
		}
		return new Pair<>(start, getEndInterval(start, end));
	}

	/**
	 * Provides the date range and price for the lowest price period in a given range
	 * 
	 * @return
	 */
	public @NonNull IntervalData @NonNull [] getIntervalsBoundsAndPrices(final List<int[]> intervals) {
		final @NonNull IntervalData @NonNull [] sortedIntervals = new @NonNull IntervalData @NonNull [intervals.size() - 1];
		for (int i = 0; i < intervals.size() - 1; i++) {
			sortedIntervals[i] = new IntervalData(intervals.get(i)[0], getEndInterval(intervals.get(i)[0], intervals.get(i + 1)[0]), intervals.get(i)[1]);
		}
		return sortedIntervals;
	}

	/**
	 * Provides the date range and price for the lowest price period in a given range
	 * 
	 * @return
	 */

	public @NonNull IntervalData @NonNull [] getIntervalsBoundsAndPricesWithNoRange(final List<int @NonNull []> intervals) {
		final @NonNull IntervalData @NonNull [] sortedIntervals = new @NonNull IntervalData @NonNull [intervals.size() - 1];
		for (int i = 0; i < intervals.size() - 1; i++) {
			sortedIntervals[i] = new IntervalData(intervals.get(i)[0], intervals.get(i)[0], intervals.get(i)[1]);
		}
		return sortedIntervals;
	}

	public int getEndInterval(final int intervalDataStart, final int intervalDataEnd) {
		if (intervalDataStart == intervalDataEnd) {
			return intervalDataStart;
		} else {
			return intervalDataEnd - 1;
		}
	}

	/**
	 * Provides the date range for the lowest price period in a given range
	 * 
	 * @return
	 */
	@NonNull
	public Pair<Integer, Integer> getLowestPriceInterval(final @NonNull IPriceIntervalProvider priceIntervalProvider, final int startOfRange, final int endOfRange, final @NonNull IPortSlot slot,
			final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		return getLowestPriceInterval(priceIntervalProvider.getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
	}

	@Nullable
	public ILoadOption getFirstLoadOption(final List<@NonNull IPortSlot> slots) {
		for (final IPortSlot slot : slots) {
			if (ILoadOption.class.isAssignableFrom(slot.getClass())) {
				return (ILoadOption) slot;
			}
		}
		return null;
	}

	@Nullable
	public IDischargeOption getFirstDischargeOption(@NonNull final List<IPortSlot> slots) {
		for (final IPortSlot slot : slots) {
			if (IDischargeOption.class.isAssignableFrom(slot.getClass())) {
				return (IDischargeOption) slot;
			}
		}
		return null;
	}

	public boolean isPricingEventTypeLoad(@NonNull final PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_LOAD || pricingEventType == PricingEventType.END_OF_LOAD) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPricingEventTypeDischarge(@NonNull final PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_DISCHARGE || pricingEventType == PricingEventType.END_OF_DISCHARGE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Makes sure the end of a time window is in the proper format. Takes as input the inclusive end and outputs the correct format.
	 * 
	 * @param end
	 * @return
	 */
	public int processEndTime(final int inclusiveEnd) {
		// make exclusive end
		return inclusiveEnd + 1;
	}

	public long getCharterRateForTimePickingDecision(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IPortTimeWindowsRecord portTimeWindowRecordStart,
			@NonNull final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER || vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			if (portTimeWindowRecord == portTimeWindowRecordStart) {
				return schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselAvailability, portTimeWindowRecord.getFirstSlotFeasibleTimeWindow().getInclusiveStart());
			}
		}
		return 0L;
	}
}
