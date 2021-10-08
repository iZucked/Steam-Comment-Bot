/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseEndEvent;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseStartEvent;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.cache.CacheVerificationFailedException;
import com.mmxlabs.scheduler.optimiser.cache.GeneralCacheSettings;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class TimeWindowScheduler implements IArrivalTimeScheduler {

	private final Random randomForVerification = new Random(0);

	@Inject
	private PortTimesRecordMaker portTimesRecordMaker;

	@Inject
	private Provider<FeasibleTimeWindowTrimmer> timeWindowTrimmerProvider;

	@Inject
	private PriceBasedWindowTrimmer priceBasedWindowTrimmerProvider;

	@Inject
	private PNLBasedWindowTrimmer pnlBasedWindowTrimmerProvider;

	@com.google.inject.Inject(optional = true)
	private ICustomTimeWindowTrimmer customTimeTrimmer;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	@Named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)
	private boolean usePriceBasedWindowTrimming = false;

	@Inject
	@Named(SchedulerConstants.Key_UsePNLBasedWindowTrimming)
	private boolean usePNLBasedWindowTrimming = false;

	@Inject
	@Named(SchedulerConstants.SCENARIO_TYPE_ADP)
	private boolean isADPScenario = false;

	@Inject
	@Named(SchedulerConstants.SCENARIO_TYPE_LONG_TERM)
	private boolean isLongTermScenario = false;

	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean useCanalBasedWindowTrimming = false;

	@Inject
	@Named(SchedulerConstants.Key_TimeWindowSchedulerCache)
	private CacheMode cacheMode;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private final @NonNull LoadingCache<CacheKey, Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData>> cache;

	@Inject
	public TimeWindowScheduler(@Named(SchedulerConstants.CONCURRENCY_LEVEL) final int concurrencyLevel) {

		final int cacheSize = GeneralCacheSettings.TimeWindowScheduler_Default_CacheSize;

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(cacheSize);

		if (GeneralCacheSettings.TimeWindowScheduler_RecordStats) {
			builder = builder.recordStats();
		}
		this.cache = builder //
				.build(new CacheLoader<CacheKey, Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData>>() {

					@Override
					public Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> load(final CacheKey key) throws Exception {
						return doTrimming(key.resource, key.sequence, key.currentBookingData, key.sequencesAttributesProvider);

					}

				});
	}

	public ScheduledTimeWindows calculateTrimmedWindows(final @NonNull ISequences sequences) {
		final Map<IResource, MinTravelTimeData> travelTimeDataMap = new HashMap<>();
		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = new HashMap<>();

		// Construct new bookings data object
		final CurrentBookingData data = new CurrentBookingData();

		if (useCanalBasedWindowTrimming) {
			for (final var entrance : this.panamaSlotsProvider.getAllBookings().keySet()) {
				for (final var booking : this.panamaSlotsProvider.getAllBookings().get(entrance)) {
					data.addBooking(booking);
				}
			}
		}

		for (final IResource resource : sequences.getResources()) {

			List<IPortTimeWindowsRecord> list;
			@NonNull
			final ISequence sequence = sequences.getSequence(resource);
			if (hintEnableCache && cacheMode != CacheMode.Off) {

				@Nullable
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = cache.getUnchecked(new CacheKey(resource, sequence, data.copy(), sequences.getProviders()));
				if (p != null) {
					list = p.getFirst();
					travelTimeDataMap.put(resource, p.getSecond());
				} else {
					list = null;
				}

				// VERIFY
				if (cacheMode == CacheMode.Verify || (GeneralCacheSettings.ENABLE_RANDOM_VERIFICATION && randomForVerification.nextDouble() < GeneralCacheSettings.VERIFICATION_CHANCE)) {
					final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p2 = doTrimming(resource, sequence, data, sequences.getProviders());
					final List<IPortTimeWindowsRecord> list2 = p2.getFirst();

					if (list != null) {
						if (!Objects.equals(list, list2)) {
							throw new CacheVerificationFailedException();
						}
					}
				}
			} else {
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = doTrimming(resource, sequence, data, sequences.getProviders());
				list = p.getFirst();
				travelTimeDataMap.put(resource, p.getSecond());
			}
			// Copy list incase externally modified
			trimmedWindows.put(resource, new ArrayList<>(list));

			// We copy the booking data as we don't know whether or not cache will hit or
			// miss.
			// Modify the original to remove used bookings
			// The reason for the below code appears to be in case of a cache hit, the
			// booking data passed in won't have been updated with used bookings
			// and unassigned bookings utilised moves to assigned bookings etc. Sometimes it
			// will already be updated, sometimes not.
			if (list != null) {
				for (final IPortTimeWindowsRecord rr : list) {
					for (final IPortSlot slot : rr.getSlots()) {
						final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
						if (booking != null) {
							if (!data.isUsed(booking.getEntryPoint(), booking)) {
								data.setUsed(booking.getEntryPoint(), booking);
							}
						}
					}
				}
			}
		}

		// validateBookingsUsedOnce(trimmedWindows);

		return new ScheduledTimeWindows(travelTimeDataMap, trimmedWindows);
	}

	private void validateBookingsUsedOnce(final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows) {
		// Check used bookings.
		final Set<IRouteOptionBooking> usedBookings = new HashSet<>();
		for (final IResource r : trimmedWindows.keySet()) {
			final var list = trimmedWindows.get(r);
			if (list != null) {
				for (final IPortTimeWindowsRecord rr : list) {
					for (final IPortSlot slot : rr.getSlots()) {
						final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
						if (booking != null) {

							if (!usedBookings.contains(booking)) {
								usedBookings.add(booking);
							} else {
								// Problem, booking must have been used twice.
								assert false;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Map<IResource, List<@NonNull IPortTimesRecord>> schedule(final ISequences fullSequences) {

		final ScheduledTimeWindows scheduledTimeWindows = calculateTrimmedWindows(fullSequences);
		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = scheduledTimeWindows.getTrimmedTimeWindowsMap();
		final Map<IResource, List<IPortTimesRecord>> portTimeRecords = new HashMap<>();
		//
		for (int seqIndex = 0; seqIndex < fullSequences.getResources().size(); seqIndex++) {
			final IResource resource = fullSequences.getResources().get(seqIndex);
			final ISequence sequence = fullSequences.getSequence(resource);
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final MinTravelTimeData travelTimeData = scheduledTimeWindows.getTravelTimeData().get(resource);

			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

				final IPortTimesRecord record = portTimesRecordMaker.makeDESOrFOBPortTimesRecord(resource, sequence);
				if (record != null) {
					portTimeRecords.put(resource, Lists.newArrayList(record));
				}
			} else {

				if (!(isADPScenario || isLongTermScenario) && usePNLBasedWindowTrimming) {
					// Use the simple variant. We have computed the necessary values.
					portTimeRecords.put(resource, portTimesRecordMaker.makeSimpleShippedPortTimesRecords(seqIndex, resource, sequence, trimmedWindows.get(resource), travelTimeData));
				} else {
					// Use the more complex variant. Window start is a hint rather than absolute
					// value.
					portTimeRecords.put(resource, portTimesRecordMaker.calculateShippedPortTimesRecords(seqIndex, resource, sequence, trimmedWindows.get(resource), travelTimeData));
				}
			}
		}

		return portTimeRecords;
	}

	public boolean isUsePriceBasedWindowTrimming() {
		return usePriceBasedWindowTrimming;
	}

	public boolean isUseCanalBasedWindowTrimming() {
		return useCanalBasedWindowTrimming;
	}

	public void setUseCanalBasedWindowTrimming(final boolean useCanalBasedWindowTrimming) {
		this.useCanalBasedWindowTrimming = useCanalBasedWindowTrimming;
		// Invalidate caches
		clearCaches();
	}

	public void setUsePriceBasedWindowTrimming(final boolean usePriceBasedWindowTrimming) {
		this.usePriceBasedWindowTrimming = usePriceBasedWindowTrimming;
		// Invalidate caches
		clearCaches();
	}

	public void setUsePNLBasedWindowTrimming(final boolean usePNLBasedWindowTrimming) {
		this.usePNLBasedWindowTrimming = usePNLBasedWindowTrimming;
		// Invalidate caches
		clearCaches();
	}

	private Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> doTrimming(final IResource resource, final ISequence sequence, final CurrentBookingData data,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		final FeasibleTimeWindowTrimmer feasibleTimeWindowTrimmer = timeWindowTrimmerProvider.get();
		feasibleTimeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);
		final MinTravelTimeData minTimeData = new MinTravelTimeData(resource, sequence);
		final List<IPortTimeWindowsRecord> list = feasibleTimeWindowTrimmer.generateTrimmedWindows(resource, sequence, minTimeData, data, sequencesAttributesProvider);
		final ICustomTimeWindowTrimmer customTrimmer = customTimeTrimmer == null ? null : customTimeTrimmer;

		if (customTrimmer != null) {
			customTrimmer.trimWindows(resource, list, minTimeData);
		}

		if (!(isADPScenario || isLongTermScenario) && usePNLBasedWindowTrimming && pnlBasedWindowTrimmerProvider != null) {
			pnlBasedWindowTrimmerProvider.trimWindows(resource, list, minTimeData);
		} else if (usePriceBasedWindowTrimming && priceBasedWindowTrimmerProvider != null) {
			priceBasedWindowTrimmerProvider.trimWindows(resource, list, minTimeData);
		}

		return new Pair<>(list, minTimeData);

	}

	@Subscribe
	public void startPhase(final OptimisationPhaseStartEvent event) {
		// TODO: Inspect settings and invalidate only if needed
		// E.g. gco on/off
		// E.g. lateness paremeter changes.
		clearCaches();
	}

	@Subscribe
	public void endPhase(final OptimisationPhaseEndEvent event) {
		if (GeneralCacheSettings.TimeWindowScheduler_RecordStats) {
			System.out.println("Time Scheduler: " + this);
			System.out.println("Time Scheduler: " + cache.stats());
		}
		clearCaches();
	}

	private void clearCaches() {
		cache.invalidateAll();
		if (pnlBasedWindowTrimmerProvider != null) {
			pnlBasedWindowTrimmerProvider.clearCaches();
		}
	}
}
