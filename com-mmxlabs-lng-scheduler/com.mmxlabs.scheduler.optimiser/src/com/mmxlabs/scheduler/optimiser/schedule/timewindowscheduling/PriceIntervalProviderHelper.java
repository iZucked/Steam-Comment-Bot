/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class PriceIntervalProviderHelper {

	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private @NonNull Provider<IPriceIntervalProducer> priceIntervalProducerProvider;

	@Inject
	private @NonNull IVesselProvider vesselProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	private final Set<PricingEventType> loadPricingEventTypeSet = new HashSet<>(
			Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_LOAD, PricingEventType.END_OF_LOAD_WINDOW, PricingEventType.START_OF_LOAD, PricingEventType.START_OF_LOAD_WINDOW, }));

	private final Set<PricingEventType> dischargePricingEventTypeSet = new HashSet<>(Arrays.asList(
			new PricingEventType[] { PricingEventType.END_OF_DISCHARGE, PricingEventType.END_OF_DISCHARGE_WINDOW, PricingEventType.START_OF_DISCHARGE, PricingEventType.START_OF_DISCHARGE_WINDOW, }));

	private final @NonNull PriceIntervalsComparator priceIntervalComparator = new PriceIntervalsComparator();

	private final static int TOTAL_BOILOFF_COSTS_INDEX = 0;
	private final static int TOTAL_BUNKER_COSTS_INDEX = 1;

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
	public List<int[]> buildComplexPriceIntervals(final int start, final int end, @NonNull final ILoadOption load, @NonNull final IDischargeOption discharge,
			@NonNull final IPriceIntervalProvider loadPriceIntervalProvider, @NonNull final IPriceIntervalProvider dischargePriceIntervalProvider,
			@NonNull final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int[][] intervals = getOverlappingWindows(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, start, end, portTimeWindowRecord);
		final List<int[]> bestIntervals = new LinkedList<>();
		for (final int[] interval : intervals) {
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
		final List<int @NonNull []> priceIntervals = new LinkedList<>();
		buildIntervalsList(slot, intervals, curve, start, end, portTimeWindowRecord, priceIntervals);
		return priceIntervals;
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
		final List<int @NonNull []> priceIntervals = new LinkedList<>();
		buildIntervalsList(slot, intervals, curve, start, end, portTimeWindowRecord, priceIntervals);
		return priceIntervals;
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

	public int[] getIdealLoadAndDischargeTimesGivenCanal(final int purchaseStart, final int purchaseEnd, int salesStart, final int salesEnd, final int loadDuration, final int canalMaxSpeed,
			final int canalNBOSpeed) {
		// set min start dates
		int purchase = salesStart - canalMaxSpeed - loadDuration;
		if (purchase < purchaseStart) {
			salesStart = purchaseStart + canalMaxSpeed + loadDuration;
			purchase = purchaseStart;
		}
		int discharge = salesStart;
		if (purchase > purchaseEnd) {
			purchase = purchaseEnd;
			discharge = Math.min(Math.max(purchaseStart + canalNBOSpeed + loadDuration, salesStart), salesEnd);
		} else {
			// we are able vary our speeds
			final int canalDifference = canalNBOSpeed - canalMaxSpeed;
			final int remainder = purchase - purchaseStart - canalDifference;
			if (remainder >= 0) {
				purchase -= canalDifference;
			} else {
				purchase = purchaseStart;
				discharge = Math.min(salesStart - remainder, salesEnd); // note: remainder is -ve
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
			final long charterRatePerDay, @NonNull final LadenRouteData[] sortedCanalTimes, final long boiloffRateM3, final IVesselClass vesselClass, final int cv, final boolean isLaden) {
		assert sortedCanalTimes.length > 0;
		final int equivalenceFactor = vesselClass.getBaseFuel().getEquivalenceFactor();
		long bestMargin = Long.MAX_VALUE;
		LadenRouteData bestCanal = null;

		for (final LadenRouteData canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal.ladenTimeAtMaxSpeed)) {
				final long cost = getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, boiloffRateM3, vesselClass, cv, equivalenceFactor, canal, charterRatePerDay, isLaden);
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
				bestMargin = getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, boiloffRateM3, vesselClass, cv, equivalenceFactor, bestCanal, charterRatePerDay, isLaden);
			}
		}
		assert bestCanal != null;
		return new NonNullPair<LadenRouteData, Long>(bestCanal, bestMargin);
	}

	public long getTotalEstimatedCostForRoute(final IntervalData purchase, final IntervalData sales, final int salesPrice, final int loadDuration, final long boiloffRateM3,
			final IVesselClass vesselClass, final int cv, final int equivalenceFactor, final LadenRouteData canal, final long charterRatePerDay, final boolean isLaden) {
		final int[] times = getIdealLoadAndDischargeTimesGivenCanal(purchase.start, purchase.end, sales.start, sales.end, loadDuration, (int) canal.ladenTimeAtMaxSpeed,
				(int) canal.ladenTimeAtNBOSpeed);
		final long[] fuelCosts = getLegFuelCosts(salesPrice, boiloffRateM3, vesselClass, cv, times, canal.ladenRouteDistance, equivalenceFactor,
				vesselBaseFuelCalculator.getBaseFuelPrice(vesselClass, times[0]), canal.transitTime, loadDuration, isLaden);

		final long charterCost = OptimiserUnitConvertor.convertToInternalDailyCost((charterRatePerDay * (long) (times[1] - times[0])) / 24L); // note: converting charter rate to same scale as fuel
																																				// costs
		final long cost = canal.ladenRouteCost + fuelCosts[TOTAL_BOILOFF_COSTS_INDEX] + fuelCosts[TOTAL_BUNKER_COSTS_INDEX] + charterCost;
		return cost;
	}

	/**
	 * Calculates approximate fuel costs. Assumptions: does not calculate the in port costs, route costs or idle bunkers
	 * 
	 * @param salesPrice
	 * @param boiloffRateM3
	 * @param vesselClass
	 * @param cv
	 * @param times
	 * @param distance
	 * @param equivalenceFactor
	 * @param baseFuelPrice
	 * @param canalTransitTime
	 * @param durationAtPort
	 * @param isLaden
	 *            TODO
	 * @return
	 */
	public long[] getLegFuelCosts(final int salesPrice, final long boiloffRateM3, final IVesselClass vesselClass, final int cv, final int[] times, final long distance, final int equivalenceFactor,
			final int baseFuelPrice, final int canalTransitTime, final int durationAtPort, final boolean isLaden) {
		final VesselState vesselState;
		if (isLaden) {
			vesselState = VesselState.Laden;
		} else {
			vesselState = VesselState.Ballast;
		}
		final int totalLegLengthInHours = (times[1] - times[0] - durationAtPort - canalTransitTime);

		// estimate speed and rate
		final int nboSpeed = vesselClass.getConsumptionRate(vesselState).getSpeed(Calculator.convertM3ToMT(boiloffRateM3, cv, equivalenceFactor));
		if (totalLegLengthInHours == 0) {
			final int z = 0;
		}
		final int naturalSpeed = Calculator.speedFromDistanceTime(distance, totalLegLengthInHours);
		final int speed = Math.min(Math.max(nboSpeed, naturalSpeed), vesselClass.getMaxSpeed()); // the speed bounded by NBO and Max
		final long rate = vesselClass.getConsumptionRate(vesselState).getRate(speed);
		final long vesselTravelTimeInHours = Calculator.getTimeFromSpeedDistance(speed, distance);
		final long idleTimeInHours = Math.max(totalLegLengthInHours - vesselTravelTimeInHours, 0) + canalTransitTime; // note: modelling canal time as idle

		final long requiredTotalFuelMT = (rate * vesselTravelTimeInHours) / 24L;
		final long requiredTotalFuelM3 = Calculator.convertMTToM3(requiredTotalFuelMT, cv, equivalenceFactor);
		final long requiredTotalFuelMMBTu = Calculator.convertM3ToMMBTu(requiredTotalFuelM3, cv);

		final long idleBoiloffMMBTU = Calculator.convertM3ToMMBTu(vesselClass.getIdleNBORate(vesselState) * (int) idleTimeInHours, cv) / 24L;
		final long idleBoiloffCost = Calculator.costFromVolume(idleBoiloffMMBTU, salesPrice);

		final long[] fuelCosts = new long[2];
		final long totalBoilOffCost;
		final long totalBunkerCost;

		if (vesselClass.hasReliqCapability()) {
			totalBoilOffCost = Calculator.costFromVolume(requiredTotalFuelMMBTu, salesPrice);
			totalBunkerCost = 0L;
		} else {
			final long boiloffM3 = (vesselTravelTimeInHours * boiloffRateM3) / 24L;
			final long boiloffMMBTU = Calculator.convertM3ToMMBTu((vesselTravelTimeInHours * boiloffRateM3) / 24, cv);
			final long boiloffMT = Calculator.convertM3ToMT(boiloffM3, cv, equivalenceFactor);
			final long boiloffCost = Calculator.costFromVolume(boiloffMMBTU, salesPrice);
			final long boiloffCostMMBTU = boiloffCost;

			final long bunkersNeededMT = Math.max(0, requiredTotalFuelMT - boiloffMT);
			final long bunkersCost = Calculator.costFromConsumption(bunkersNeededMT, baseFuelPrice);

			final long fboInMMBTU = Math.max(0, requiredTotalFuelMMBTu - boiloffMMBTU);
			final long fboCost = Calculator.costFromVolume(fboInMMBTU, salesPrice);

			if (fboCost > bunkersCost) {
				totalBoilOffCost = boiloffCostMMBTU;
				totalBunkerCost = bunkersCost;
			} else {
				totalBoilOffCost = Calculator.costFromVolume(requiredTotalFuelMMBTu, salesPrice);
				totalBunkerCost = 0L;
			}
		}
		// note converting units so same scale as charter rate
		fuelCosts[0] = OptimiserUnitConvertor.convertToInternalDailyCost(totalBoilOffCost + idleBoiloffCost);
		fuelCosts[1] = OptimiserUnitConvertor.convertToInternalDailyCost(totalBunkerCost);
		return fuelCosts;
	}

	public NonNullPair<LadenRouteData, Long> getBestCanalDetailsWithBoiloff(@NonNull final IntervalData purchase, @NonNull final IntervalData sales, final int loadDuration, final int salesPrice,
			@NonNull final LadenRouteData[] sortedCanalTimes, final long boiloffRateM3, final int cv, final int loadVolumeMMBTU) {
		assert sortedCanalTimes.length > 0;
		long bestMargin = Long.MAX_VALUE;
		long bestBoiloffCostMMBTU = Long.MIN_VALUE;
		LadenRouteData bestCanal = null;
		for (final LadenRouteData canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal.ladenTimeAtMaxSpeed)) {
				final int[] times = getIdealLoadAndDischargeTimesGivenCanal(purchase.start, purchase.end, sales.start, sales.end, loadDuration, (int) canal.ladenTimeAtMaxSpeed,
						(int) canal.ladenTimeAtNBOSpeed);
				final int ladenLength = (times[1] - times[0]) / 24;

				final long boiloffMMBTU = Calculator.convertM3ToMMBTu((ladenLength) * boiloffRateM3, cv);
				final long boiloffCost = Calculator.costFromVolume(boiloffMMBTU, sales.price);
				final long boiloffCostMMBTU = OptimiserUnitConvertor.convertToInternalDailyCost(boiloffCost);
				final long charterCost = 0;
				final long cost = canal.ladenRouteCost + boiloffCostMMBTU + charterCost;
				if (cost < bestMargin) {
					bestMargin = cost;
					bestCanal = canal;
					bestBoiloffCostMMBTU = boiloffCostMMBTU;
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
				final int[] times = getIdealLoadAndDischargeTimesGivenCanal(purchase.start, purchase.end, sales.start, sales.end, loadDuration, (int) bestCanal.ladenTimeAtMaxSpeed,
						(int) bestCanal.ladenTimeAtMaxSpeed);
				final int ladenLength = (times[1] - times[0]) / 24;
				final long boiloffMMBTU = Calculator.convertM3ToMMBTu((ladenLength) * boiloffRateM3, cv);
				final long boiloffCost = Calculator.costFromVolume(boiloffMMBTU, sales.price);
				bestBoiloffCostMMBTU = OptimiserUnitConvertor.convertToInternalDailyCost(boiloffCost);
			}
		}
		assert bestCanal != null;
		return new NonNullPair<>(bestCanal, bestBoiloffCostMMBTU);
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
			if (list.size() == 0) {
				if (intervals.get(i)[0] == inclusiveStart) {
					list.add(new int[] { inclusiveStart, intervals.get(i)[1] });
				} else if (intervals.get(i)[0] > inclusiveStart) {
					list.add(new int[] { inclusiveStart, intervals.get(i - 1)[1] });
					if (intervals.get(i)[0] < exclusiveEnd) {
						list.add(intervals.get(i));
					}
				}
			} else if (intervals.get(i)[0] < exclusiveEnd) {
				list.add(intervals.get(i));
			}
		}
		if (list.size() == 0) {
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

	@NonNull
	List<int @NonNull []> buildIntervalsList(final @NonNull IPortSlot slot, final @NonNull IIntegerIntervalCurve intervals, final @Nullable ICurve curve, final int start, final int end,
			final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, @NonNull final List<int @NonNull []> priceIntervals) {

		final ILoadOption loadOption = getFirstLoadOption(portTimeWindowsRecord.getSlots());
		assert loadOption != null;

		final IDischargeOption dischargeOption = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
		assert dischargeOption != null;

		final PricingEventType pricingEventType = getPriceEventTypeFromPortSlot(slot, portTimeWindowsRecord);

		final int[] hourIntervals = intervals.getIntervalRange(start, end);
		int transferDate = start;
		if (isStartOfWindow(pricingEventType)) {
			priceIntervals
					.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, shiftTimeByTimezoneToUTC(start, slot, portTimeWindowsRecord, pricingEventType)) });
			priceIntervals.add(getEndInterval(end));
		} else if (isEndOfWindow(pricingEventType)) {
			priceIntervals
					.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, shiftTimeByTimezoneToUTC(end, slot, portTimeWindowsRecord, pricingEventType)) });
			priceIntervals.add(getEndInterval(end));
		} else if (isPricingDateSpecified(slot, pricingEventType)) {
			final int slotPricingDate = getDateFromSlotOrContract(slot, portTimeWindowsRecord);
			assert slotPricingDate != IPortSlot.NO_PRICING_DATE;
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, timeZoneToUtcOffsetProvider.UTC(slotPricingDate, slot.getPort())) });
			priceIntervals.add(getEndInterval(end));
		} else {
			// first add start
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption,
					shiftTimeByTimezoneToUTC(start + (isEndOfEvent(pricingEventType) ? getDuration(slot, portTimeWindowsRecord) : 0), slot, portTimeWindowsRecord, pricingEventType)) });
			for (int h = 1; h < hourIntervals.length - 1; h++) {
				final int date = hourIntervals[h];
				if (isStartOfEvent(pricingEventType)) {
					transferDate = date;
				} else if (isEndOfEvent(pricingEventType)) {
					transferDate = date - getDuration(slot, portTimeWindowsRecord);
				}
				final int windowDate = shiftTimeByTimezoneToLocalTime(transferDate, slot, portTimeWindowsRecord, pricingEventType);
				if (windowDate < end && windowDate > start) {
					priceIntervals.add(new int[] { windowDate, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, date) });
				}
			}
			priceIntervals.add(getEndInterval(end));
		}
		return priceIntervals;
	}

	void createAndSetTimeWindow(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IPortSlot slot, final int start, final int end) {
		final ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(slot);
		final TimeWindow feasibleTimeWindow = new TimeWindow(start, end, timeWindow.getExclusiveEndFlex());
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

}
