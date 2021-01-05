/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * Methods to trim time windows based on purchase and sales prices and canal
 * costs
 * 
 * @author achurchill
 *
 */
public class TimeWindowsTrimming {

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private ITimeWindowSchedulingCanalDistanceProvider schedulingCanalDistanceProvider;

	@Inject
	private SchedulerCalculationUtils schedulerCalculationUtils;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private PanamaBookingHelper panamaBookingHelper;

	@Inject
	private IVoyagePlanEvaluator voyagePlanEvaluator;

	/**
	 * Trim time windows for a given set of slots
	 * 
	 * @param portTimeWindowRecord
	 * @return
	 */
	public @NonNull IPortTimeWindowsRecord processCargo(final @NonNull IResource resource, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final int vesselStartTime,
			final @NonNull IPortTimeWindowsRecord portTimeWindowRecordStart, MinTravelTimeData minTravelTimeData) {
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
			processTimeWindowsForCargo(resource, portTimeWindowRecord, vesselStartTime, load, discharge, end, portTimeWindowRecordStart, minTravelTimeData);
		}
		return portTimeWindowRecord;
	}

	private void trimDischargeWindowIndependentOfLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void trimLoadWindowIndependentOfDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	/**
	 * Process time windows for slots in a cargo, with different rules for different
	 * pricing date combinations.
	 * 
	 * @param portTimeWindowRecord
	 * @param vesselStartTime
	 * @param load
	 * @param discharge
	 * @param end
	 */
	private void processTimeWindowsForCargo(final @NonNull IResource resource, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final int vesselStartTime, final @Nullable ILoadOption load,
			final @Nullable IDischargeOption discharge, final IEndPortSlot end, final @NonNull IPortTimeWindowsRecord portTimeWindowRecordStart, MinTravelTimeData minTravelTimeData) {

		// Always off for now
		boolean detailedEndSchedulingMode = false;

		if (detailedEndSchedulingMode && end != null) {
			// Not all end states need a more detailed look

			final IVesselAvailability vesselAvailability = schedulerCalculationUtils.getVesselAvailabilityFromResource(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				// Disable for now, but maybe we could consider it
				detailedEndSchedulingMode = false;
			} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) {
				// Use new mode
			} else {
				assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER;
				final IEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);
				if (req.isMaxDurationSet() || req.isMinDurationSet()) {
					// Use new mode, unless..
					if (req.getMinDurationInHours() == req.getMaxDurationInHours()) {
						// No duration to consider
						detailedEndSchedulingMode = false;
					}
				} else if (!req.hasTimeRequirement()) {
					// No end date optimisation needed
					detailedEndSchedulingMode = false;
				} else {
					ITimeWindow timeWindow = req.getTimeWindow();
					if (timeWindow.getExclusiveEnd() - timeWindow.getInclusiveStart() < 48) {
						// For small windows, ignore new mode
						detailedEndSchedulingMode = false;
					}
				}
			}
		}

		if (load != null && discharge != null && load.getLoadPriceCalculator() instanceof IPriceIntervalProvider && discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
			long charterRateForDecision = priceIntervalProviderHelper.getCharterRateForTimePickingDecision(portTimeWindowRecord, portTimeWindowRecordStart, resource);

			if (detailedEndSchedulingMode && end != null) {
				trimCargoAndLastReturnWithRouteChoice(resource, portTimeWindowRecord, load, discharge, end, portTimeWindowRecordStart, minTravelTimeData);
			} else if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
					|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
					&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
							|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
				// simplest case
				@NonNull
				final List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				trimLoadAndDischargeWindowsWithRouteChoice(resource, portTimeWindowRecord, load, discharge, getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load),
						dischargePriceIntervalsIndependentOfLoad, dischargePriceIntervalsIndependentOfLoad, charterRateForDecision, false);
			} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord) && (priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
				// complex case (L -> D; D -> L)
				final List<int[]> dischargePriceIntervalsBasedOnLoad = getDischargePriceIntervalsBasedOnLoad(portTimeWindowRecord, discharge);
				trimLoadAndDischargeWindowsWithRouteChoice(resource, portTimeWindowRecord, load, discharge, getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load),
						dischargePriceIntervalsBasedOnLoad, dischargePriceIntervalsBasedOnLoad, charterRateForDecision, false);
			} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord) && (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
					|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
				// complex case (L -> D; D -> D)
				final List<int @NonNull []> loadZeroPrices = new LinkedList<>();
				loadZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(), 0 });
				final List<int[]> loadIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getInclusiveStart(),
						portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getExclusiveEnd(), loadZeroPrices);
				final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
						(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, false);
				// List<int[]> boiloffPricingIntervals =
				// getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				final List<int[]> boiloffPricingIntervals = dischargeIntervals;
				trimLoadAndDischargeWindowsWithRouteChoice(resource, portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, charterRateForDecision, false);
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
				trimLoadAndDischargeWindowsWithRouteChoice(resource, portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals, charterRateForDecision, true);
			}
			// if last cargo, process end elements
			if (!detailedEndSchedulingMode && end != null) {
				final List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge);
				final IVessel vessel = getVesselFromPortTimeWindowsRecord(resource);
				final int[] endElementTimes = trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(resource, portTimeWindowRecord, load, discharge, end, vessel,
						dischargePriceIntervalsIndependentOfLoad, dischargePriceIntervalsIndependentOfLoad, vesselStartTime, portTimeWindowRecordStart);
				final ITimeWindow tw = new TimeWindow(endElementTimes[2], endElementTimes[2] + 1);
				portTimeWindowRecord.setSlotFeasibleTimeWindow(end, tw);
			}
			{
				// Reclassify Panama Northbound as Relaxed, or Beyond based on new timewindow
				final IVessel vessel = getVesselFromPortTimeWindowsRecord(resource);
				int voyageStartTime = portTimeWindowRecord.getFirstSlotFeasibleTimeWindow().getInclusiveStart();
				for (IPortSlot slot : portTimeWindowRecord.getSlots()) {
					if (portTimeWindowRecord.getSlotIsNextVoyageConstrainedPanama(slot)) {
						if (PanamaBookingHelper.isSouthboundIdleTimeRuleEnabled() || 
							distanceProvider.getRouteOptionDirection(load.getPort(), ERouteOption.PANAMA) == RouteOptionDirection.NORTHBOUND) {
							final int toCanal = panamaBookingHelper.getTravelTimeToCanal(vessel, load.getPort(), true);
							if (toCanal != Integer.MAX_VALUE) {
								int estimatedCanalArrival = voyageStartTime + portTimeWindowRecord.getSlotDuration(load) + toCanal;
								portTimeWindowRecord.setSlotNextVoyagePanamaPeriod(load, panamaBookingHelper.getPanamaPeriod(estimatedCanalArrival));
							}
						}
					}
				}
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
	 * @param charterRateForTimePickingDecision TODO
	 * @param switched
	 */
	private void trimLoadAndDischargeWindowsWithRouteChoice(final @NonNull IResource resource, final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load,
			final @NonNull IDischargeOption discharge, final List<int[]> loadIntervals, final List<int[]> dischargeIntervals, final List<int[]> boiloffPricingIntervals,
			long charterRateForTimePickingDecision, final boolean inverted) {
		final IVessel vessel = getVesselFromPortTimeWindowsRecord(resource);
		int[] bounds = trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, vessel, load, discharge, loadIntervals, dischargeIntervals, boiloffPricingIntervals,
				charterRateForTimePickingDecision, inverted);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, bounds[0], bounds[1]);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, bounds[2], bounds[3]);
	}

	private int[] trimEndElementTimeWindowsWithRouteOptimisationAndBoilOff(final @NonNull IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, final ILoadOption load,
			final @NonNull IDischargeOption discharge, final IEndPortSlot endSlot, final IVessel vessel, final List<int[]> dischargePriceIntervals, final List<int[]> boiloffPricingIntervals,
			final int vesselStartTime, final @NonNull IPortTimeWindowsRecord portTimeWindowRecordStart) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		assert dischargeTimeWindow != null;
		final IVesselAvailability vesselAvailability = schedulerCalculationUtils.getVesselAvailabilityFromResource(resource);

		// get distances for end ballast journey
		final TravelRouteData[] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(discharge.getPort(), endSlot.getPort(), vesselAvailability.getVessel(),
				Math.max((dischargeTimeWindow.getExclusiveEnd() - 1) + portTimeWindowsRecord.getSlotDuration(discharge), IPortSlot.NO_PRICING_DATE),
				portTimeWindowsRecord.getSlotNextVoyageOptions(discharge), portTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(discharge),
				portTimeWindowsRecord.getSlotAdditionalPanamaIdleHours(discharge), false);

		// get time we'll be starting the final ballast journey
		final int endTimeOfLastNonReturnSlot = getEarliestStartTimeOfBallastForEndSlot(portTimeWindowsRecord);

		// set the feasible window for the end slot
		processFeasibleTimeWindowForEndSlot(portTimeWindowsRecord, endSlot, endTimeOfLastNonReturnSlot, TravelRouteData.getMinimumTravelTime(sortedCanalTimes), resource, portTimeWindowRecordStart);

		// get intervals for the end slot based on different speeds
		final List<int[]> endPriceIntervals = getEndPriceIntervals(portTimeWindowsRecord, discharge, endSlot, endTimeOfLastNonReturnSlot, vessel, load.getCargoCVValue());

		/**
		 * TODO: Refactor out vesselStartTime ? Always equals to 0
		 **/
		// choose the best time for end
		final int[] times = findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(vesselAvailability, load, sortedCanalTimes, portTimeWindowsRecord.getSlotDuration(discharge),
				priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals), priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals),
				priceIntervalProviderHelper.getIntervalsBoundsAndPricesWithNoRange(endPriceIntervals), vesselStartTime);
		return times;
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
	 * @param charterRateForTimePickingDecision
	 * @return
	 */
	int[] trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull IVessel vessel, final @NonNull ILoadOption load,
			final @NonNull IDischargeOption discharge, final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals, final List<int[]> boiloffPricingIntervals,
			long charterRateForTimePickingDecision, final boolean inverted) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final @NonNull TravelRouteData @NonNull [] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(load.getPort(), discharge.getPort(), vessel,
				loadTimeWindow.getInclusiveStart() + portTimeWindowsRecord.getSlotDuration(load), portTimeWindowsRecord.getSlotNextVoyageOptions(load),
				portTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(load), portTimeWindowsRecord.getSlotAdditionalPanamaIdleHours(load), true);
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
					(int) sortedCanalTimes[canalsWeCanUse.get(0)].travelTimeAtMaxSpeed, (int) sortedCanalTimes[canalsWeCanUse.get(0)].travelTimeAtNBOSpeed);
		} else {
			// we could go via canal but should we?
			final @NonNull IntervalData @NonNull [] purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final @NonNull IntervalData @NonNull [] salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			final @NonNull IntervalData @NonNull [] boiloffIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(boiloffPricingIntervals);
			if (!inverted) {
				return findBestBucketPairWithRouteAndBoiloffConsiderations(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals,
						charterRateForTimePickingDecision);
			} else {
				return findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, boiloffIntervals, salesIntervals,
						charterRateForTimePickingDecision);
			}
		}
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and
	 * finds the option with the best margin
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param boiloffIntervals
	 * @param salesIntervals
	 * @param charterRateForTimePickingDecision
	 * @return
	 */
	public int[] findBestBucketPairWithRouteAndBoiloffConsiderations(final @NonNull IVessel vessel, final @NonNull ILoadOption load, final @NonNull TravelRouteData @NonNull [] sortedCanalTimes,
			final int loadDuration, final @NonNull IntervalData @NonNull [] purchaseIntervals, final @NonNull IntervalData @NonNull [] boiloffIntervals,
			final @NonNull IntervalData @NonNull [] salesIntervals, long charterRateForTimePickingDecision) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		final long loadVolumeMMBTU = getMaxLoadVolumeInMMBTU(load, vessel);
		TravelRouteData bestCanalDetails = sortedCanalTimes[0];
		long bestMargin = Long.MIN_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
					NonNullPair<TravelRouteData, Long> totalEstimatedJourneyCostDetails;
					totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex], boiloffIntervals[salesIndex], loadDuration,
							salesIntervals[salesIndex].price, charterRateForTimePickingDecision, sortedCanalTimes, vessel.getNBORate(VesselState.Laden), vessel, load.getCargoCVValue(), true);
					final long estimatedCostMMBTU = Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(totalEstimatedJourneyCostDetails.getSecond(), loadVolumeMMBTU);
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
			final NonNullPair<TravelRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[0], boiloffIntervals[0],
					loadDuration, salesIntervals[0].price, charterRateForTimePickingDecision, sortedCanalTimes, vessel.getNBORate(VesselState.Laden), vessel, load.getCargoCVValue(), true);
			bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
		}

		assert bestCanalDetails != null;
		return getCargoBounds(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.travelTimeAtMaxSpeed, (int) bestCanalDetails.travelTimeAtNBOSpeed);
	}

	private long getMaxLoadVolumeInMMBTU(final ILoadOption load, @NonNull IVessel vessel) {
		final long loadVolumeMMBTU = load.getMaxLoadVolumeMMBTU();
		return Math.min(loadVolumeMMBTU, Calculator.convertM3ToMMBTu(vessel.getCargoCapacity(), load.getCargoCVValue()));
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and
	 * finds the option with the best margin with the purchase and sales intervals
	 * inverted
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param boiloffIntervals
	 * @param salesIntervals
	 * @param charterRateForTimePickingDecision
	 * @return
	 */
	private int[] findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(final @NonNull IVessel vessel, final @NonNull ILoadOption load,
			final @NonNull TravelRouteData @NonNull [] sortedCanalTimes, final int loadDuration, final @NonNull IntervalData @NonNull [] purchaseIntervals,
			final @NonNull IntervalData @NonNull [] boiloffIntervals, final @NonNull IntervalData @NonNull [] salesIntervals, long charterRateForTimePickingDecision) {

		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = 0;
		int bestSalesDetailsIdx = salesIntervals.length - 1;
		final long loadVolumeMMBTU = getMaxLoadVolumeInMMBTU(load, vessel);
		TravelRouteData bestCanalDetails = sortedCanalTimes[0];
		long bestMargin = Long.MIN_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex < purchaseIntervals.length; purchaseIndex++) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex >= 0; salesIndex--) {
					final int salesPrice = boiloffIntervals[purchaseIndex].price; // inverted!
					final NonNullPair<TravelRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex],
							salesIntervals[salesIndex], loadDuration, salesPrice, charterRateForTimePickingDecision, sortedCanalTimes, vessel.getNBORate(VesselState.Laden), vessel,
							load.getCargoCVValue(), true);
					final long estimatedCostMMBTU = Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(totalEstimatedJourneyCostDetails.getSecond(), loadVolumeMMBTU);

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
			final NonNullPair<TravelRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[0], boiloffIntervals[0],
					loadDuration, salesIntervals[0].price, charterRateForTimePickingDecision, sortedCanalTimes, vessel.getNBORate(VesselState.Laden), vessel, load.getCargoCVValue(), true);
			bestCanalDetails = totalEstimatedJourneyCostDetails.getFirst();
		}
		assert bestCanalDetails != null;
		return getCargoBounds(purchaseIntervals[bestPurchaseDetailsIdx].start, purchaseIntervals[bestPurchaseDetailsIdx].end, salesIntervals[bestSalesDetailsIdx].start,
				salesIntervals[bestSalesDetailsIdx].end, loadDuration, (int) bestCanalDetails.travelTimeAtMaxSpeed, (int) bestCanalDetails.travelTimeAtNBOSpeed);
	}

	private int[] findBestUnboundBucketTimesWithRouteAndBoiloffConsiderations(final IVesselAvailability vesselAvailability, final ILoadOption load, final TravelRouteData[] sortedCanalTimes,
			final int loadDuration, final IntervalData[] purchaseIntervals, final IntervalData[] boiloffIntervals, final IntervalData[] salesIntervals, final int vesselStartTime) {
		assert purchaseIntervals.length > 0;

		int bestPurchaseDetailsIdx = purchaseIntervals.length - 1;
		int bestSalesDetailsIdx = 0;
		long bestMargin = Long.MAX_VALUE;
		if (purchaseIntervals.length > 1 || salesIntervals.length > 1) {
			for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
				for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.length; salesIndex++) {
					final long charterRatePerDay = schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselAvailability, purchaseIntervals[purchaseIndex].start);
					final NonNullPair<TravelRouteData, Long> totalEstimatedJourneyCostDetails = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchaseIntervals[purchaseIndex],
							salesIntervals[salesIndex], // D to E
							loadDuration, boiloffIntervals[purchaseIndex].price, charterRatePerDay, sortedCanalTimes, vesselAvailability.getVessel().getNBORate(VesselState.Ballast),
							vesselAvailability.getVessel(), load.getCargoCVValue(), false);
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

	private void processFeasibleTimeWindowForEndSlot(@NonNull final IPortTimeWindowsRecord portTimeWindowRecord, @NonNull final IEndPortSlot end, final int endTimeOfLastNonReturnSlot,
			final int minimumTravelTime, final @NonNull IResource resource, @NonNull final IPortTimeWindowsRecord portTimeWindowRecordStart) {
		// assume that end will start at the start of the previous slot's time window +
		// previous slots duration

		final ITimeWindow slotFeasibleTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(end);
		final int assumedStart = endTimeOfLastNonReturnSlot + minimumTravelTime;
		int feasibleStart;
		int feasibleEnd = IPortSlot.NO_PRICING_DATE;

		if (slotFeasibleTimeWindow == null) {
			feasibleStart = assumedStart;
		} else {
			feasibleStart = Math.max(assumedStart, slotFeasibleTimeWindow.getInclusiveStart());
			if (slotFeasibleTimeWindow.getExclusiveEnd() != IPortSlot.NO_PRICING_DATE) {
				feasibleEnd = Math.max(slotFeasibleTimeWindow.getExclusiveEnd(), feasibleStart + 1);
			}
		}

		final IEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);

		if (req.isMaxDurationSet()) {
			final ITimeWindow tw = portTimeWindowRecordStart.getFirstSlotFeasibleTimeWindow();
			final int maxUpperBound = tw.getExclusiveEnd() + req.getMaxDurationInHours();
			feasibleEnd = Math.min(maxUpperBound, feasibleEnd);

			// Sanity check
			feasibleStart = Math.min(feasibleEnd - 1, feasibleStart);
		}

		if (req.isMinDurationSet()) {
			final ITimeWindow tw = portTimeWindowRecordStart.getFirstSlotFeasibleTimeWindow();
			final int minLowerBound = tw.getInclusiveStart() + req.getMinDurationInHours();

			feasibleStart = Math.max(minLowerBound, feasibleStart);

			// Be sure that the end window will never get past the EndBy date if set
			if (req.hasTimeRequirement()) {
				feasibleStart = Math.min(feasibleStart, req.getTimeWindow().getExclusiveEnd() - 1);
			}

			if (feasibleStart > feasibleEnd) {
				// SG: This seems slightly suspect ....
				feasibleEnd = feasibleStart;
				feasibleStart -= 1;
			}
		}

		portTimeWindowRecord.setSlotFeasibleTimeWindow(end, new TimeWindow(feasibleStart, feasibleEnd));
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

	private @NonNull IVessel getVesselFromPortTimeWindowsRecord(final @NonNull IResource resource) {
		final IVessel vessel = priceIntervalProviderHelper.getVessel(resource);
		assert vessel != null;
		return vessel;
	}

	private int[] getCargoBounds(final int purchaseStart, final int purchaseEnd, final int salesStart, final int salesEnd, final int loadDuration, final int maxSpeedCanal, final int nboSpeedCanal) {
		final int[] idealTimes = priceIntervalProviderHelper.getIdealLoadAndDischargeTimesGivenCanal(purchaseStart, purchaseEnd, salesStart, salesEnd, loadDuration, maxSpeedCanal, nboSpeedCanal);
		return new int[] { idealTimes[0], processEndTime(idealTimes[0]), idealTimes[1], processEndTime(idealTimes[1]) };
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

	@NonNull
	private List<int[]> getLoadPriceIntervalsBasedOnDischarge(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsBasedOnLoad(final @NonNull IPortTimeWindowsRecord portTimeWindowRecord, final @NonNull IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	/**
	 * Get price intervals for the end element. Bounded at the start either by
	 * window or the end of the previous interval
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
		final List<Integer> sortedTimes = schedulingCanalDistanceProvider.getTimeDataForDifferentSpeedsAndRoutes(discharge.getPort(), end.getPort(), vessel, cv, endTimeOfLastNonReturnSlot, false,
				portTimeWindowRecord.getSlotNextVoyageOptions(discharge), portTimeWindowRecord.getSlotIsNextVoyageConstrainedPanama(discharge),
				portTimeWindowRecord.getSlotAdditionalPanamaIdleHours(discharge));
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

	/**
	 * Makes sure the end of a time window is in the proper format. Takes as input
	 * the inclusive end and outputs the correct format.
	 * 
	 * @param end
	 * @return
	 */
	private int processEndTime(final int inclusiveEnd) {
		// make exclusive end
		return inclusiveEnd + 1;
	}

	@NonNullByDefault
	private void trimCargoAndLastReturnWithRouteChoice(final IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, ILoadOption load, final IDischargeOption discharge,
			final IEndPortSlot endSlot, final IPortTimeWindowsRecord portTimeWindowsRecordStart, MinTravelTimeData travelTimeData) {

		final IVesselAvailability vesselAvailability = schedulerCalculationUtils.getVesselAvailabilityFromResource(resource);

		// Get basic intervals (each item is time,price)
		final List<int[]> loadPriceIntervalsIndependentOfDischarge = getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowsRecord, load);
		final List<int[]> dischargePriceIntervalsIndependentOfLoad = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowsRecord, discharge);

		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		final ITimeWindow dischargeTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

		assert loadTimeWindow != null;
		assert dischargeTimeWindow != null;

		// get distances for laden journey
		final TravelRouteData[] sortedLadenCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselAvailability.getVessel(),
				Math.max((loadTimeWindow.getExclusiveEnd() - 1) + portTimeWindowsRecord.getSlotDuration(load), IPortSlot.NO_PRICING_DATE), portTimeWindowsRecord.getSlotNextVoyageOptions(load),
				portTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(load), portTimeWindowsRecord.getSlotAdditionalPanamaIdleHours(load), true);

		// get distances for end ballast journey
		final TravelRouteData[] sortedBallastCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(discharge.getPort(), endSlot.getPort(), vesselAvailability.getVessel(),
				Math.max((dischargeTimeWindow.getExclusiveEnd() - 1) + portTimeWindowsRecord.getSlotDuration(discharge), IPortSlot.NO_PRICING_DATE),
				portTimeWindowsRecord.getSlotNextVoyageOptions(discharge), portTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(discharge),
				portTimeWindowsRecord.getSlotAdditionalPanamaIdleHours(discharge), false);

		TreeSet<Integer> loadTimes = new TreeSet<>();
		TreeSet<Integer> dischargeTimes = new TreeSet<>();
		TreeSet<Integer> endTimes = new TreeSet<>();

		int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		for (int[] interval : loadPriceIntervalsIndependentOfDischarge) {
			// Add in basic time
			int offset = interval[1] == Integer.MAX_VALUE ? -1 : 0; // Upper bound?
			loadTimes.add(interval[0] + offset);

			// Work out some idealised discharge times.
			for (TravelRouteData d : sortedLadenCanalTimes) {
				dischargeTimes.add(interval[0] + loadDuration + d.travelTimeAtNBOSpeed);
				dischargeTimes.add(interval[0] + loadDuration + d.travelTimeAtMaxSpeed);
			}
		}

		int dischargeDuration = portTimeWindowsRecord.getSlotDuration(discharge);
		for (int[] interval : dischargePriceIntervalsIndependentOfLoad) {
			// Add in basic time

			int offset = interval[1] == Integer.MAX_VALUE ? -1 : 0; // Upper bound?
			dischargeTimes.add(interval[0] + offset);
			// Work out some idealised end times.
			for (TravelRouteData d : sortedBallastCanalTimes) {
				endTimes.add(interval[0] + dischargeDuration + d.travelTimeAtNBOSpeed);
				endTimes.add(interval[0] + dischargeDuration + d.travelTimeAtMaxSpeed);
			}
//			// Compute "ideal" load date given the discharge
//			for (TravelRouteData d : sortedLadenCanalTimes) {
//				loadTimes.add(interval[0] - loadDuration - d.travelTimeAtNBOSpeed);
//				loadTimes.add(interval[0] - loadDuration - d.travelTimeAtMaxSpeed);
//			}
		}

		//// Now trim the end window a bit

		// get time we'll be starting the final ballast journey
		final int endTimeOfLastNonReturnSlot = getEarliestStartTimeOfBallastForEndSlot(portTimeWindowsRecord);

		// set the feasible window for the end slot
		processFeasibleTimeWindowForEndSlot(portTimeWindowsRecord, endSlot, endTimeOfLastNonReturnSlot, TravelRouteData.getMinimumTravelTime(sortedBallastCanalTimes), resource,
				portTimeWindowsRecordStart);

		// Remove the computed times outside of the given windows
		trimTreeSet(portTimeWindowsRecord.getSlotFeasibleTimeWindow(load), loadTimes);
		trimTreeSet(portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge), dischargeTimes);
		trimTreeSet(portTimeWindowsRecord.getSlotFeasibleTimeWindow(endSlot), endTimes);
		// Unbounded end windows may leave this value...
		endTimes.remove(Integer.MAX_VALUE);
		endTimes.remove(Integer.MAX_VALUE - 1);

		// Dummy record. May want to include some heel?
		PreviousHeelRecord previousHeelRecord = new PreviousHeelRecord();
		previousHeelRecord.forcedCooldown = false;

		// Create a basic PortTimesRecord instance to pass to evaluator. This will be
		// updated with arrival times and then cloned for each evaluation
		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		for (final IPortSlot slot : portTimeWindowsRecord.getSlots()) {
			portTimesRecord.setRouteOptionBooking(slot, portTimeWindowsRecord.getRouteOptionBooking(slot));
			portTimesRecord.setSlotNextVoyageOptions(slot, portTimeWindowsRecord.getSlotNextVoyageOptions(slot), portTimeWindowsRecord.getSlotNextVoyagePanamaPeriod(slot));

			final int visitDuration = portTimeWindowsRecord.getSlotDuration(slot);
			final int extraIdleTime = portTimeWindowsRecord.getSlotExtraIdleTime(slot);

			portTimesRecord.setSlotTime(slot, 0);
			portTimesRecord.setSlotDuration(slot, visitDuration);
			portTimesRecord.setSlotExtraIdleTime(slot, extraIdleTime);
		}
		portTimesRecord.setReturnSlotTime(endSlot, 0);

		// Best solution found
		long[] bestMetrics = null;
		ScheduledVoyagePlanResult bestResult = null;

		// Loop over all combinations of load, discharge and return times.
		for (int loadTime : loadTimes) {
			portTimesRecord.setSlotTime(load, loadTime);

			for (int dischargeTime : dischargeTimes) {
				// Make sure travel time is valid
				dischargeTime = Math.max(dischargeTime, loadTime + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(load)));
				portTimesRecord.setSlotTime(discharge, dischargeTime);

				for (int endTime : endTimes) {
					// Make sure travel time is valid
					endTime = Math.max(endTime, dischargeTime + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(discharge)));
					portTimesRecord.setReturnSlotTime(endSlot, endTime);

					// Evaluate!
					// Note: We assume the caching will take care of duplicated time sets rather
					// than needing to manage this here.
					List<ScheduledVoyagePlanResult> result = voyagePlanEvaluator.evaluateShipped(resource, vesselAvailability, //
							vesselAvailability.getCharterCostCalculator(), //
							0, null, previousHeelRecord, portTimesRecord.copy(), true, false, false, null);

					// Is this the best solution found so far?
					if (bestMetrics == null || MetricType.betterThan(result.get(0).metrics, bestMetrics)) {
						bestMetrics = result.get(0).metrics;
						bestResult = result.get(0);
					}
				}
			}
		}

		assert bestResult != null;

		// Set the time windows to the arrival times of the best solution found.
		// It is possible the final scheduler code may adjust these to something else to
		// satisfy e.g. min/max duration constraints.
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowsRecord, load, bestResult.arrivalTimes.get(0), bestResult.arrivalTimes.get(0) + 1);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowsRecord, discharge, bestResult.arrivalTimes.get(1), bestResult.arrivalTimes.get(1) + 1);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowsRecord, endSlot, bestResult.returnTime, bestResult.returnTime + 1);
	}

	/**
	 * Util method to remove values outside the given time window
	 * 
	 * @param inclusiveStart
	 * @param exclusiveEnd
	 * @param set
	 */

	private void trimTreeSet(ITimeWindow tw, final TreeSet<Integer> set) {
		trimTreeSet(tw.getInclusiveStart(), tw.getExclusiveEnd(), set);
	}

	/**
	 * Util method to remove values outside the given range
	 * 
	 * @param inclusiveStart
	 * @param exclusiveEnd
	 * @param set
	 */
	private void trimTreeSet(final int inclusiveStart, final int exclusiveEnd, final TreeSet<Integer> set) {
		set.add(inclusiveStart);
		set.add(exclusiveEnd - 1);

		set.removeIf(t -> t < inclusiveStart);
		set.removeIf(t -> t >= exclusiveEnd);
	}
}
