/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.cache.CacheVerificationFailedException;
import com.mmxlabs.scheduler.optimiser.cache.GeneralCacheSettings;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduling.PNLBasedWindowTrimmerUtils.TimeChoice;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

@NonNullByDefault
public class PNLBasedWindowTrimmer {

	private final Random randomForVerification = new Random(GeneralCacheSettings.RANDOM_VERIFICATION_SEED);

	public static final int SIZE_OF_POOL = 80; // Maximum number of solutions to keep in the pool for the next iteration

	/**
	 * Trim out solutions which are equivalent. E.g. start sequence start and
	 * current end time, retain the best solution so far and discard the rest.
	 * Assumes there is no interaction with the future sections that would change
	 * the current metrics
	 * 
	 */
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
	@Named(SchedulerConstants.Key_PNLBasedWindowTrimmerCache)
	private CacheMode cacheMode;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private final LoadingCache<PNLTrimmerShippedCacheKey, ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>>> shippedCache;

	@SuppressWarnings("null")
	@Inject
	public PNLBasedWindowTrimmer(final @Named(SchedulerConstants.CONCURRENCY_LEVEL) int concurrencyLevel) {

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(GeneralCacheSettings.PNLWindowTrimmer_Default_CacheSize);

		if (GeneralCacheSettings.PNLWindowTrimmer_RecordStats) {
			builder = builder.recordStats();
		}

		this.shippedCache = builder //
				.build(new CacheLoader<PNLTrimmerShippedCacheKey, ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>>>() {

					@Override
					public ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> load(final PNLTrimmerShippedCacheKey key) throws Exception {
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
		if (GeneralCacheSettings.PNLWindowTrimmer_RecordStats) {
			System.out.println("PNL Trimmer: " + shippedCache.stats());
		}
		clearCaches();
	}

	public void clearCaches() {
		shippedCache.invalidateAll();
	}

	public void trimWindows(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			trimWindowsForRoundTrip(resource, trimmedWindows, travelTimeData, sequencesAttributesProvider);
		} else {
			trimWindowsForCharter(resource, trimmedWindows, travelTimeData, sequencesAttributesProvider);
		}
	}

	public void trimWindowsForRoundTrip(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindowRecords, final MinTravelTimeData travelTimeData,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		// Check non-empty sequence
		if (trimmedWindowRecords.isEmpty()) {
			return;
		}


		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

		final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap = trimmerUtils.computeDefaultIntervals(trimmedWindowRecords, resource, travelTimeData);
		
		IWriteLockable.writeLock(trimmedWindowRecords);

		for (int idx = 0; idx < trimmedWindowRecords.size(); idx++) {

			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindowRecords.get(idx);

			// Only schedule cargoes.
			if (portTimeWindowsRecord.getFirstSlot().getPortType() != PortType.Load) {
				continue;
			}

			// Try to find the end interval
			final IPortSlot nextSlot = portTimeWindowsRecord.getReturnSlot();
			assert nextSlot != null;

			// Round trip cargoes have no prior state.
			final List<ScheduledVoyagePlanResult> results = evaluateRoundTripWithIntervals(resource, vesselCharter, portTimeWindowsRecord, travelTimeData, intervalMap,
					sequencesAttributesProvider);

			Collections.sort(results, ScheduledVoyagePlanResult::compareTo);

			final ScheduledVoyagePlanResult newPrev = results.get(0);
			IPortTimeWindowsRecord copy = null;

			for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {
				final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);

				final int t = newPrev.arrivalTimes.get(i);

				// Only create the copy once - if we rest on the assumption that
				// portTimeWindowsRecord.getSlots() is never empty then code inside if-block
				// below should be moved outside this for loop - can probably move outside for
				// loop anyway (worst case we create a copy for a PTWR that has no slots)
				if (copy == null) {
					copy = new PortTimeWindowsRecord(portTimeWindowsRecord);
					trimmedWindowRecords.set(idx, copy);
				}

				final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(t, t + 1);
				copy.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
			}
			
			final int t = newPrev.returnTime;
			final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(t, t + 1);
			copy.setSlotFeasibleTimeWindow(nextSlot, feasibleTimeWindow);
			
		}
	}

	public void trimWindowsForCharter(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindowRecords, final MinTravelTimeData travelTimeData,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

		if (trimmedWindowRecords.isEmpty() || // No records at all
				(trimmedWindowRecords.size() < 2 && vesselCharter.isOptional()) // Just start and end event. If optional, ignore
		) {
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

			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindowRecords.get(trimmedWindowRecords.size() - 1);
			final IPortSlot endEventSlot = portTimeWindowsRecord.getReturnSlot();
			assert endEventSlot != null;

			final int slotIndex = portTimeWindowsRecord.getIndex(endEventSlot);

			final PortTimeWindowsRecord endEventPTR = new PortTimeWindowsRecord();
			endEventPTR.setSlot(endEventSlot, tw, 0, slotIndex);
			endEventPTR.setSlotFeasibleTimeWindow(endEventSlot, tw);
			endEventPTR.setSlotDuration(endEventSlot, 0);

			trimmedWindowRecords.add(endEventPTR);
		}

		// Link the records together by setting the previous cargo return slot
		{
			for (int idx = 1; idx < trimmedWindowRecords.size(); idx++) {
				final IPortTimeWindowsRecord prev = trimmedWindowRecords.get(idx - 1);
				final IPortTimeWindowsRecord next = trimmedWindowRecords.get(idx);

				prev.setReturnSlot(next.getFirstSlot(), next.getFirstSlotFeasibleTimeWindow(), 0, next.getIndex(next.getFirstSlot()));
			}
		}

		// Compute the initial set of arrival times for each slot. This is based on
		// pricing change points and max speed/NBO based travel times.
		// This will be further refined later depending on previous voyages.
		// Note this can change some of the time window records.
		final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap = trimmerUtils.computeDefaultIntervals(trimmedWindowRecords, resource, travelTimeData);

		// Disallow further changes to the records
		IWriteLockable.writeLock(trimmedWindowRecords);

		List<ScheduledRecord> currentHeads = new LinkedList<>();
		for (int idx = 0; idx < trimmedWindowRecords.size(); idx++) {

			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindowRecords.get(idx);
			final boolean lastPlan = idx == trimmedWindowRecords.size() - 1;

			final List<ScheduledRecord> results = evaluateShippedWithIntervals(resource, vesselCharter, lastPlan, portTimeWindowsRecord, travelTimeData, intervalMap, currentHeads,
					sequencesAttributesProvider);

			assert !results.isEmpty();
			boolean includeMinDuration = lastPlan;
			final IPortSlot returnSlot = portTimeWindowsRecord.getReturnSlot();
			includeMinDuration |= returnSlot instanceof IEndPortSlot;
			Collections.sort(results, ScheduledRecord.getComparator(includeMinDuration));

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
				final IPortTimeWindowsRecord ptwr = new PortTimeWindowsRecord(trimmedWindowRecords.get(idx));
				trimmedWindowRecords.set(idx, ptwr);

				for (int i = 0; i < ptwr.getSlots().size(); ++i) {
					final IPortSlot slot = ptwr.getSlots().get(i);

					final int t = newPrev.arrivalTimes.get(i);

					// Check we have respected the min travel time.
					assert t >= expectedMinTime;

					final MutableTimeWindow feasibleTimeWindow = new MutableTimeWindow(t, t + 1);
					ptwr.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
					// Update last records return window to match this window
					if (idx > 0 && i == 0) {
						trimmedWindowRecords.get(idx - 1).setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
					}

					expectedMinTime = t + travelTimeData.getMinTravelTime(ptwr.getIndex(slot));
				}
				++idx;
			}
		}

		trimmedWindowRecords.forEach(IWriteLockable::writeLock);
	}

	public ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> computeVoyagePlanResults(final PNLTrimmerShippedCacheKey key) {
		if (cacheMode == CacheMode.Off) {
			return doComputeVoyagePlanResults(key);
		} else {
			final ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> result = shippedCache.getUnchecked(key);

			if (GeneralCacheSettings.ENABLE_RANDOM_VERIFICATION && randomForVerification.nextDouble() < GeneralCacheSettings.VERIFICATION_CHANCE) {
				final ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> expected = doComputeVoyagePlanResults(key);

				if (expected.size() != result.size()) {
					throw new CacheVerificationFailedException();
				}
				for (int i = 0; i < expected.size(); ++i) {
					final Pair<ScheduledPlanInput, ScheduledVoyagePlanResult> e = expected.get(i);
					final Pair<ScheduledPlanInput, ScheduledVoyagePlanResult> a = result.get(i);

					if (!a.getFirst().equals(e.getFirst())) {
						throw new CacheVerificationFailedException();
					}
					// Note: our isEqual method rather than equals as we don't want to implement
					// hashCode/equals.
					if (!a.getSecond().isEqual(e.getSecond())) {
						throw new CacheVerificationFailedException();
					}
				}

			}

			if (cacheMode == CacheMode.Verify) {
				final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> expected = doComputeVoyagePlanResults(key);
				if (expected.size() != result.size()) {
					throw new CacheVerificationFailedException();
				}
				for (int i = 0; i < expected.size(); ++i) {
					if (!expected.get(i).getFirst().equals(result.get(i).getFirst())) {
						throw new CacheVerificationFailedException();
					}
					if (!expected.get(i).getSecond().isEqual(result.get(i).getSecond())) {
						throw new CacheVerificationFailedException();
					}
				}
			}
			return result;
		}
	}

	public ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> doComputeVoyagePlanResults(final PNLTrimmerShippedCacheKey key) {

		final IPortTimeWindowsRecord portTimeWindowsRecord = key.ptwr;

		final IResource resource = key.resource;
		final IVesselCharter vesselCharter = key.vesselCharter;

		final boolean lastPlan = key.lastPlan;

		final List<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = new LinkedList<>();

		final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator = (spi, portTimesRecord) -> {

			final boolean returnAll = false;
			PreviousHeelRecord previousHeelRecord = spi.getPreviousHeelRecord();
			if (previousHeelRecord == null) {
				previousHeelRecord = new PreviousHeelRecord(0, 0, 0, false);
			}

			final ImmutableList<ScheduledVoyagePlanResult> result = voyagePlanEvaluator.evaluateShipped(resource, vesselCharter, //
					vesselCharter.getCharterCostCalculator(), //
					spi.getVesselStartTime(), //
					key.firstLoadPort, previousHeelRecord, portTimesRecord, lastPlan, //
					returnAll, false, // Do not keep voyage plans
					key.sequencesAttributesProvider, null);

			result.forEach(r -> evaluatorResults.add(Pair.of(spi, r)));
		};

		// Construct search range AND the base port times record used in search
		final PortTimesRecord basePortTimesRecord = new PortTimesRecord();

		for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {

			final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);
			assert slot != null;

			final int t = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot).getInclusiveStart();
			basePortTimesRecord.setSlotTime(slot, t);

			basePortTimesRecord.setRouteOptionBooking(slot, portTimeWindowsRecord.getRouteOptionBooking(slot));
			basePortTimesRecord.setSlotNextVoyageOptions(slot, portTimeWindowsRecord.getSlotNextVoyageOptions(slot));

			final int visitDuration = portTimeWindowsRecord.getSlotDuration(slot);
			basePortTimesRecord.setSlotDuration(slot, visitDuration);

			for (var type: ExplicitIdleTime.values()) {
				final int extraIdleTime = portTimeWindowsRecord.getSlotExtraIdleTime(slot, type);
				basePortTimesRecord.setSlotExtraIdleTime(slot, type, extraIdleTime);
			}
		}

		basePortTimesRecord.setSlotTime(basePortTimesRecord.getFirstSlot(), key.spi.getPlanStartTime());
		run(key.spi, key.intervalMap, vesselCharter, basePortTimesRecord, portTimeWindowsRecord, evaluator, 1, key.minTimeData, key.spi.getPlanStartTime());

		// This can be cached, so make immutable
		return ImmutableList.copyOf(evaluatorResults);
	}

	public List<ScheduledRecord> evaluateShippedWithIntervals( //
			final IResource resource, final IVesselCharter vesselCharter, //
			final boolean lastPlan, final IPortTimeWindowsRecord portTimeWindowsRecord, //
			final MinTravelTimeData minTimeData, //
			final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final List<ScheduledRecord> previousStates, ISequencesAttributesProvider sequencesAttributesProvider) {

		final List<ScheduledRecord> scheduledRecords = new LinkedList<>();

		@Nullable
		final IPort firstLoad = findFirstLoadPort(intervalMap);

		if (previousStates.isEmpty()) {
			// This means we are in the first voyage of the sequence, so
			for (final TimeChoice tc : intervalMap.get(portTimeWindowsRecord.getFirstSlot())) {
				final PNLTrimmerShippedCacheKey key = PNLTrimmerShippedCacheKey.forFirstRecord(tc.time, firstLoad, resource, vesselCharter, portTimeWindowsRecord, lastPlan, intervalMap,
						minTimeData, sequencesAttributesProvider);

				final ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = computeVoyagePlanResults(key);

				scheduledRecords.addAll(evaluatorResults.stream().map(res -> {
					final PreviousHeelRecord pp = (res == null) ? new PreviousHeelRecord() : res.getSecond().endHeelState;

					// As this is the first voyage, we need to set up the min/max durations in the
					// first ScheduledRecord of the list
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
			// We have a previous voyage on the sequence to follow on from.
			for (final ScheduledRecord r : previousStates) {
				final PNLTrimmerShippedCacheKey key = PNLTrimmerShippedCacheKey.from(r, firstLoad, resource, vesselCharter, portTimeWindowsRecord, lastPlan, intervalMap, minTimeData,
						sequencesAttributesProvider);

				final ImmutableList<Pair<ScheduledPlanInput, ScheduledVoyagePlanResult>> evaluatorResults = computeVoyagePlanResults(key);

				scheduledRecords.addAll(evaluatorResults.stream().map(res -> {
					final PreviousHeelRecord pp = (res == null) ? new PreviousHeelRecord() : res.getSecond().endHeelState;
					return new ScheduledRecord(res.getSecond(), pp, r);
				}).collect(Collectors.toList()));
			}
		}

		if (scheduledRecords.isEmpty()) {
			throw new IllegalStateException();
		}

		return scheduledRecords;
	}

	private @Nullable IPort findFirstLoadPort(final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap) {
		int earliestT = Integer.MAX_VALUE;
		IPort earliestLoadPort = null;
		for (final IPortSlot slot : intervalMap.keySet()) {
			if (slot instanceof ILoadOption && slot.getTimeWindow().getInclusiveStart() < earliestT) {
				earliestT = slot.getTimeWindow().getInclusiveStart();
				earliestLoadPort = slot.getPort();
			}
		}
		return earliestLoadPort;
	}

	public List<ScheduledVoyagePlanResult> evaluateRoundTripWithIntervals(final IResource resource, final IVesselCharter vesselCharter, //
			final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData minTimeData, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		final List<ScheduledVoyagePlanResult> results = new LinkedList<>();

		final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator = (r, portTimesRecord) -> {

			final boolean returnAll = false;

			final ImmutableList<ScheduledVoyagePlanResult> result = voyagePlanEvaluator.evaluateRoundTrip(resource, vesselCharter, //
					vesselCharter.getCharterCostCalculator(), //
					portTimesRecord, returnAll, false, // Do not keep voyage plans
					sequencesAttributesProvider, null);
			results.addAll(result);
		};

		// Construct search range AND the base port times record used in search
		final PortTimesRecord basePortTimesRecord = new PortTimesRecord();

		for (int i = 0; i < portTimeWindowsRecord.getSlots().size(); ++i) {

			final IPortSlot slot = portTimeWindowsRecord.getSlots().get(i);
			final int t = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot).getInclusiveStart();
			basePortTimesRecord.setSlotTime(slot, t);

			basePortTimesRecord.setRouteOptionBooking(slot, portTimeWindowsRecord.getRouteOptionBooking(slot));
			basePortTimesRecord.setSlotNextVoyageOptions(slot, portTimeWindowsRecord.getSlotNextVoyageOptions(slot));

			final int visitDuration = portTimeWindowsRecord.getSlotDuration(slot);
			basePortTimesRecord.setSlotDuration(slot, visitDuration);

			for (var type: ExplicitIdleTime.values()) {
				final int extraIdleTime = portTimeWindowsRecord.getSlotExtraIdleTime(slot, type);
				basePortTimesRecord.setSlotExtraIdleTime(slot, type, extraIdleTime);
			}
		}

		for (final TimeChoice tc : intervalMap.get(portTimeWindowsRecord.getFirstSlot())) {
			final ScheduledPlanInput spi = new ScheduledPlanInput(tc.time, tc.time, null);
			basePortTimesRecord.setSlotTime(basePortTimesRecord.getFirstSlot(), tc.time);
			run(spi, intervalMap, vesselCharter, basePortTimesRecord, portTimeWindowsRecord, evaluator, 1, minTimeData, tc.time);
		}

		if (results.isEmpty()) {
			throw new IllegalStateException();
		}

		return results;
	}

	private void run(final ScheduledPlanInput spi, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final IVesselCharter vesselCharter, final PortTimesRecord record,
			final IPortTimeWindowsRecord ptwr, final BiConsumer<ScheduledPlanInput, PortTimesRecord> evaluator, final int slotIdx, final MinTravelTimeData minTimeData, final int lastSlotArrivalTime) {

		// Slot idx 0 is a fixed input.
		assert slotIdx != 0;

		final IPortSlot returnSlot = ptwr.getReturnSlot();
		final List<@NonNull IPortSlot> slots = ptwr.getSlots();
		final int terminateIdx = slots.size() + (returnSlot == null ? 0 : 1);
		final int returnSlotIdx = returnSlot == null ? -1 : slots.size();

		if (slotIdx == terminateIdx) {
			// 3. Termination state
			final PortTimesRecord recordCopy = record.copy();
			PortTimesRecordMaker.updatePortTimesRecordWithPanamaRestrictions(distanceProvider, vesselCharter.getVessel(), ptwr, recordCopy);
			evaluator.accept(spi, recordCopy);
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
					// Add in the default interval range
					for (final TimeChoice t : basicIntervals) {
						times.add(t);
					}
				}
				if (slotIdx == 0) {
					// Use last time?
				} else if (slotIdx > 0) {
					trimmerUtils.computeIntervalsForSlot(returnSlot, vesselCharter, vesselStartTime, record, ptwr, slots, slotIdx, minTimeData, lastSlotArrivalTime, times);
				}

				// 1. Main scheduling loop
				int lastT = Integer.MIN_VALUE;
				final boolean checkDiscard = times.size() > DISCARD_THRESHOLD;
				for (final TimeChoice tc : times) {
					final int time = tc.time;

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
					run(spi, intervalMap, vesselCharter, record, ptwr, evaluator, slotIdx + 1, minTimeData, time);
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
					trimmerUtils.computeIntervalsForSlot(slots.get(slotIdx), vesselCharter, vesselStartTime, record, ptwr, slots, slotIdx, minTimeData, lastSlotArrivalTime, times);
				}

				// 1. Main scheduling loop
				int lastT = Integer.MIN_VALUE;
				final boolean checkDiscard = times.size() > DISCARD_THRESHOLD;

				for (final TimeChoice tc : times) {
					final int time = tc.time;
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
					run(spi, intervalMap, vesselCharter, record, ptwr, evaluator, slotIdx + 1, minTimeData, time);
					lastT = time;
				}
			}
		}
	}
}
