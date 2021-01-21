/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;

/**
 * An implementation of {@link IConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
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

		scheduler.setUseCanalBasedWindowTrimming(true);
		scheduler.setUsePriceBasedWindowTrimming(false);
		//We only need basic time scheduling otherwise will be slow.
		scheduler.setUsePNLBasedWindowTrimming(false);
		
		final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(sequences);

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = schedule.getTrimmedTimeWindowsMap();

		final Set<IPortSlot> currentUnbookedSlotsNorthbound = new HashSet<>();
		final Set<IPortSlot> currentUnbookedSlotsNorthboundInRelaxed = new HashSet<>();
		final Set<IPortSlot> currentUnbookedSlotsSouthbound = new HashSet<>();
		final Set<IPortSlot> currentUnbookedSlotsSouthboundInRelaxed = new HashSet<>();

		LOOP_RESOURCE: for (int r = 0; r < sequences.getResources().size(); r++) {
			final IResource resource = sequences.getResources().get(r);
			final ISequence sequence = sequences.getSequence(resource);

			// skip resources that are not scheduled
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			// Skip invalid resources
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE //
					|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE //
					|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				continue;
			}

			// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
			if (sequence.size() < 2) {
				continue;
			}

			final List<IPortTimeWindowsRecord> records = trimmedWindows.get(resource);
			for (final IPortTimeWindowsRecord record : records) {
				for (final IPortSlot slot : record.getSlots()) {
					// TODO - fill in details
					// note return slot not used in same way!
					if (record.getSlotNextVoyageOptions(slot) != AvailableRouteChoices.PANAMA_ONLY) {
						// not going through Panama, ignore
						continue;
					}
					if (record.getRouteOptionBooking(slot) != null) {
						// We have a booking, so ignore
						continue;
					}

					final PanamaPeriod panamaPeriod = record.getSlotNextVoyagePanamaPeriod(slot);
					if (panamaPeriod == PanamaPeriod.Relaxed || panamaPeriod == PanamaPeriod.Strict) {

						final boolean usesNorthBoundDirection = distanceProvider.getRouteOptionDirection(slot.getPort(), ERouteOption.PANAMA) == RouteOptionDirection.NORTHBOUND;

						if (panamaPeriod == PanamaPeriod.Relaxed) {
							// Record relaxed period slots
							if (usesNorthBoundDirection) {
								currentUnbookedSlotsNorthboundInRelaxed.add(slot);
							} else {
								currentUnbookedSlotsSouthboundInRelaxed.add(slot);
							}
						}
						// All slots, relaxed and strict
						if (usesNorthBoundDirection) {
							currentUnbookedSlotsNorthbound.add(slot);
						} else {
							currentUnbookedSlotsSouthbound.add(slot);
						}
					}
					if (panamaPeriod == PanamaPeriod.Beyond) {
						// Anything else after this voyage should also be beyond
						continue LOOP_RESOURCE;
					}
				}
			}
		}

		if (!initialState) {
			assert unbookedSlotsNorthbound != null;
			assert unbookedSlotsSouthbound != null;

			//////// strict constraint
			// Remove white list
			currentUnbookedSlotsNorthbound.removeAll(unbookedSlotsNorthbound);
			// Remove relaxed bookings
			currentUnbookedSlotsNorthbound.removeAll(currentUnbookedSlotsNorthboundInRelaxed);
			if (!currentUnbookedSlotsNorthbound.isEmpty()) {
				messages.add(String.format("%s: there are some unbooked northbound slots!", this.name));
				return false;
			}
			// Remove white list
			currentUnbookedSlotsSouthbound.removeAll(unbookedSlotsSouthbound);
			// Remove relaxed bookings
			currentUnbookedSlotsSouthbound.removeAll(currentUnbookedSlotsSouthboundInRelaxed);
			if (!currentUnbookedSlotsSouthbound.isEmpty()) {
				messages.add(String.format("%s: there are some unbooked southbound slots!", this.name));
				return false;
			}

			//////// relaxed constraint
			final boolean northboundIsValid;
			final boolean southboundIsValid;
			{
				// Total northbound slots in relaxed period.
				final int countBeforeNorthbound = currentUnbookedSlotsNorthboundInRelaxed.size(); // 0

				// Remove all initially seen slots (use all slots in case something has moved between strict and relaxed)
				currentUnbookedSlotsNorthboundInRelaxed.removeAll(unbookedSlotsNorthbound);

				// New count is the new stuff we have not seen before
				final int countAfterNorthbound = currentUnbookedSlotsNorthboundInRelaxed.size(); // 0

				// The delta is the number "whitelisted" slots from the initial solution
				final int whitelistedSlotCountNorthbound = (countBeforeNorthbound - countAfterNorthbound); // 6

				// What is out upper limit?
//				final int relaxedSlotCountNorthbound = panamaSlotsProvider.getRelaxedBookingCountNorthbound(); // 5

				// Calculate the adjusted upper bound. Total flex, minus the previously seen stuff. This gives us the remaining flex for new slots. This may be negative meaning we have no further
				// flex.
//				final int adjustedFlexCountNorthbound = relaxedSlotCountNorthbound - whitelistedSlotCountNorthbound; // -1

				// If count is zero, then there are no new slots in the relaxed period over the initial solution => accept.
				// Otherwise if the current excess is less than or equal to the adjusted flex => accept.
//				northboundIsValid = countAfterNorthbound == 0 || countAfterNorthbound <= adjustedFlexCountNorthbound;
			}
			if (!PanamaBookingHelper.isSouthboundIdleTimeRuleEnabled()) {
				//OLD southbound rule.
				final int countBeforeSouthbound = currentUnbookedSlotsSouthboundInRelaxed.size(); // 0
				currentUnbookedSlotsSouthboundInRelaxed.removeAll(unbookedSlotsSouthbound);
				final int countAfterSouthbound = currentUnbookedSlotsSouthboundInRelaxed.size(); // 0
				final int whitelistedSlotCountSouthbound = (countBeforeSouthbound - countAfterSouthbound); // 6

				final int relaxedSlotCountSouthbound = panamaSlotsProvider.getRelaxedBookingCountSouthbound(); // 5
				final int adjustedCountSouthbound = relaxedSlotCountSouthbound - whitelistedSlotCountSouthbound; // -1

				southboundIsValid = countAfterSouthbound == 0 || countAfterSouthbound <= adjustedCountSouthbound;
				if(!southboundIsValid)
					messages.add(String.format("%s: Panama booking count error!", this.name));
//			return northboundIsValid && southboundIsValid;
				return southboundIsValid;
			}
			
		} else {
			unbookedSlotsNorthbound = currentUnbookedSlotsNorthbound;
			unbookedSlotsSouthbound = currentUnbookedSlotsSouthbound;
		}
		return true;
	}
}
