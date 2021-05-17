/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * Prevents new, Panama only routes without bookings, being introduced into the solution, which are not present in the initial solution
 * where there is not sufficient slack in the schedule to wait at the Panama crossing for the prescribed number of waiting days.
 */
public class PanamaSlotsConstraintChecker implements IInitialSequencesConstraintChecker {

	private final @NonNull String name;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private TimeWindowScheduler scheduler;

	@Inject
	private IVesselProvider vesselProvider;

	private Set<@NonNull IPortSlot> unbookedSlotsNorthbound;
	private Set<@NonNull IPortSlot> unbookedSlotsSouthbound;

	public PanamaSlotsConstraintChecker(final @NonNull String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		// Should really be false, but action set code does not call sequencedAccepted
		return checkConstraints(sequences, unbookedSlotsNorthbound == null, messages);
	}

	@Override
	public void sequencesAccepted(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, final List<String> messages) {
		checkConstraints(fullSequences, true, messages);
	}

	public boolean checkConstraints(final ISequences sequences, final boolean initialState, final List<String> messages) {

		//We only need basic time scheduling with canal trimming on.
		scheduler.setUseCanalBasedWindowTrimming(true);
		scheduler.setUsePriceBasedWindowTrimming(false);
		scheduler.setUsePNLBasedWindowTrimming(false);
		
		final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(sequences);

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = schedule.getTrimmedTimeWindowsMap();

		// For now, we don't need to check this constraint + above left for client runtime buffering,
		// in case in future we need to re-enable this or similar logic, as per Simon.
		if (true) {
			return true;
		}
		
		final Set<IPortSlot> currentUnbookedSlotsNorthbound = new HashSet<>();
		final Set<IPortSlot> currentUnbookedSlotsSouthbound = new HashSet<>();
		final Map<IPortSlot, IPortTimeWindowsRecord> portSlotToTimeWindowsRecord = new HashMap<>();
		
		for (int r = 0; r < sequences.getResources().size(); r++) {
			final IResource resource = sequences.getResources().get(r);
			final ISequence sequence = sequences.getSequence(resource);

			// skip resources that are not scheduled, invalid resources
			// (+filter out solutions with less than 2 elements i.e. spot charters, etc.)
			if (realShippedVoyage(resource) && possiblePanamaJourney(sequence)) {

				final List<IPortTimeWindowsRecord> records = trimmedWindows.get(resource);
				for (final IPortTimeWindowsRecord record : records) {
					for (final IPortSlot slot : record.getSlots()) {
						
						 // can only go through Panama and we do not have a booking
						if (record.getSlotNextVoyageOptions(slot) == AvailableRouteChoices.PANAMA_ONLY &&
							record.getRouteOptionBooking(slot) == null) { 
								
							// Get the direction.
							final boolean usesNorthBoundDirection = distanceProvider.getRouteOptionDirection(slot.getPort(), ERouteOption.PANAMA) == RouteOptionDirection.NORTHBOUND;

							// Record the slot without a booking.
							if (usesNorthBoundDirection) {
								currentUnbookedSlotsNorthbound.add(slot);
							} else {
								currentUnbookedSlotsSouthbound.add(slot);
							}
						}
					}
				}
			}
		}

		if (!initialState) {
			assert unbookedSlotsNorthbound != null;
			assert unbookedSlotsSouthbound != null;
			
			//Remove white listed violations present in initial solution.
			currentUnbookedSlotsNorthbound.removeAll(unbookedSlotsNorthbound);
		
			if (!currentUnbookedSlotsNorthbound.isEmpty()) {
				return false;
			}
			
			//Remove white listed violations present in initial solution.
			currentUnbookedSlotsSouthbound.removeAll(unbookedSlotsSouthbound);

			if (!currentUnbookedSlotsSouthbound.isEmpty()) {
				return false;
			}
			
		} else {
			unbookedSlotsNorthbound = currentUnbookedSlotsNorthbound;
			unbookedSlotsSouthbound = currentUnbookedSlotsSouthbound;
		}
		return true;
	}
	
	private boolean realShippedVoyage(final IResource resource) {
		// skip resources that are not scheduled
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return !(vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP);
	}
	
	private boolean possiblePanamaJourney(final ISequence sequence) {
		// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
		return sequence.size() > 1;
	}
}
