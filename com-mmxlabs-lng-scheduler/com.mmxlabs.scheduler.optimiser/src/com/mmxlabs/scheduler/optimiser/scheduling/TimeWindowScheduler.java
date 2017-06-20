package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		// This data is used as the key
		private final Map<IPort, Set<IRouteOptionBooking>> unassignedBookings;
		// This data will be modified by the scheduler
		private transient CurrentBookingData currentBookingData;

		public CacheKey(final IResource resource, final ISequence sequence, final CurrentBookingData _currentBookingData) {
			this.resource = resource;
			this.sequence = sequence;
			// Copy unassigned elements for use in key
			this.unassignedBookings = new HashMap<>();
			if (_currentBookingData.unassignedBookings != null) {
				for (final Map.Entry<IPort, TreeSet<IRouteOptionBooking>> e : _currentBookingData.unassignedBookings.entrySet()) {
					this.unassignedBookings.put(e.getKey(), new TreeSet<>(e.getValue()));
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

		// Construct new bookings data object
		final CurrentBookingData data = new CurrentBookingData();
		data.assignedBookings = new HashMap<IPort, Set<IRouteOptionBooking>>();
		data.unassignedBookings = new HashMap<IPort, TreeSet<IRouteOptionBooking>>();

		panamaSlotsProvider.getBookings().entrySet().forEach(e -> {
			final Set<IRouteOptionBooking> assigned = e.getValue().stream().filter(j -> j.getPortSlot().isPresent()).collect(Collectors.toSet());
			data.assignedBookings.put(e.getKey(), new TreeSet<>(assigned));
			data.unassignedBookings.put(e.getKey(), new TreeSet<>(Sets.difference(e.getValue(), assigned)));
		});

		for (final IResource resource : sequences.getResources()) {

			List<IPortTimeWindowsRecord> list;
			@NonNull
			final ISequence sequence = sequences.getSequence(resource);
			if (hintEnableCache) {

				@Nullable
				final Pair<List<IPortTimeWindowsRecord>, MinTravelTimeData> p = cache.get(new CacheKey(resource, sequence, data.copy()));
				if (p != null) {
					list = p.getFirst();
					travelTimeDataMap.put(resource, p.getSecond());
				} else {
					list = null;
				}

				// VERIFY
				if (false) {

					timeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);
					final MinTravelTimeData minTimeData = new MinTravelTimeData(resource, sequence);
					final List<IPortTimeWindowsRecord> list2 = timeWindowTrimmer.generateTrimmedWindows(resource, sequence, minTimeData, data.copy());

					if (usePriceBasedWindowTrimming) {
						priceBasedWindowTrimmer.trimWindows(resource, list2, minTimeData);
					}
					if (list != null) {
						if (false) {
							// DEBUG - Verify used bookings were are in the unassigned list. Note assumes not assignedbooking exists
							final List<IRouteOptionBooking> l1 = new ArrayList<IRouteOptionBooking>();
							for (final IPortTimeWindowsRecord rr : list) {
								for (final IPortSlot slot : rr.getSlots()) {
									final IRouteOptionBooking booking = rr.getRouteOptionBooking(slot);
									if (booking != null) {
										l1.add(booking);

										final TreeSet<IRouteOptionBooking> treeSet = data.unassignedBookings.get(booking.getEntryPoint());
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
										final List<IPortTimeWindowsRecord> list3 = timeWindowTrimmer.generateTrimmedWindows(resource, sequence, minTimeData2, data.copy());

									}
									// return false;
								}
								final boolean r = !(e1.hasNext() || e2.hasNext());
							}
						}
						assert Objects.equals(list, list2);
					}
				}
			} else {
				timeWindowTrimmer.setTrimByPanamaCanalBookings(useCanalBasedWindowTrimming);
				final MinTravelTimeData minTimeData = new MinTravelTimeData(resource, sequence);
				list = timeWindowTrimmer.generateTrimmedWindows(resource, sequence, minTimeData, data);

				if (usePriceBasedWindowTrimming) {
					priceBasedWindowTrimmer.trimWindows(resource, list, minTimeData);
				}
				travelTimeDataMap.put(resource, minTimeData);
			}
			// Copy list incase externaly modified
			trimmedWindows.put(resource, new ArrayList<>(list));

			// We copy the booking data as we don't know whether or not cache will hit or miss.
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
