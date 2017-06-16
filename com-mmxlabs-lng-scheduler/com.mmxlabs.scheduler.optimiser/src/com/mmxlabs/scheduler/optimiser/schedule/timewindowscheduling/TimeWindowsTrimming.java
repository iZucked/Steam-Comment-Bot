/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
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
	public @NonNull IPortTimeWindowsRecord processCargo(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final int vesselStartTime) {
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
			final IEndPortSlot end = portTimeWindowRecord.getReturnSlot() instanceof IEndPortSlot ? (IEndPortSlot) portTimeWindowRecord.getReturnSlot() : null;
			// process and set time windows
			processTimeWindowsForCargo(portTimeWindowRecord, vesselStartTime, load, discharge, end);
		}
		return portTimeWindowRecord;
	}

	private void setFeasibleTimeWindowForEndSlot(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IEndPortSlot end, final int endTimeOfLastNonReturnSlot,
			final long minimumTravelTime) {
		processFeasibleTimeWindowForEndSlot(portTimeWindowRecord, end, endTimeOfLastNonReturnSlot, (int) minimumTravelTime);
	}

	private void processFeasibleTimeWindowForEndSlot(@NonNull final IPortTimeWindowsRecord portTimeWindowRecord, @NonNull final IEndPortSlot end, final int endTimeOfLastNonReturnSlot,
			final int minimumTravelTime) {
		final ITimeWindow endTimeWindow = end.getTimeWindow();
		// assume that end will start at the start of the previous slot's time window + previous slots duration
		final int assumedStart = endTimeOfLastNonReturnSlot + minimumTravelTime;
		int feasibleStart, feasibleEnd;
		feasibleStart = feasibleEnd = IPortSlot.NO_PRICING_DATE;
		if (endTimeWindow == null) {
			feasibleStart = assumedStart;
		} else {
			feasibleStart = Math.max(assumedStart, endTimeWindow.getInclusiveStart());
			if (endTimeWindow.getExclusiveEnd() != IPortSlot.NO_PRICING_DATE) {
				feasibleEnd = Math.max(endTimeWindow.getExclusiveEnd(), feasibleStart + 1);
			}
		}
		portTimeWindowRecord.setSlotFeasibleTimeWindow(end, new MutableTimeWindow(feasibleStart, feasibleEnd));
	}

	/**
	 * Gets time effectively for the earliest time we can start
	 * 
	 * @param portTimeWindowRecord
	 * @return
	 */
	private int getEarliestStartTimeOfBallastForEndSlot(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		final List<@NonNull IPortSlot> slots = portTimeWindowRecord.getSlots();
		final IPortSlot lastNonEndSlot = slots.get(slots.size() - 1);
		final ITimeWindow previousFeasibleTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(lastNonEndSlot);
		return previousFeasibleTimeWindow.getInclusiveStart() + portTimeWindowRecord.getSlotDuration(lastNonEndSlot);
	}

	/**
	 * Process time windows for slots in a cargo, with different rules for different pricing date combinations.
	 * 
	 * @param portTimeWindowRecord
	 * @param vesselStartTime
	 * @param load
	 * @param discharge
	 * @param end
	 */
	private void processTimeWindowsForCargo(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final int vesselStartTime, final @Nullable ILoadOption load,
			final @Nullable IDischargeOption discharge, final IEndPortSlot end) {
		if (load != null && discharge != null && load.getLoadPriceCalculator() instanceof IPriceIntervalProvider && discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
			if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
					|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
					&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
							|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
				// simplest case
				@NonNull
				final List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load),
						dischargePriceIntervalsIndependentOfLoad, dischargePriceIntervalsIndependentOfLoad, false);
			} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord) && (priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
				// complex case (L -> D; D -> L)
				final List<int[]> dischargePriceIntervalsBasedOnLoad = getDischargePriceIntervalsBasedOnLoad(portTimeWindowRecord, discharge);
				trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load), dischargePriceIntervalsBasedOnLoad,
						dischargePriceIntervalsBasedOnLoad, false);
			} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord) && (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
					|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
				// complex case (L -> D; D -> D)
				final List<int[]> loadZeroPrices = new LinkedList<>();
				loadZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(), 0 });
				final List<int[]> loadIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(),
						portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getExclusiveEnd(), loadZeroPrices);
				final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
						(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, false);
				// List<int[]> boiloffPricingIntervals = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				final List<int[]> boiloffPricingIntervals = dischargeIntervals;
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
			if (end != null) {
				final List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				final IVessel vessel = getVesselFromPortTimeWindowsRecord(portTimeWindowRecord);
				final int[] endElementTimes = trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, load, discharge, end, vessel, dischargePriceIntervalsIndependentOfLoad,
						dischargePriceIntervalsIndependentOfLoad, vesselStartTime);
				final MutableTimeWindow tw = new MutableTimeWindow(endElementTimes[2], endElementTimes[2] + 1);
				portTimeWindowRecord.setSlotFeasibleTimeWindow(end, tw);
			}
		}
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
	int[] trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull IVessel vessel, final @NonNull ILoadOption load,
			final @NonNull IDischargeOption discharge, final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals, final List<int[]> boiloffPricingIntervals,
			final boolean inverted) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final @NonNull LadenRouteData @NonNull [] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel,
				loadTimeWindow.getInclusiveStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTravelTime = priceIntervalProviderHelper.getMinTravelTimeAtMaxSpeed(sortedCanalTimes);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals), minTravelTime + loadDuration);
		final int maxTime = Math.max(priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals), minTime);
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
			final @NonNull IntervalData @NonNull [] purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final @NonNull IntervalData @NonNull [] salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			final @NonNull IntervalData @NonNull [] boiloffIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals);
			if (!inverted) {
				return findBestBucketPairWithRouteAndBoiloffConsiderations(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals);
			} else {
				return findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals);
			}
		}
	}

	int[] trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(final IPortTimeWindowsRecord portTimeWindowsRecord, final ILoadOption load, final IDischargeOption discharge,
			final IEndPortSlot endSlot, final IVessel vessel, final List<int[]> dischargePriceIntervals, final List<int[]> boiloffPricingIntervals, final int vesselStartTime) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		assert dischargeTimeWindow != null;
		final IVesselAvailability vesselAvailability = schedulerCalculationUtils.getVesselAvailabilityFromResource(portTimeWindowsRecord.getResource());
		// get distances for end ballast journey
		final LadenRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumBallastTravelTimes(discharge.getPort(), endSlot.getPort(), vesselAvailability.getVessel(),
				Math.max((dischargeTimeWindow.getExclusiveEnd() - 1) + portTimeWindowsRecord.getSlotDuration(discharge), IPortSlot.NO_PRICING_DATE));
		// get time we'll be starting the final ballast journey
		final int endTimeOfLastNonReturnSlot = getEarliestStartTimeOfBallastForEndSlot(portTimeWindowsRecord);
		// set the feasible window for the end slot
		setFeasibleTimeWindowForEndSlot(portTimeWindowsRecord, endSlot, endTimeOfLastNonReturnSlot, LadenRouteData.getMinimumTravelTime(sortedCanalTimes));
		// get intervals for the end slot based on different speeds
		final List<int[]> endPriceIntervals = getEndPriceIntervals(portTimeWindowsRecord, discharge, endSlot, endTimeOfLastNonReturnSlot, vessel, load.getCargoCVValue());
		// choose the best time for end
		final int[] times = findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(vesselAvailability, load, sortedCanalTimes, portTimeWindowsRecord.getSlotDuration(discharge),
				priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals), priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals),
				priceIntervalProviderHelper.getIntervalsBoundsAndPricesWithNoRange(endPriceIntervals), vesselStartTime);
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
	private int[] findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(final @NonNull IVessel vessel, final @NonNull ILoadOption load,
			final @NonNull LadenRouteData @NonNull [] sortedCanalTimes, final int loadDuration, final @NonNull IntervalData @NonNull [] purchaseIntervals,
			final @NonNull IntervalData @NonNull [] boiloffIntervals, final @NonNull IntervalData @NonNull [] salesIntervals) {

		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = 0;
		int bestSalesDetailsIdx = salesIntervals.length - 1;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		LadenRouteData bestCanalDetails = sortedCanalTimes[0];
		long bestMargin = Long.MIN_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex < purchaseIntervals.length; purchaseIndex++) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex >= 0; salesIndex--) {
					final int salesPrice = boiloffIntervals[purchaseIndex].price; // inverted!
					final NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex],
							salesIntervals[salesIndex], loadDuration, salesPrice, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(),
							load.getCargoCVValue(), true);
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
		} else if (sortedCanalTimes.length > 1) {
			// still need to find out the best canal
			final NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[0], boiloffIntervals[0], loadDuration,
					salesIntervals[0].price, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(), load.getCargoCVValue(), true);
			bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
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
	public int[] findBestBucketPairWithRouteAndBoiloffConsiderations(final @NonNull IVessel vessel, final @NonNull ILoadOption load, final @NonNull LadenRouteData @NonNull [] sortedCanalTimes,
			final int loadDuration, final @NonNull IntervalData @NonNull [] purchaseIntervals, final @NonNull IntervalData @NonNull [] boiloffIntervals,
			final @NonNull IntervalData @NonNull [] salesIntervals) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		LadenRouteData bestCanalDetails = sortedCanalTimes[0];
		long bestMargin = Long.MIN_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
					NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails;
					totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex], boiloffIntervals[salesIndex], loadDuration,
							salesIntervals[salesIndex].price, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(), load.getCargoCVValue(), true);
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
		} else if (sortedCanalTimes.length > 1) {
			// still need to find out the best canal
			final NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[0], boiloffIntervals[0], loadDuration,
					salesIntervals[0].price, 0, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), vessel.getVesselClass(), load.getCargoCVValue(), true);
			bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
		}

		assert bestCanalDetails != null;
		return getCargoBounds(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.ladenTimeAtMaxSpeed, (int) bestCanalDetails.ladenTimeAtNBOSpeed);
	}

	public int[] findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(final IVesselAvailability vesselAvailability, final ILoadOption load, final LadenRouteData[] sortedCanalTimes,
			final int loadDuration, final IntervalData[] purchaseIntervals, final IntervalData[] boiloffIntervals, final IntervalData[] salesIntervals, final int vesselStartTime) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		long bestMargin = Long.MAX_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
					final long charterRatePerDay = schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselAvailability, vesselStartTime, purchaseIntervals[purchaseIndex].start);
					final NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex],
							salesIntervals[salesIndex], // D to E
							loadDuration, boiloffIntervals[purchaseIndex].price, charterRatePerDay, sortedCanalTimes, vesselAvailability.getVessel().getVesselClass().getNBORate(VesselState.Ballast),
							vesselAvailability.getVessel().getVesselClass(), load.getCargoCVValue(), false);
					final long newMargin = totalEstimatedJourneyCostDetails.getSecond();
					if (newMargin < bestMargin) {
						bestMargin = newMargin;
						bestPurchaseDetailsIdx = purchaseIndex;
						bestSalesDetailsIdx = salesIndex;
					}
				}
			}
		}

		return new int[] { purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end };
	}

	private int[] getCargoBounds(final int purchaseStart, final int purchaseEnd, final int salesStart, final int salesEnd, final int loadDuration, final int maxSpeedCanal, final int nboSpeedCanal) {
		final int[] idealTimes = priceIntervalProviderHelper.getIdealLoadAndDischargeTimesGivenCanal(purchaseStart, purchaseEnd, salesStart, salesEnd, loadDuration, maxSpeedCanal, nboSpeedCanal);
		return new int[] { idealTimes[0], processEndTime(idealTimes[0]), idealTimes[1], processEndTime(idealTimes[1]) };
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
	private void trimLoadAndDischargeWindowsWithRouteChoice(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge,
			final List<int[]> loadIntervals, final List<int[]> dischargeIntervals, final List<int[]> boiloffPricingIntervals, final boolean switched) {
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
	private List<int[]> getLoadPriceIntervalsIndependentOfDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord));
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsIndependentOfLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge) {
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
	private List<int[]> getLoadPriceIntervalsBasedOnDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

//	private void trimLoadWindowBasedOnDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load, final IDischargeOption discharge) {
//		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load));
//		final int start = bounds.getFirst();
//		final int end = bounds.getSecond();
//		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
//	}

	private void trimDischargeWindowIndependentOfLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsBasedOnLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	/**
	 * Get price intervals for the end element. Bounded at the start either by window or the end of the previous interval
	 * 
	 * @param portTimeWindowRecord
	 * @param discharge
	 * @param end
	 * @param vessel
	 * @param cv
	 * @return
	 */
	@NonNull
	private List<int[]> getEndPriceIntervals(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge, final @NonNull IEndPortSlot end,
			final int endTimeOfLastNonReturnSlot, final IVessel vessel, final int cv) {
		final ITimeWindow feasibleEndTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(end);
		final ITimeWindow specifiedEndTimeWindow = end.getTimeWindow();
		final List<Integer> sortedTimes = schedulingCanalDistanceProvider.getTimeDataForDifferentSpeedsAndRoutes(discharge.getPort(), end.getPort(), vessel, cv, endTimeOfLastNonReturnSlot, false);
		final int speedBasedEndTime = sortedTimes.get(sortedTimes.size() - 1) + 1;
		int upperBound;
		final int lowerBound = feasibleEndTimeWindow.getInclusiveStart();
		if (specifiedEndTimeWindow == null) {
			upperBound = speedBasedEndTime;
		} else {
			final int specifiedEnd = specifiedEndTimeWindow.getExclusiveEnd();
			if (specifiedEnd == IPortSlot.NO_PRICING_DATE) {
				upperBound = speedBasedEndTime;
			} else {
				upperBound = Math.max(lowerBound + 1, Math.min(speedBasedEndTime, specifiedEnd));
			}
		}
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(lowerBound, upperBound, priceIntervalProviderHelper.getEndElementPriceIntervals(sortedTimes));
	}

//	private void trimDischargeWindowBasedOnLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge) {
//		final ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
//		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(priceIntervalProviderHelper.getFeasibleIntervalSubSet(timeWindow.getInclusiveStart(),
//				timeWindow.getExclusiveEnd(), priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord)));
//		final int start = bounds.getFirst();
//		final int end = bounds.getSecond();
//		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
//	}
//
//	private void loadOrDischargeDeterminesBothPricingEvents(final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge, final IPriceIntervalProvider loadPriceIntervalProvider,
//			final @NonNull IPriceIntervalProvider dischargePriceIntervalProvider, final IPortTimeWindowsRecord portTimeWindowRecord, final boolean dateFromLoad) {
//		final List<int @NonNull []> complexPricingIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider,
//				portTimeWindowRecord, dateFromLoad);
//		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(complexPricingIntervals);
//		final int start = bounds.getFirst();
//		final int end = bounds.getSecond();
//		if (dateFromLoad) {
//			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
//		} else {
//			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
//		}
//	}

	/**
	 * Makes sure the end of a time window is in the proper format. Takes as input the inclusive end and outputs the correct format.
	 * 
	 * @param end
	 * @return
	 */
	private int processEndTime(final int inclusiveEnd) {
		// make exclusive end
		return inclusiveEnd + 1;
	}

}
