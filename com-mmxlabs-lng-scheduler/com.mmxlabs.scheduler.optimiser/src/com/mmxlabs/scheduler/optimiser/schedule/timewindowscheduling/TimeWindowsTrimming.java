/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * Methods to trim time windows based on purchase and sales prices and canal costs
 * 
 * @author achurchill
 *
 */
public class TimeWindowsTrimming {

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private ITimeWindowSchedulingCanalDistanceProvider schedulingCanalDistanceProvider;

	@Inject
	private SchedulerCalculationUtils schedulerCalculationUtils;
	
	/**
	 * Trim time windows for a given set of slots
	 * 
	 * @param portTimeWindowRecord
	 * @return
	 */
	public IPortTimeWindowsRecord processCargo(final IPortTimeWindowsRecord portTimeWindowRecord, final int vesselStartTime) {
		boolean seenLoad = false;
		boolean seenDischarge = false;
		boolean complexCargo = false;
		final List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData = new LinkedList<>();

		for (final IPortSlot slot : portTimeWindowRecord.getSlots()) {
			if (slot instanceof ILoadOption) {
				slotData.add(new Triple<IPortSlot, Class<?>, PricingEventType>(slot, slot.getClass(), ((ILoadOption) slot).getPricingEvent()));
				if (seenLoad) {
					complexCargo = true;
				} else {
					seenLoad = true;
				}
			} else if (slot instanceof IDischargeOption) {
				slotData.add(new Triple<IPortSlot, Class<?>, PricingEventType>(slot, slot.getClass(), ((IDischargeOption) slot).getPricingEvent()));
				if (seenDischarge) {
					complexCargo = true;
				} else {
					seenDischarge = true;
				}
			}
		}

		if (complexCargo) {
			for (final Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				if (slotRow.getSecond().isAssignableFrom(ILoadOption.class)) {
					if (!priceIntervalProviderHelper.isLoadPricingEventTime((ILoadOption) slotRow.getFirst(), portTimeWindowRecord) && !priceIntervalProviderHelper
							.isPricingDateSpecified(slotRow.getFirst(), PriceIntervalProviderHelper.getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					if (!priceIntervalProviderHelper.isDischargePricingEventTime((IDischargeOption) slotRow.getFirst(), portTimeWindowRecord) && !priceIntervalProviderHelper
							.isPricingDateSpecified(slotRow.getFirst(), PriceIntervalProviderHelper.getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				}
			}
			for (final Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				if (slotRow.getSecond().isAssignableFrom(ILoadOption.class)) {
					final ILoadOption slot = (ILoadOption) slotRow.getFirst();
					if (slot.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
						trimLoadWindowIndependentOfDischarge(portTimeWindowRecord, slot);
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					final IDischargeOption slot = (IDischargeOption) slotRow.getFirst();
					if (slot.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
						trimDischargeWindowIndependentOfLoad(portTimeWindowRecord, slot);
					}
				}
			}
		} else {
			final Pair<ILoadOption, IDischargeOption> slots = priceIntervalProviderHelper.getLoadAndDischarge(portTimeWindowRecord, slotData);
			final ILoadOption load = slots.getFirst();
			final IDischargeOption discharge = slots.getSecond();
			final EndPortSlot end = portTimeWindowRecord.getReturnSlot() instanceof EndPortSlot ? (EndPortSlot) portTimeWindowRecord.getReturnSlot() : null;
			if (load != null && discharge != null && load.getLoadPriceCalculator() instanceof IPriceIntervalProvider && discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
				if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// simplest case
					@NonNull
					List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load),
							dischargePriceIntervalsIndependentOfLoad, dischargePriceIntervalsIndependentOfLoad, false);
				} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord)
						&& (priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
					// complex case (L -> D; D -> L)
					List<int[]> dischargePriceIntervalsBasedOnLoad = getDischargePriceIntervalsBasedOnLoad(portTimeWindowRecord, discharge);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load),
							dischargePriceIntervalsBasedOnLoad, dischargePriceIntervalsBasedOnLoad, false);
				} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord)
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// complex case (L -> D; D -> D)
					final List<int[]> loadZeroPrices = new LinkedList<>();
					loadZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(), 0 });
					final List<int[]> loadIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getExclusiveEnd(), loadZeroPrices);
					final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, false);
					List<int[]> boiloffPricingIntervals = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, false);
				} else if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord)) {
					// complex case (L -> L; D -> L)
					final List<int[]> dischargeZeroPrices = new LinkedList<>();
					dischargeZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getInclusiveStart(), 0 });
					final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getInclusiveStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getExclusiveEnd(), dischargeZeroPrices);
					final List<int[]> loadIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, true);
					final List<int[]> boiloffPricingIntervals = getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, true);
				}
				// if last cargo, process end elements
				if (end != null && end.getTimeWindow() == null) {
					List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
					final IVessel vessel = getVesselFromPortTimeWindowsRecord(portTimeWindowRecord);
					if (vessel.getName().contains("Mel")) {
						int z = 0;
					}
					int[] endElementTimes = trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, load, discharge, end, dischargePriceIntervalsIndependentOfLoad, getEndPriceIntervals(portTimeWindowRecord, discharge, end, vessel, load.getCargoCVValue()), dischargePriceIntervalsIndependentOfLoad, vesselStartTime);
					TimeWindow tw = new TimeWindow(endElementTimes[2], endElementTimes[2]+1);
					if (tw.getInclusiveStart() < portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getExclusiveEnd()) {
						int z = 0;
					}
					portTimeWindowRecord.setSlotFeasibleTimeWindow(end, tw);
				}
			}
		}
		return portTimeWindowRecord;
	}

	/**
	 * Trim time windows given different route options
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @param boiloffPricingIntervals 
	 * @return
	 */
	int[] trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals, List<int[]> boiloffPricingIntervals, final boolean inverted) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getInclusiveStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals);
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			Pair<Integer, Integer> loadBounds;
			Pair<Integer, Integer> dischargeBounds;
			if (!inverted) {
				loadBounds = priceIntervalProviderHelper.getLowestPriceInterval(loadPriceIntervals);
				dischargeBounds = priceIntervalProviderHelper.getHighestPriceInterval(dischargePriceIntervals);
			} else {
				loadBounds = priceIntervalProviderHelper.getHighestPriceInterval(loadPriceIntervals);
				dischargeBounds = priceIntervalProviderHelper.getLowestPriceInterval(dischargePriceIntervals);
			}
			return getCargoBounds(loadBounds.getFirst(), loadBounds.getSecond(), dischargeBounds.getFirst(), dischargeBounds.getSecond(), loadDuration,
					(int) sortedCanalTimes[canalsWeCanUse.get(0)].ladenTimeAtMaxSpeed, (int) sortedCanalTimes[canalsWeCanUse.get(0)].ladenTimeAtNBOSpeed);
		} else {
			// we could go via canal but should we?
			final IntervalData[] purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final IntervalData[] salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			final IntervalData[] boiloffIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals);
			if (!inverted) {
				return findBestBucketPairWithRouteAndBoiloffConsiderations(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals);
			} else {
				return findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals);
			}
		}
	}
	
	int[] trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(final IPortTimeWindowsRecord portTimeWindowsRecord, final ILoadOption load, final IDischargeOption discharge, final EndPortSlot endSlot,
			final List<int[]> dischargePriceIntervals, final List<int[]> endPriceIntervals, List<int[]> boiloffPricingIntervals, int vesselStartTime) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		assert dischargeTimeWindow != null;
		final IVesselAvailability vesselAvailability = schedulerCalculationUtils.getVesselAvailabilityFromResource(portTimeWindowsRecord.getResource());
//		final ITimeWindow endTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(endSlot);
//		assert dischargeTimeWindow != null;
//		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumBallastTravelTimes(load.getPort(), discharge.getPort(), vessel, Math.max(dischargeTimeWindow.getExclusiveEnd() + portTimeWindowsRecord.getSlotDuration(discharge), endTimeWindow.getInclusiveStart()));
		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumBallastTravelTimes(load.getPort(), discharge.getPort(), vesselAvailability.getVessel(), Math.max((dischargeTimeWindow.getExclusiveEnd() - 1) + portTimeWindowsRecord.getSlotDuration(discharge), -1000));
		int[] times = findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(vesselAvailability, load, sortedCanalTimes, portTimeWindowsRecord.getSlotDuration(discharge), priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals), priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals), priceIntervalProviderHelper.getIntervalsBoundsAndPricesWithNoRange(endPriceIntervals), vesselStartTime);
		return times;
	}

	private IVessel getVesselFromPortTimeWindowsRecord(final IPortTimeWindowsRecord portTimeWindowsRecord) {
		final IResource resource = portTimeWindowsRecord.getResource();
		assert resource != null;
		final IVessel vessel = priceIntervalProviderHelper.getVessel(resource);
		assert vessel != null;
		return vessel;
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and finds the option with the best margin with the purchase and sales intervals inverted
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param boiloffIntervals 
	 * @param salesIntervals
	 * @return
	 */
	private int[] findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(final IVessel vessel, final ILoadOption load, final LadenRouteData[] sortedCanalTimes, final int loadDuration,
			final IntervalData[] purchaseIntervals, IntervalData[] boiloffIntervals, final IntervalData[] salesIntervals) {

		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = 0;
		int bestSalesDetailsIdx = salesIntervals.length - 1;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		LadenRouteData bestCanalDetails = null;
		long bestMargin = Long.MIN_VALUE;
		for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex < purchaseIntervals.length; purchaseIndex++) {
			for (int salesIndex = bestSalesDetailsIdx; salesIndex >= 0; salesIndex--) {
				final int salesPrice = boiloffIntervals[purchaseIndex].price; // inverted!
				NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex], salesIntervals[salesIndex], loadDuration, salesPrice, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(), load.getCargoCVValue(), true);
				final long estimatedCostMMBTU = totalEstimatedJourneyCostDetails.getSecond() / loadVolumeMMBTU;

				final long newMargin = purchaseIntervals[purchaseIndex].price - salesIntervals[salesIndex].price - estimatedCostMMBTU; // inverted!
				if (newMargin > bestMargin) {
					bestMargin = newMargin;
					bestPurchaseDetailsIdx = purchaseIndex;
					bestSalesDetailsIdx = salesIndex;
					bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
				}
			}
		}
		assert bestCanalDetails != null;
		return getCargoBounds(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.ladenTimeAtMaxSpeed, (int) bestCanalDetails.ladenTimeAtNBOSpeed);
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and finds the option with the best margin
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param boiloffIntervals 
	 * @param salesIntervals
	 * @return
	 */
	public int[] findBestBucketPairWithRouteAndBoiloffConsiderations(final IVessel vessel, final ILoadOption load, final LadenRouteData[] sortedCanalTimes, final int loadDuration,
			final IntervalData[] purchaseIntervals, IntervalData[] boiloffIntervals, final IntervalData[] salesIntervals) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		LadenRouteData bestCanalDetails = null;
		long bestMargin = Long.MIN_VALUE;
		for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
			for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
				NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex], boiloffIntervals[salesIndex], loadDuration, salesIntervals[salesIndex].price, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(), load.getCargoCVValue(), true);
				final long estimatedCostMMBTU = totalEstimatedJourneyCostDetails.getSecond() / loadVolumeMMBTU;
				final long newMargin = salesIntervals[salesIndex].price - purchaseIntervals[purchaseIndex].price - estimatedCostMMBTU;
				if (newMargin > bestMargin) {
					bestMargin = newMargin;
					bestPurchaseDetailsIdx = purchaseIndex;
					bestSalesDetailsIdx = salesIndex;
					bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
				}
			}
		}

		assert bestCanalDetails != null;
		return getCargoBounds(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.ladenTimeAtMaxSpeed, (int) bestCanalDetails.ladenTimeAtNBOSpeed);
	}

	public int[] findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(final IVesselAvailability vesselAvailability, final ILoadOption load, final LadenRouteData[] sortedCanalTimes, final int loadDuration,
			final IntervalData[] purchaseIntervals, IntervalData[] boiloffIntervals, final IntervalData[] salesIntervals, int vesselStartTime) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		LadenRouteData bestCanalDetails = null;
		long bestMargin = Long.MAX_VALUE;
//		System.out.println("vessel:"+vesselAvailability.getVessel().getName());
		for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
			for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
				long charterRatePerDay = schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselAvailability, vesselStartTime, purchaseIntervals[purchaseIndex].start);
				NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex], salesIntervals[salesIndex], // D to E
						loadDuration, boiloffIntervals[purchaseIndex].price, charterRatePerDay, sortedCanalTimes, vesselAvailability.getVessel().getVesselClass().getNBORate(VesselState.Ballast), vesselAvailability.getVessel().getVesselClass(), load.getCargoCVValue(), false);
				final long estimatedCostMMBTU = totalEstimatedJourneyCostDetails.getSecond() / loadVolumeMMBTU;
//				System.out.println(String.format("time: %s cost: %s", salesIntervals[salesIndex].start, totalEstimatedJourneyCostDetails.getSecond()));
				final long newMargin = totalEstimatedJourneyCostDetails.getSecond();
				if (newMargin < bestMargin) {
					bestMargin = newMargin;
					bestPurchaseDetailsIdx = purchaseIndex;
					bestSalesDetailsIdx = salesIndex;
					bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
				}
			}
		}

		return new int[] {purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end};
	}

	
	private int[] getCargoBounds(final int purchaseStart, final int purchaseEnd, final int salesStart, final int salesEnd, final int loadDuration, final int maxSpeedCanal, final int nboSpeedCanal) {
		final int[] idealTimes = priceIntervalProviderHelper.getIdealLoadAndDischargeTimesGivenCanal(purchaseStart, purchaseEnd, salesStart, salesEnd, loadDuration, maxSpeedCanal, nboSpeedCanal);
		return new int[] {idealTimes[0], processEndTime(idealTimes[0]), idealTimes[1], processEndTime(idealTimes[1])};
	}

	/**
	 * Trim time windows given different route options
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @return
	 */
	@Deprecated
	int[] trimCargoTimeWindowsWithRouteOptimisation(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getInclusiveStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals);
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			final Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(loadPriceIntervals);
			final Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(lowestPriceInterval.getFirst(), lowestPriceInterval.getSecond(), highestPriceInterval.getFirst(),
					highestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[canalsWeCanUse.get(0)].ladenTimeAtMaxSpeed);
		} else {
			// we could go via canal but should we?
			final IntervalData[] purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final IntervalData[] salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals[bestPurchaseDetailsIdx], salesIntervals[bestSalesDetailsIdx], loadDuration, sortedCanalTimes[0].ladenTimeAtMaxSpeed)) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end,
						salesIntervals[bestSalesDetailsIdx].start, salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) sortedCanalTimes[0].ladenTimeAtMaxSpeed);
			} else {
				// we are going to have to go via a canal. is it worth it?
				final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				final IntervalData bestPurchaseDetails = purchaseIntervals[bestPurchaseDetailsIdx];
				final IntervalData bestSalesDetails = salesIntervals[bestSalesDetailsIdx];
				LadenRouteData bestCanalDetails = priceIntervalProviderHelper.getBestCanalDetails(bestPurchaseDetails, bestSalesDetails, loadDuration, sortedCanalTimes);
				long bestMargin = salesIntervals[bestSalesDetailsIdx].price - purchaseIntervals[bestPurchaseDetailsIdx].price - (bestCanalDetails.ladenRouteCost / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
						final LadenRouteData newCanalDetails = priceIntervalProviderHelper.getBestCanalDetails(purchaseIntervals[purchaseIndex], salesIntervals[salesIndex], loadDuration,
								sortedCanalTimes);
						final long newMargin = salesIntervals[salesIndex].price - purchaseIntervals[purchaseIndex].price - (newCanalDetails.ladenRouteCost / loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
							bestCanalDetails = newCanalDetails;
						}
					}
				}
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end,
						salesIntervals[bestSalesDetailsIdx].start, salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.ladenTimeAtMaxSpeed);
			}
		}
	}

	/**
	 * Trim time windows given different route options when load depends on discharge and discharge depends on load
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @return
	 */
	@Deprecated
	private int[] trimCargoTimeWindowsWithRouteOptimisationForInvertedCase(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load,
			final IDischargeOption discharge, final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getInclusiveStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration;
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			final Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(loadPriceIntervals);
			final Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(highestPriceInterval.getFirst(), highestPriceInterval.getSecond(), lowestPriceInterval.getFirst(),
					lowestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[0].ladenTimeAtMaxSpeed);
		} else {
			// we could go via canal but should we?
			final IntervalData[] purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals); // discharge prices!!
			final IntervalData[] salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals); // load prices!!
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals[bestPurchaseDetailsIdx], salesIntervals[bestSalesDetailsIdx], loadDuration, sortedCanalTimes[0].ladenTimeAtMaxSpeed)) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end,
						salesIntervals[bestSalesDetailsIdx].start, salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) sortedCanalTimes[0].ladenTimeAtMaxSpeed);
			} else {
				// we are going to have to go via a canal. is it worth it?
				final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				final IntervalData bestPurchaseDetails = purchaseIntervals[bestPurchaseDetailsIdx];
				final IntervalData bestSalesDetails = salesIntervals[bestSalesDetailsIdx];
				LadenRouteData bestCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes, bestSalesDetails.end - bestPurchaseDetails.start + loadDuration);
				long bestMargin = purchaseIntervals[bestPurchaseDetailsIdx].price - salesIntervals[bestSalesDetailsIdx].price - (bestCanalDetails.ladenRouteCost / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
						final LadenRouteData newCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes,
								salesIntervals[salesIndex].end - purchaseIntervals[purchaseIndex].start + loadDuration);
						final long newMargin = purchaseIntervals[purchaseIndex].price - salesIntervals[salesIndex].price - (newCanalDetails.ladenRouteCost / loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
							bestCanalDetails = newCanalDetails;
						}
					}
				}
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end,
						salesIntervals[bestSalesDetailsIdx].start, salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.ladenTimeAtMaxSpeed);
			}
		}
	}

	/**
	 * Trim a time window for a cargo considering different route options
	 * 
	 * @param portTimeWindowRecord
	 * @param load
	 * @param discharge
	 * @param loadIntervals
	 * @param dischargeIntervals
	 * @param boiloffPricingIntervals 
	 * @param switched
	 */
	private void trimLoadAndDischargeWindowsWithRouteChoice(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadIntervals, final List<int[]> dischargeIntervals, List<int[]> boiloffPricingIntervals, final boolean switched) {
		final IVessel vessel = getVesselFromPortTimeWindowsRecord(portTimeWindowRecord);
		int[] bounds;
		if (switched) {
			bounds = trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, vessel, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, true);
		} else {
			bounds = trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, vessel, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, false);
		}
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, bounds[0], bounds[1]);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, bounds[2], bounds[3]);
	}

	@NonNull
	public List<int[]> getLoadPriceIntervalsIndependentOfDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord));
	}

	@NonNull
	public List<int[]> getDischargePriceIntervalsIndependentOfLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowIndependentOfLoad(discharge, portTimeWindowRecord));
	}

	private void trimLoadWindowIndependentOfDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	@NonNull
	private List<int[]> getLoadPriceIntervalsBasedOnDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

	private void trimLoadWindowBasedOnDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private void trimDischargeWindowIndependentOfLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsBasedOnLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	@NonNull
	public List<int[]> getEndPriceIntervals(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge, final EndPortSlot end, final IVessel vessel, int cv) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		final int endOfDischarge = (dischargeTimeWindow.getExclusiveEnd() - 1) + portTimeWindowRecord.getSlotDuration(discharge);
		List<Integer> times = schedulingCanalDistanceProvider.getTimeDataForDifferentSpeedsAndRoutes(discharge.getPort(), end.getPort(), vessel, cv, endOfDischarge, false);
//		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(endOfDischarge, times.get(times.size() - 1),
//				priceIntervalProviderHelper.getEndElementPriceIntervals(times));
		return priceIntervalProviderHelper.getEndElementPriceIntervals(times);
	}

	private void trimDischargeWindowBasedOnLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge) {
		final ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(priceIntervalProviderHelper.getFeasibleIntervalSubSet(timeWindow.getInclusiveStart(),
				timeWindow.getExclusiveEnd(), priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord)));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void loadOrDischargeDeterminesBothPricingEvents(final ILoadOption load, final IDischargeOption discharge, final IPriceIntervalProvider loadPriceIntervalProvider,
			final IPriceIntervalProvider dischargePriceIntervalProvider, final IPortTimeWindowsRecord portTimeWindowRecord, final boolean dateFromLoad) {
		final List<int[]> complexPricingIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider,
				portTimeWindowRecord, dateFromLoad);
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(complexPricingIntervals);
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		if (dateFromLoad) {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
		} else {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
		}
	}
	
	/**
	 * Makes sure the end of a time window is in the proper format.
	 * Takes as input the inclusive end and outputs the correct format.
	 * @param end
	 * @return
	 */
	private int processEndTime(int inclusiveEnd) {
		// make exclusive end
		return inclusiveEnd + 1;
	}

}
