package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class TimeWindowScheduler {

	@Inject
	private FeasibleTimeWindowTrimmer timeWindowTrimmer;

	@Inject
	private PriceBasedWindowTrimmer priceBasedWindowTrimmer;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	@Inject
	@Named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)
	private boolean usePriceBasedWindowTrimming = false;
	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean useCanalBasedWindowTrimming = false;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private static class CacheKey {

		private final IResource resource;
		private final ISequence sequence;
		private final Map<IPort, Set<IRouteOptionBooking>> unassignedBookings;
		private transient CurrentBookingData currentBookingData;

		public CacheKey(final IResource resource, final ISequence sequence, final CurrentBookingData currentBookingData) {
			this.resource = resource;
			this.sequence = sequence;
			this.currentBookingData = currentBookingData;
			this.unassignedBookings = new HashMap<>();
			if (currentBookingData.unassignedBookings != null) {
				for (final Map.Entry<IPort, Set<IRouteOptionBooking>> e : currentBookingData.unassignedBookings.entrySet()) {
					unassignedBookings.put(e.getKey(), new TreeSet<>(e.getValue()));
				}
			}
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

	private final @NonNull LHMCache<CacheKey, @Nullable Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData>> cache;

	public TimeWindowScheduler() {
		cache = new LHMCache<>("TimeWindowScheduler", (key) -> {

			// TODO: Better mechanism!
			timeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);

			final MinTravelTimeData minTimeData = new MinTravelTimeData(key.resource, key.sequence);
			final List<IPortTimeWindowsRecord> trimmedWindows = timeWindowTrimmer.generateTrimmedWindows(key.resource, key.sequence, minTimeData, key.currentBookingData);
			if (usePriceBasedWindowTrimming) {
				priceBasedWindowTrimmer.trimWindows(key.resource, trimmedWindows, minTimeData);
			}
			return new Pair<>(key, new Pair<>(trimmedWindows, minTimeData));
		}, 10_000);
	}

	public ScheduledTimeWindows schedule(final @NonNull ISequences sequences) {

		final Map<IResource, MinTravelTimeData> travelTimeDataMap = new HashMap<>();

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = new HashMap<IResource, List<IPortTimeWindowsRecord>>();

		final CurrentBookingData data = new CurrentBookingData();
		data.assignedBookings = new HashMap<IPort, Set<IRouteOptionBooking>>();
		data.unassignedBookings = new HashMap<IPort, Set<IRouteOptionBooking>>();
		panamaSlotsProvider.getBookings().entrySet().forEach(e -> {
			final Set<IRouteOptionBooking> assigned = e.getValue().stream().filter(j -> j.getPortSlot().isPresent()).collect(Collectors.toSet());
			data.assignedBookings.put(e.getKey(), new TreeSet<>(assigned));
			data.unassignedBookings.put(e.getKey(), new TreeSet<>(Sets.difference(e.getValue(), assigned)));
		});
		
		for (final IResource resource : sequences.getResources()) {

			List<IPortTimeWindowsRecord> list;
			if (hintEnableCache) {
				@Nullable
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = cache.get(new CacheKey(resource, sequences.getSequence(resource), data));
				if (p != null) {
					list = p.getFirst();
					travelTimeDataMap.put(resource, p.getSecond());
				} else {
					list = null;
				}

			} else {
				timeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);
				final MinTravelTimeData minTimeData = new MinTravelTimeData(resource, sequences.getSequence(resource));
				list = timeWindowTrimmer.generateTrimmedWindows(resource, sequences.getSequence(resource), minTimeData, data);

				if (usePriceBasedWindowTrimming) {
					priceBasedWindowTrimmer.trimWindows(resource, list, minTimeData);
				}
				travelTimeDataMap.put(resource, minTimeData);
			}
			trimmedWindows.put(resource, list);
			if (list != null) {
				for (final IPortTimeWindowsRecord rr : list) {
					for (final IPortSlot slot : rr.getSlots()) {
						final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
						if (booking != null) {
							data.unassignedBookings.remove(booking);
						}
					}
				}
			}
		}

		return new ScheduledTimeWindows(travelTimeDataMap, trimmedWindows);
	}

	public boolean isUsePriceBasedWindowTrimming() {
		return usePriceBasedWindowTrimming;
	}

	public boolean isUseCanalBasedWindowTrimming() {
		return useCanalBasedWindowTrimming;
	}

	public void setUseCanalBasedWindowTrimming(final boolean useCanalBasedWindowTrimming) {
		this.useCanalBasedWindowTrimming = useCanalBasedWindowTrimming;
	}

	public void setUsePriceBasedWindowTrimming(final boolean usePriceBasedWindowTrimming) {
		this.usePriceBasedWindowTrimming = usePriceBasedWindowTrimming;
	}
}
