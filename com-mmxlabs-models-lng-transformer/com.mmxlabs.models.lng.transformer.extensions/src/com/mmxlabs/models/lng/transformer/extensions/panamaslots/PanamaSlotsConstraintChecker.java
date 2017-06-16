/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.FeasibleTimeWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * An implementation of {@link IConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class PanamaSlotsConstraintChecker implements IConstraintChecker {

	private final String name;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	@Inject
	private FeasibleTimeWindowTrimmer scheduler;

	@Inject
	private IVesselProvider vesselProvider;

	private Set<IPortSlot> unbookedSlots;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private final @NonNull AbstractCache<@NonNull ISequences, @Nullable Map<IResource, List<IPortTimeWindowsRecord>>> cache;

	public PanamaSlotsConstraintChecker(final String name) {
		this.name = name;

		cache = new LHMCache<>("ScheduleCalculatorCache", (key) -> {

			// TODO: Better mechanism!
			scheduler.setTrimByPanamaCanalBookings(true);

			MinTravelTimeData minTimeData = new MinTravelTimeData(key);
			Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = scheduler.generateTrimmedWindows(key, minTimeData);

			return new Pair<>(key, trimmedWindows);
		}, 50_000);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows;
		if (hintEnableCache) {
			trimmedWindows = cache.get(sequences);
		} else {
			scheduler.setTrimByPanamaCanalBookings(true);
			MinTravelTimeData minTimeData = new MinTravelTimeData(sequences);
			trimmedWindows = scheduler.generateTrimmedWindows(sequences, minTimeData);
		}

		int strictBoundary = panamaSlotsProvider.getStrictBoundary();
		int relaxedBoundary = panamaSlotsProvider.getRelaxedBoundary();

		Set<IPortSlot> currentUnbookedSlots = new HashSet<>();
		Set<IPortSlot> currentUnbookedSlotsInRelaxed = new HashSet<>();

		// TODO: what to do with potential vessel return when end time is not specified?
		// TODO: nominal vessel
		// TODO: original solution might not be feasible at 16 knots and 24h early arrival time

		for (int r = 0; r < sequences.getResources().size(); r++) {
			IResource resource = sequences.getResources().get(r);
			ISequence sequence = sequences.getSequence(resource);

			// skip resources that are not scheduled
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				// TODO: Implement something here rather than rely on VoyagePlanner
				continue;
			}
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				// TODO: Implement something here rather than rely on VoyagePlanner
				continue;
			}

			// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
			if (sequence.size() < 2) {
				continue;
			}

			List<IPortTimeWindowsRecord> records = trimmedWindows.get(resource);
			IPortSlot prevSlot = null;
			for (IPortTimeWindowsRecord record : records) {
				for (IPortSlot slot : record.getSlots()) {
					// TODO - fill in details
					// note return slot not used in same way!
					if (record.getSlotNextVoyageOptions(slot) != AvailableRouteChoices.PANAMA_ONLY) {
						// not going through Panama, ignore
						continue;
					}
					if (record.getRouteOptionBooking(slot) != null) {
						// not going through Panama, ignore
						continue;
					}

					int windowStart = record.getSlotFeasibleTimeWindow(slot).getInclusiveStart();
					if (windowStart >= relaxedBoundary) {
						continue;
					}

					if (windowStart >= strictBoundary && windowStart < relaxedBoundary) {
						currentUnbookedSlotsInRelaxed.add(slot);
					}
					currentUnbookedSlots.add(slot);
				}
			}
		}

		if (unbookedSlots != null) {
			// strict constraint
			currentUnbookedSlots.removeAll(unbookedSlots);
			currentUnbookedSlots.removeAll(currentUnbookedSlotsInRelaxed);
			if (!currentUnbookedSlots.isEmpty()) {
				return false;
			}

			// relaxed constraint
			int countBefore = currentUnbookedSlots.size(); // 0
			currentUnbookedSlotsInRelaxed.removeAll(unbookedSlots);
			int countAfter = currentUnbookedSlotsInRelaxed.size(); // 0
			int whitelistedSlotCount = (countBefore - countAfter); // 6

			int relaxedSlotCount = panamaSlotsProvider.getRelaxedBookingCount(); // 5
			int newCount = relaxedSlotCount - whitelistedSlotCount; // -1

			if (countAfter == 0 || countAfter <= newCount) {
				return true;
			} else {
				return false;
			}
		} else {
			unbookedSlots = currentUnbookedSlots;
		}
		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
