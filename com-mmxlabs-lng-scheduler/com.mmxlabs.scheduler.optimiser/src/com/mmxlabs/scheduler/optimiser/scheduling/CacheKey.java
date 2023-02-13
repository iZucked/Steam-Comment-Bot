/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;

public final class CacheKey {

	public final IResource resource;
	public final ISequence sequence;
	public final ISequencesAttributesProvider sequencesAttributesProvider;
	// This data will be modified by the scheduler
	public final CurrentBookingData currentBookingData;
	
	// This data is used as the key
	private final Map<ECanalEntry, List<IRouteOptionBooking>> unusedBookings;

	public CacheKey(final IResource resource, final ISequence sequence, final CurrentBookingData _currentBookingData, ISequencesAttributesProvider sequencesAttributesProvider) {
		this.resource = resource;
		this.sequence = sequence;
		this.sequencesAttributesProvider = sequencesAttributesProvider;
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
		return Objects.hash(resource, sequence, unusedBookings, sequencesAttributesProvider);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof CacheKey other) {
			return resource == other.resource //
					&& Objects.equals(sequence, other.sequence) //
					&& Objects.equals(unusedBookings, other.unusedBookings) //
					&& Objects.equals(sequencesAttributesProvider, other.sequencesAttributesProvider);

		}
		return false;
	}
}