package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;

public class CacheKey {

	final IResource resource;
	final ISequence sequence;
	// This data is used as the key
	private final Map<ECanalEntry, List<IRouteOptionBooking>> unusedBookings;
	// This data will be modified by the scheduler
	final CurrentBookingData currentBookingData;

	public CacheKey(final IResource resource, final ISequence sequence, final CurrentBookingData _currentBookingData) {
		this.resource = resource;
		this.sequence = sequence;
		// Copy unassigned elements for use in key
		this.unusedBookings = new HashMap<>();
		for (ECanalEntry e : ECanalEntry.values()) {
			this.unusedBookings.put(e, new ArrayList<>(_currentBookingData.getUnusedBookings(e)));
		}
		// used to evaluate
		this.currentBookingData = _currentBookingData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(resource, sequence, unusedBookings);
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
					&& Objects.equals(unusedBookings, other.unusedBookings);

		}
		return false;
	}
}