/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.scheduling.FeasibleTimeWindowTrimmer.PanamaSegments;

public final class CurrentBookingData {
	private final Map<ECanalEntry, Set<IRouteOptionBooking>> usedBookings = new HashMap<>();
	private final Map<ECanalEntry, List<IRouteOptionBooking>> unusedBookings = new HashMap<>();
	
	/**
	 * Perform a deep copy of the this CurrentBookingData object. This is important since the data structure
	 * is not immutable and will be modified by the Trimmer code amongst other things and failure to do a 
	 * deep copy could effect the correct calculation of the #hashCode() of CacheKey objects.
	 * @return a deep copy down to the level of the sets and lists.
	 */
	public CurrentBookingData copy() {
		final CurrentBookingData newBookingData = new CurrentBookingData();
		newBookingData.usedBookings.putAll(this.usedBookings);
		for (var usedBookingsDirectionalEntry : this.usedBookings.entrySet()) {
			var direction = usedBookingsDirectionalEntry.getKey();
			var usedBookingSet = usedBookingsDirectionalEntry.getValue();
			if (usedBookingSet != null) {
				newBookingData.usedBookings.put(direction, new HashSet<>(usedBookingSet));
			}
		}
		newBookingData.unusedBookings.putAll(this.unusedBookings);
		for (var unusedBookingsDirectionalEntry : this.unusedBookings.entrySet()) {
			var direction = unusedBookingsDirectionalEntry.getKey();
			var unusedBookingList = unusedBookingsDirectionalEntry.getValue();
			if (unusedBookingList != null) {
				//Copying contents of an array list should be slightly, only slightly though faster than a LinkedList.
				newBookingData.unusedBookings.put(direction, new ArrayList<>(unusedBookingList));
			}
		}
		return newBookingData;
	}

	public void addBooking(final IRouteOptionBooking booking) {
		final List<IRouteOptionBooking> unusedBookingsForEntrance = getUnusedSetForEntrance(booking.getEntryPoint());
		assert (!unusedBookingsForEntrance.contains(booking));
		unusedBookingsForEntrance.add(booking);
	}

	private List<IRouteOptionBooking> getUnusedSetForEntrance(final ECanalEntry canalEntrance) {
		return unusedBookings.computeIfAbsent(canalEntrance, k -> new LinkedList<>());
	}

	public Collection<IRouteOptionBooking> getUnusedBookings(final ECanalEntry canalEntrance) {
		return Collections.unmodifiableCollection(getUnusedSetForEntrance(canalEntrance));
	}

	private Set<IRouteOptionBooking> getUsedSetForEntrance(final ECanalEntry canalEntrance) {
		return usedBookings.computeIfAbsent(canalEntrance, k -> new HashSet<>());
	}

	public void setUsed(final ECanalEntry canalEntrance, final IRouteOptionBooking booking) {
		final Set<IRouteOptionBooking> usedBookingsForEntrance = getUsedSetForEntrance(canalEntrance);
		assert !usedBookingsForEntrance.contains(booking);
		usedBookingsForEntrance.add(booking);
		final List<IRouteOptionBooking> unusedBookingsForEntrance = getUnusedSetForEntrance(canalEntrance);
		unusedBookingsForEntrance.remove(booking);
	}

	public boolean isUsed(final ECanalEntry canalEntrance, final IRouteOptionBooking booking) {
		final Set<IRouteOptionBooking> usedBookingsForEntrance = getUsedSetForEntrance(canalEntrance);
		return usedBookingsForEntrance.contains(booking);
	}

	public Collection<IRouteOptionBooking> getUsedBookings(final ECanalEntry canalEntrance) {
		return Collections.unmodifiableCollection(getUsedSetForEntrance(canalEntrance));
	}

	/**
	 * Return time sorted unused bookings allocated to a specific vessel.
	 * 
	 * @return
	 */
	public List<IRouteOptionBooking> getUnusedBookingsForSpecificVessel(@NonNull IVessel vessel, final ECanalEntry panamaEntry) {
		final List<IRouteOptionBooking> vesselBookings = getUnusedSetForEntrance(panamaEntry).stream() //
				// Make sure there is only one vessel on the booking
				.filter(e -> e.getVessels().size() == 1) //
				// .. and it is this sequence's vessel
				.filter(e -> e.getVessels().contains(vessel)) //
				.collect(Collectors.toList());
		
		return vesselBookings;
	}

	/**
	 * Return time sorted unused bookings allocated to a group of vessels. I.e. default group or specific group.
	 * 
	 * @return
	 */
	public List<IRouteOptionBooking> getUnusedBookingsForMultipleVessels(@NonNull IVessel vessel, final ECanalEntry panamaEntry) {

		final List<IRouteOptionBooking> vesselBookings = getUnusedSetForEntrance(panamaEntry).stream() //
				// Check for bookings with multiple vessels (i.e. created with booking code)
				.filter(e -> e.getVessels().size() != 1) //
				// .. and it is this sequence's vessel or is the set empty and free to go anywhere?
				.filter(e -> e.getVessels().isEmpty() || e.getVessels().contains(vessel)) //
				.collect(Collectors.toList());

		return vesselBookings;
	}

	public boolean isBookingFeasible(final IRouteOptionBooking booking, int originWindowStart, int destinationWindowEnd, PanamaSegments segments) {
		return isBookingFeasible(booking, originWindowStart, destinationWindowEnd, segments.travelTimeToCanalWithMargin, segments.travelTimeFromCanal);
	}

	/**
	 * Is the booking feasible given the time windows and travel times?
	 * 
	 * @param booking
	 * @param originWindowStart
	 * @param destinationWindowEnd
	 * @param compositeTimeToCanal
	 *            This is the origin visit duration, travel time to the canal and extra arrival time margin.
	 * @param timeFromCanal
	 *            Travel time from the canal to the destination
	 * @return
	 */
	public boolean isBookingFeasible(final IRouteOptionBooking booking, int originWindowStart, int destinationWindowEnd, int compositeTimeToCanal, int timeFromCanal) {
		// Can we reach the booking in time?
		final int canalTime = originWindowStart + compositeTimeToCanal;
		if (canalTime > booking.getBookingDate()) {
			return false;
		}

		// Is there still time to reach our destination?
		if (booking.getBookingDate() + timeFromCanal < destinationWindowEnd) {
			return true;
		}
		return false;
	}
}
