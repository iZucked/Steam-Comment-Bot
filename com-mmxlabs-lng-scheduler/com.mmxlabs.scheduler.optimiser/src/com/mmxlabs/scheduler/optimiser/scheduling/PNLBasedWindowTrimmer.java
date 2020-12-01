/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseEndEvent;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseStartEvent;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.scheduling.PNLBasedWindowTrimmerUtils.TimeChoice;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

@NonNullByDefault
public class PNLBasedWindowTrimmer {

	public static final int CACHE_SIZE = 10_000_000;

	public static final int SIZE_OF_POOL = 80;
	public static final boolean TRIM_EQUIV = true;

	// When iterating, discard times which are with n hours of the last time tried.
	public static final int DISCARD_SMALL_INTERVAL = -1; // How large does the array need to be before we start discarding
	public static final int DISCARD_THRESHOLD = 8; // How large does the array need to be before we start discarding
	public static final int DISCARD_DISTANCE = 48; // How many hours to skip over

	@Inject
	private IVoyagePlanEvaluator voyagePlanEvaluator;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private PNLBasedWindowTrimmerUtils trimmerUtils;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private PanamaBookingHelper panamaBookingsHelper;

	@Inject
	@Named(SchedulerConstants.Key_PNLTrimmerCache)
	private CacheMode cacheMode;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private static final boolean recordCacheStats = false;

	private final LoadingCache<PNLTrimmerShippedCacheKey, List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>>> shippedCache;

	@SuppressWarnings("null")
	@Inject
	public PNLBasedWindowTrimmer(final @Named(SchedulerConstants.CONCURRENCY_LEVEL) int concurrencyLevel) {

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(CACHE_SIZE);

		if (recordCacheStats) {
			builder = builder.recordStats();
		}

		this.shippedCache = builder //
				.build(new CacheLoader<PNLTrimmerShippedCacheKey, List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>>>() {

					@Override
					public List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> load(final PNLTrimmerShippedCacheKey key) throws Exception {
						return doComputeVoyagePlanResults(key);
					}
				});
	}

	@Subscribe
	public void startPhase(final OptimisationPhaseStartEvent event) {
		// TODO: Inspect settings and invalidate only if needed
		// E.g. gco on/off
		// E.g. lateness parameter changes.
		clearCaches();
	}

	@Subscribe
	public void endPhase(final OptimisationPhaseEndEvent event) {
		if (recordCacheStats) {
			System.out.println("PNL Trimmer: " + shippedCache.stats());
		}
		clearCaches();
	}

	public void clearCaches() {
		shippedCache.invalidateAll();
	}

	public void trimWindows(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			trimWindowsForRoundTrip(resource, trimmedWindows, travelTimeData);
		} else {
			trimWindowsForCharter(resource, trimmedWindows, travelTimeData);
		}
	}

	public void trimWindowsForRoundTrip(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		// Check non-empty sequence
		if (trimmedWindows.isEmpty()) {
			return;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		final Map<IPortSlot, Collection<TimeChoice>> intervalMap = trimmerUtils.computeDefaultIntervals(trimmedWindows, resource, travelTimeData);

		for (int idx = 0; idx < trimmedWindows.size(); idx++) {

			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(idx);

			// Only schedule cargoes.
			if (portTimeWindowsRecord.getFirstSlot().getPortType() != PortType.Load) {
				continue;
			}

			// Try to find the end interval
			final IPortSlot nextSlot = portTimeWindowsRecord.getReturnSlot();
			assert nextSlot != null;

			// Round trip cargoes have no prior state.
			final List<ScheduledVoyagePlanResult> results = evaluateRoundTripWithIntervals(resource, vesselAvailability, portTimeWindowsRecord, travelTimeData, intervalMap);

			Collections.sort(results, ScheduledVoyagePlanResult::compareTo);

			final ScheduledVoyagePlanResult newPrev = results.get(0);
			for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {
				final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);

				final int t = newPrev.arrivalTimes.get(i);

				final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(t, t + 1);
				portTimeWindowsRecord.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
			}
		}
	}

	public void trimWindowsForCharter(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (trimmedWindows.isEmpty() || (trimmedWindows.size() < 2 && vesselAvailability.isOptional())) {
			return;
		}

		// Add in a final record for the end event.
		{
			ITimeWindow tw = null;
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
			if (endRequirement != null) {
				tw = endRequirement.getTimeWindow();
			}

			if (tw == null) {
				tw = new TimeWindow(0, Integer.MAX_VALUE);
			}

			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(trimmedWindows.size() - 1);
			final IPortSlot endEventSlot = portTimeWindowsRecord.getReturnSlot();
			assert endEventSlot != null;

			final int slotIndex = portTimeWindowsRecord.getIndex(endEventSlot);

			final PortTimeWindowsRecord endEventPTR = new PortTimeWindowsRecord();
			endEventPTR.setSlot(endEventSlot, tw, 0, slotIndex);
			endEventPTR.setSlotFeasibleTimeWindow(endEventSlot, tw);
			endEventPTR.setSlotDuration(endEventSlot, 0);

			trimmedWindows.add(endEventPTR);
		}

		// Link the records together by setting the previous cargo return slot
		{
			for (int idx = 1; idx < trimmedWindows.size(); idx++) {
				final IPortTimeWindowsRecord prev = trimmedWindows.get(idx - 1);
				final IPortTimeWindowsRecord next = trimmedWindows.get(idx);

				prev.setReturnSlot(next.getFirstSlot(), next.getFirstSlotFeasibleTimeWindow(), 0, next.getIndex(next.getFirstSlot()));

			}
		}

		// Compute the initial set of arrival times for each slot. This is based on pricing change points and max speed/NBO based travel times.
		// This will be further refined later depending on previous voyages.
		final Map<IPortSlot, Collection<TimeChoice>> intervalMap = trimmerUtils.computeDefaultIntervals(trimmedWindows, resource, travelTimeData);

		List<ScheduledRecord> currentHeads = new LinkedList<>();
		for (int idx = 0; idx < trimmedWindows.size(); idx++) {

			final boolean lastPlan = idx == trimmedWindows.size() - 1;
			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(idx);

			final List<ScheduledRecord> results = evaluateShippedWithIntervals(resource, vesselAvailability, lastPlan, portTimeWindowsRecord, travelTimeData, intervalMap, currentHeads);

			assert !results.isEmpty();
			Collections.sort(results, ScheduledRecord.getComparator(lastPlan));

			if (!lastPlan && TRIM_EQUIV) {
				// Attempt to retain the best solutions for the given vessel start time and end
				// time of the current voyage plan.

				final Map<Triple<Integer, Integer, PreviousHeelRecord>, ScheduledRecord> best = new HashMap<>();
				for (final ScheduledRecord r : results) {
					final Triple<Integer, Integer, PreviousHeelRecord> p = new Triple<>(r.sequenceStartTime, r.currentEndTime, r.previousHeelRecord);
					best.putIfAbsent(p, r);
				}
				results.retainAll(best.values());
			}

			final int sz = SIZE_OF_POOL;
			while (results.size() > sz) {
				results.remove(sz);
			}

			currentHeads = results;

		}

		// All scheduled, now apply the arrival times to the windows.
		// update at the end
		Collections.sort(currentHeads, ScheduledRecord.getComparator(true));
		// Apply the best result found
		{
			// The data structure only allows us to work backwards through plans
			ScheduledRecord c = currentHeads.get(0);
			final List<ScheduledVoyagePlanResult> l = new LinkedList<>();
			while (c != null) {
				l.add(0, c.result);
				c = c.previous;
			}
			int idx = 0;
			int expectedMinTime = 0;
			for (final ScheduledVoyagePlanResult newPrev : l) {
				final IPortTimeWindowsRecord ptwr = trimmedWindows.get(idx);

				for (int i = 0; i < ptwr.getSlots().size(); ++i) {
					final IPortSlot slot = ptwr.getSlots().get(i);

					final int t = newPrev.arrivalTimes.get(i);

					// Check we have respected the min travel time.
					assert t >= expectedMinTime;

					final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(t, t + 1);
					ptwr.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
					// Update last records return window to match this window
					if (idx > 0 && i == 0) {
						trimmedWindows.get(idx - 1).setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
					}

					expectedMinTime = t + travelTimeData.getMinTravelTime(ptwr.getIndex(slot));

					// Reclassify Panama Northbound as Relaxed, or Beyond based on new timewindow
					if (ptwr.getSlotIsNextVoyageConstrainedPanama(slot)) {
						if (distanceProvider.getRouteOptionDirection(slot.getPort(), ERouteOption.PANAMA) == RouteOptionDirection.NORTHBOUND
								|| panamaBookingsHelper.isSouthboundIdleTimeRuleEnabled()) {
							final int toCanal = panamaBookingsHelper.getTravelTimeToCanal(vesselAvailability.getVessel(), slot.getPort(), true);
							if (toCanal != Integer.MAX_VALUE) {
								int estimatedCanalArrival = t + ptwr.getSlotDuration(slot) + toCanal;
								ptwr.setSlotNextVoyagePanamaPeriod(slot, panamaBookingsHelper.getPanamaPeriod(estimatedCanalArrival));
							}
						}
					}
				}
				++idx;
			}
		}
	}

	public List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> computeVoyagePlanResults(final PNLTrimmerShippedCacheKey key) {
		if (cacheMode == CacheMode.Off) {
			return doComputeVoyagePlanResults(key);
		} else {
			final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> result = shippedCache.getUnchecked(key);
			if (new Random().nextDouble() < 0.0001) {
				CacheStats stats = cache.stats();
				System.out.println("PNL Trimmer: " + stats + " " + (100.0 * stats.hitRate()));
//				cache.

			}
			if (cacheMode == CacheMode.Verify) {
				final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> expected = doComputeVoyagePlanResults(key);
				assert Objects.equals(result, expected);
			}
			return result;
		}
	}

	public List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> doComputeVoyagePlanResults(final PNLTrimmerShippedCacheKey key) {

		final IPortTimeWindowsRecord portTimeWindowsRecord = key.ptwr;

		final IResource resource = key.resource;
		final IVesselAvailability vesselAvailability = key.vesselAvailability;

		final boolean lastPlan = key.lastPlan;

		final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = new LinkedList<>();

		final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator = (spi, portTimesRecord) -> {

			final boolean returnAll = false;
			PreviousHeelRecord previousHeelRecord = spi.getPreviousHeelRecord();
			if (previousHeelRecord == null) {
				previousHeelRecord = new PreviousHeelRecord();
				previousHeelRecord.heelVolumeInM3 = 0;
				previousHeelRecord.lastHeelPricePerMMBTU = 0;
				previousHeelRecord.lastCV = 0;
				previousHeelRecord.forcedCooldown = false;
			}

			final List<ScheduledVoyagePlanResult> result = voyagePlanEvaluator.evaluateShipped(resource, vesselAvailability, //
					vesselAvailability.getCharterCostCalculator(), //
					spi.getVesselStartTime(), //
					previousHeelRecord, portTimesRecord, lastPlan, //
					returnAll, false, // Do not keep voyage plans
					null);

			result.forEach(r -> evaluatorResults.add(Pair.of(spi, r)));
		};

		// Construct search range AND the base port times record used in search
		final PortTimesRecord basePortTimesRecord = new PortTimesRecord();

		for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {

			final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);
			final int t = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot).getInclusiveStart();
			basePortTimesRecord.setSlotTime(slot, t);

			basePortTimesRecord.setRouteOptionBooking(slot, portTimeWindowsRecord.getRouteOptionBooking(slot));
			basePortTimesRecord.setSlotNextVoyageOptions(slot, portTimeWindowsRecord.getSlotNextVoyageOptions(slot), portTimeWindowsRecord.getSlotNextVoyagePanamaPeriod(slot));

			final int visitDuration = portTimeWindowsRecord.getSlotDuration(slot);
			basePortTimesRecord.setSlotDuration(slot, visitDuration);

			final int extraIdleTime = portTimeWindowsRecord.getSlotExtraIdleTime(slot);
			basePortTimesRecord.setSlotExtraIdleTime(slot, extraIdleTime);
		}
		basePortTimesRecord.setSlotTime(basePortTimesRecord.getFirstSlot(), key.spi.getPlanStartTime());
		run(key.spi, key.intervalMap, vesselAvailability, basePortTimesRecord, portTimeWindowsRecord, evaluator, 1, key.minTimeData, key.spi.getPlanStartTime());

		return evaluatorResults;
	}

	public List<ScheduledRecord> evaluateShippedWithIntervals( //
			final IResource resource, final IVesselAvailability vesselAvailability, //
			final boolean lastPlan, final IPortTimeWindowsRecord portTimeWindowsRecord, //
			final MinTravelTimeData minTimeData, //
			final Map<IPortSlot, Collection<TimeChoice>> intervalMap, final List<ScheduledRecord> previousStates) {

		final List<ScheduledRecord> scheduledRecords = new LinkedList<>();

		if (previousStates.isEmpty()) {

			for (final TimeChoice tc : intervalMap.get(portTimeWindowsRecord.getFirstSlot())) {
				final PNLTrimmerShippedCacheKey key = PNLTrimmerShippedCacheKey.forFirstRecord(tc.time, resource, vesselAvailability, portTimeWindowsRecord, lastPlan, intervalMap, minTimeData);
				final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = computeVoyagePlanResults(key);

				scheduledRecords.addAll(evaluatorResults.stream().map(res -> {
					final PreviousHeelRecord pp = new PreviousHeelRecord();
					pp.heelVolumeInM3 = (res == null) ? 0 : res.getSecond().lastHeelVolumeInM3;
					pp.lastHeelPricePerMMBTU = (res == null) ? 0 : res.getSecond().lastHeelPricePerMMBTU;
					pp.lastCV = (res == null) ? 0 : res.getSecond().lastCV;
					pp.forcedCooldown = (res == null) ? false : res.getSecond().forcedCooldown;

					int minDuration = 0;
					int maxDuration = Integer.MAX_VALUE;
					final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
					if (endRequirement != null && endRequirement.isMaxDurationSet()) {
						maxDuration = endRequirement.getMaxDurationInHours();
					}
					if (endRequirement != null && endRequirement.isMinDurationSet()) {
						minDuration = endRequirement.getMinDurationInHours();
					}
					return new ScheduledRecord(res.getSecond(), pp, minDuration, maxDuration);

				}).collect(Collectors.toList()));

			}
		} else {
			for (final ScheduledRecord r : previousStates) {
				final PNLTrimmerShippedCacheKey key = PNLTrimmerShippedCacheKey.from(r, resource, vesselAvailability, portTimeWindowsRecord, lastPlan, intervalMap, minTimeData);
				final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = computeVoyagePlanResults(key);

				scheduledRecords.addAll(evaluatorResults.stream().map(res -> {
					final PreviousHeelRecord pp = new PreviousHeelRecord();
					pp.heelVolumeInM3 = (res == null) ? 0 : res.getSecond().lastHeelVolumeInM3;
					pp.lastHeelPricePerMMBTU = (res == null) ? 0 : res.getSecond().lastHeelPricePerMMBTU;
					pp.lastCV = (res == null) ? 0 : res.getSecond().lastCV;
					pp.forcedCooldown = (res == null) ? false : res.getSecond().forcedCooldown;

					return new ScheduledRecord(res.getSecond(), pp, r);

				}).collect(Collectors.toList()));
			}
		}

		if (scheduledRecords.isEmpty()) {
			throw new IllegalStateException();
		}

		return scheduledRecords;
	}

	public List<ScheduledVoyagePlanResult> evaluateRoundTripWithIntervals(final IResource resource, final IVesselAvailability vesselAvailability, //
			final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData minTimeData, final Map<IPortSlot, Collection<TimeChoice>> intervalMap) {

		final List<ScheduledVoyagePlanResult> results = new LinkedList<>();

		final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator = (r, portTimesRecord) -> {

			final boolean returnAll = false;

			final List<ScheduledVoyagePlanResult> result = voyagePlanEvaluator.evaluateRoundTrip(resource, vesselAvailability, //
					vesselAvailability.getCharterCostCalculator(), //
					portTimesRecord, returnAll, false, // Do not keep voyage plans
					null);
			results.addAll(result);
		};

		// Construct search range AND the base port times record used in search
		final PortTimesRecord basePortTimesRecord = new PortTimesRecord();

		for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {

			final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);
			final int t = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot).getInclusiveStart();
			basePortTimesRecord.setSlotTime(slot, t);

			basePortTimesRecord.setRouteOptionBooking(slot, portTimeWindowsRecord.getRouteOptionBooking(slot));
			basePortTimesRecord.setSlotNextVoyageOptions(slot, portTimeWindowsRecord.getSlotNextVoyageOptions(slot), portTimeWindowsRecord.getSlotNextVoyagePanamaPeriod(slot));

			final int visitDuration = portTimeWindowsRecord.getSlotDuration(slot);
			basePortTimesRecord.setSlotDuration(slot, visitDuration);

			final int extraIdleTime = portTimeWindowsRecord.getSlotExtraIdleTime(slot);
			basePortTimesRecord.setSlotExtraIdleTime(slot, extraIdleTime);
		}

		for (final TimeChoice tc : intervalMap.get(portTimeWindowsRecord.getFirstSlot())) {
			final ScheduledPlanInput spi = new ScheduledPlanInput(tc.time, tc.time, null);
			basePortTimesRecord.setSlotTime(basePortTimesRecord.getFirstSlot(), tc.time);
			run(spi, intervalMap, vesselAvailability, basePortTimesRecord, portTimeWindowsRecord, evaluator, 1, minTimeData, tc.time);
		}

		if (results.isEmpty()) {
			throw new IllegalStateException();
		}

		return results;
	}

	private void run(final ScheduledPlanInput spi, final Map<IPortSlot, Collection<TimeChoice>> intervalMap, final IVesselAvailability vesselAvailability, final PortTimesRecord record,
			final IPortTimeWindowsRecord ptwr, final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator, final int slotIdx, final MinTravelTimeData minTimeData, final int lastSlotArrivalTime) {

		// Slot idx 0 is a fixed input.
		assert slotIdx != 0;

		final IPortSlot returnSlot = ptwr.getReturnSlot();
		final List<@NonNull IPortSlot> slots = ptwr.getSlots();
		final int terminateIdx = slots.size() + (returnSlot == null ? 0 : 1);
		final int returnSlotIdx = returnSlot == null ? -1 : slots.size();

		if (slotIdx == terminateIdx) {
			// 3. Termination state
			evaluator.accept(spi, record.copy());
		} else {
			final int vesselStartTime = spi.getVesselStartTime();
			if (slotIdx == returnSlotIdx) {
				assert returnSlot != null;
				// 2. Same again but for return

				final Set<TimeChoice> times = new TreeSet<>();
				final Collection<TimeChoice> basicIntervals = intervalMap.get(returnSlot);
				if (basicIntervals == null) {
					times.add(TimeChoice.forImportant(lastSlotArrivalTime));
				} else {
					for (final TimeChoice t : basicIntervals) {
						times.add(t);
					}
				}
				if (slotIdx == 0) {
					// Use last time?
				} else if (slotIdx > 0) {
					trimmerUtils.computeIntervalsForSlot(returnSlot, vesselAvailability, vesselStartTime, record, ptwr, slots, slotIdx, minTimeData, lastSlotArrivalTime, times);
				}

				// 1. Main scheduling loop
				int lastT = Integer.MIN_VALUE;
				final boolean checkDiscard = times.size() > DISCARD_THRESHOLD;
				for (final TimeChoice tc : times) {
					int time = tc.time;

					assert time >= 0;

					if (!tc.important && checkDiscard && lastT + DISCARD_DISTANCE > time) {
						continue;
					}
					if (!tc.important && DISCARD_SMALL_INTERVAL > 0) {
						if (lastT + DISCARD_SMALL_INTERVAL > time) {
							continue;
						}
					}
					record.setReturnSlotTime(returnSlot, time);
					run(spi, intervalMap, vesselAvailability, record, ptwr, evaluator, slotIdx + 1, minTimeData, time);
					lastT = time;
				}
			} else {
				final Set<TimeChoice> times = new TreeSet<>();
				if (lastSlotArrivalTime == -1) {
					final Collection<TimeChoice> basicIntervals = intervalMap.get(slots.get(slotIdx));
					for (final TimeChoice t : basicIntervals) {
						times.add(t);
					}
				} else if (slotIdx == 0) {
					// Use last time?
					times.add(TimeChoice.forNormal(lastSlotArrivalTime));
				} else if (slotIdx > 0) {
					final Collection<TimeChoice> basicIntervals = intervalMap.get(slots.get(slotIdx));
					for (final TimeChoice t : basicIntervals) {
						times.add(t);
					}
					trimmerUtils.computeIntervalsForSlot(slots.get(slotIdx), vesselAvailability, vesselStartTime, record, ptwr, slots, slotIdx, minTimeData, lastSlotArrivalTime, times);
				}

				// 1. Main scheduling loop
				int lastT = Integer.MIN_VALUE;
				final boolean checkDiscard = times.size() > DISCARD_THRESHOLD;

				for (final TimeChoice tc : times) {
					int time = tc.time;
					assert time >= 0;

					if (!tc.important && checkDiscard && lastT + DISCARD_DISTANCE > time) {
						continue;
					}
					if (!tc.important && DISCARD_SMALL_INTERVAL > 0) {
						if (lastT + DISCARD_SMALL_INTERVAL > time) {
							continue;
						}
					}
					// Adjust arrival time based on min travel time
					record.setSlotTime(slots.get(slotIdx), time);
					run(spi, intervalMap, vesselAvailability, record, ptwr, evaluator, slotIdx + 1, minTimeData, time);
					lastT = time;
				}
			}
		}
	}
}
