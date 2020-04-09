/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class TimeWindowScheduler implements IArrivalTimeScheduler {

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
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean useCanalBasedWindowTrimming = false;

	@Inject
	@Named(SchedulerConstants.Key_ArrivalTimeCache)
	private CacheMode cacheMode;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private static class CacheKey {

		private final IResource resource;
		private final ISequence sequence;
		// This data is used as the key
		private final Map<ECanalEntry, List<IRouteOptionBooking>> unassignedBookings;
		// This data will be modified by the scheduler
		private transient CurrentBookingData currentBookingData;

		public CacheKey(final IResource resource, final ISequence sequence, final CurrentBookingData _currentBookingData) {
			this.resource = resource;
			this.sequence = sequence;
			// Copy unassigned elements for use in key
			this.unassignedBookings = new HashMap<>();
			if (_currentBookingData.unassignedBookings != null) {
				for (final Map.Entry<ECanalEntry, List<IRouteOptionBooking>> e : _currentBookingData.unassignedBookings.entrySet()) {
					this.unassignedBookings.put(e.getKey(), new ArrayList<>(e.getValue()));
				}
			}
			// used to evaluate
			this.currentBookingData = _currentBookingData;
		}

		@Override
		public int hashCode() {
			return Objects.hash(resource, sequence, unassignedBookings);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				return resource == other.resource //
						&& Objects.equals(sequence, other.sequence) //
						&& Objects.equals(unassignedBookings, other.unassignedBookings);

			}
			return false;
		}
	};

	private final boolean recordStats = false;

	private final @NonNull LoadingCache<CacheKey, Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData>> cache;

	@Inject
	public TimeWindowScheduler(@Named(SchedulerConstants.CONCURRENCY_LEVEL) int concurrencyLevel) {
		int cacheSize = 100_000;
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(cacheSize);

		if (recordStats) {
			builder = builder.recordStats();
		}
		this.cache = builder //
				.build(new CacheLoader<CacheKey, Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData>>() {

					@Override
					public Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> load(final CacheKey key) throws Exception {
						return doTrimming(key.resource, key.sequence, key.currentBookingData);

					}

				});
	}

	public ScheduledTimeWindows calculateTrimmedWindows(final @NonNull ISequences sequences) {
		final Map<IResource, MinTravelTimeData> travelTimeDataMap = new HashMap<>();

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = new HashMap<>();

		// Construct new bookings data object
		final CurrentBookingData data = new CurrentBookingData();
		data.assignedBookings = new HashMap<>();
		data.unassignedBookings = new HashMap<>();

		if (useCanalBasedWindowTrimming) {
			panamaSlotsProvider.getAssignedBookings().entrySet().forEach(e -> {
				data.assignedBookings.put(e.getKey(), new ArrayList<>(e.getValue()));
			});
			panamaSlotsProvider.getUnassignedBookings().entrySet().forEach(e -> {
				data.unassignedBookings.put(e.getKey(), new ArrayList<>(e.getValue()));
			});
		}

		for (final IResource resource : sequences.getResources()) {

			List<IPortTimeWindowsRecord> list;
			@NonNull
			final ISequence sequence = sequences.getSequence(resource);
			if (hintEnableCache && cacheMode != CacheMode.Off) {

				@Nullable
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = cache.getUnchecked(new CacheKey(resource, sequence, data.copy()));
				if (p != null) {
					list = p.getFirst();
					travelTimeDataMap.put(resource, p.getSecond());
				} else {
					list = null;
				}

				// VERIFY
				if (cacheMode == CacheMode.Verify) {
					final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p2 = doTrimming(resource, sequence, data);
					final List<IPortTimeWindowsRecord> list2 = p2.getFirst();

					if (list != null) {
						if (false) {
							// For more detailed debugging.
							doDetailedBookingVerification(data, resource, list, sequence, list2);
						}
						assert Objects.equals(list, list2);
					}
				}
			} else {
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = doTrimming(resource, sequence, data);
				list = p.getFirst();
				travelTimeDataMap.put(resource, p.getSecond());
			}
			// Copy list incase externally modified
			trimmedWindows.put(resource, new ArrayList<>(list));

			// We copy the booking data as we don't know whether or not cache will hit or
			// miss.
			// Modify the original to remove used bookings
			if (list != null) {
				for (final IPortTimeWindowsRecord rr : list) {
					for (final IPortSlot slot : rr.getSlots()) {
						final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
						if (booking != null) {
							data.unassignedBookings.get(booking.getEntryPoint()).remove(booking);
						}
					}
				}
			}
		}

		return new ScheduledTimeWindows(travelTimeDataMap, trimmedWindows);
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

				if (usePNLBasedWindowTrimming) {
					// Use the simple variant. We have computed the necessary values.
					portTimeRecords.put(resource, portTimesRecordMaker.makeSimpleShippedPortTimesRecords(seqIndex, resource, sequence, trimmedWindows.get(resource), travelTimeData));
				} else {
					// Use the more complex variant. Window start is a hint rather than abolute
					// value.
					portTimeRecords.put(resource, portTimesRecordMaker.calculateShippedPortTimesRecords(seqIndex, resource, sequence, trimmedWindows.get(resource), travelTimeData));
				}
			}
		}

		return portTimeRecords;
	}

	private void doDetailedBookingVerification(final CurrentBookingData data, final IResource resource, List<IPortTimeWindowsRecord> list, final ISequence sequence,
			final List<IPortTimeWindowsRecord> list2) {
		if (false) {
			// DEBUG - Verify used bookings were are in the unassigned list. Note assumes
			// not assignedbooking exists
			final List<IRouteOptionBooking> l1 = new ArrayList<IRouteOptionBooking>();
			for (final IPortTimeWindowsRecord rr : list) {
				for (final IPortSlot slot : rr.getSlots()) {
					final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
					if (booking != null) {
						l1.add(booking);

						final List<IRouteOptionBooking> treeSet = data.unassignedBookings.get(booking.getEntryPoint());
						if (!treeSet.contains(booking)) {
							final int ii = 0;
						}
					}
				}
			}

			final List<IRouteOptionBooking> l2 = new ArrayList<IRouteOptionBooking>();
			for (final IPortTimeWindowsRecord rr : list2) {
				for (final IPortSlot slot : rr.getSlots()) {
					final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
					if (booking != null) {
						l2.add(booking);

						if (!data.unassignedBookings.get(booking.getEntryPoint()).contains(booking)) {
							final int ii = 0;
						}
					}
				}
			}
		}

		if (false) {
			// DEBUG - See which record fails
			if (!Objects.equals(list, list2)) {
				final Iterator<IPortTimeWindowsRecord> e1 = list.iterator();
				final Iterator<IPortTimeWindowsRecord> e2 = list2.iterator();
				while (e1.hasNext() && e2.hasNext()) {
					final IPortTimeWindowsRecord o1 = e1.next();
					final IPortTimeWindowsRecord o2 = e2.next();
					if (!(o1.equals(o2))) {
						// DEBUG BREAKPOINT HERE
						o1.equals(o2);
						final MinTravelTimeData minTimeData2 = new MinTravelTimeData(resource, sequence);
						final List<IPortTimeWindowsRecord> list3 = timeWindowTrimmerProvider.get().generateTrimmedWindows(resource, sequence, minTimeData2, data.copy());

					}
					// return false;
				}
				final boolean r = !(e1.hasNext() || e2.hasNext());
			}
		}
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

	private Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> doTrimming(final IResource resource, final ISequence sequence, final CurrentBookingData data) {

		FeasibleTimeWindowTrimmer feasibleTimeWindowTrimmer = timeWindowTrimmerProvider.get();
		feasibleTimeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);
		final MinTravelTimeData minTimeData = new MinTravelTimeData(resource, sequence);
		final List<IPortTimeWindowsRecord> list = feasibleTimeWindowTrimmer.generateTrimmedWindows(resource, sequence, minTimeData, data);
		final ICustomTimeWindowTrimmer customTrimmer = customTimeTrimmer == null ? null : customTimeTrimmer;

		if (customTrimmer != null) {
			customTrimmer.trimWindows(resource, list, minTimeData);
		}

		if (usePNLBasedWindowTrimming && pnlBasedWindowTrimmerProvider != null) {
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
		if (recordStats) {
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
