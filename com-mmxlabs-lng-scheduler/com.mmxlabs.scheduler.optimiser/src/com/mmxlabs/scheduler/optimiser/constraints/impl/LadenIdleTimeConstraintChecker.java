/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * A constraint checker which tests whether the ports in a sequence can be reached from one another presuming the vessel travels at its maximum speed all the way and spends a minimum amount of time at
 * each point. Allows a certain quantity of lateness.
 * 
 * 
 * @author hinton
 * 
 * @param
 */
public class LadenIdleTimeConstraintChecker implements IPairwiseConstraintChecker {

	/**
	 * Max allowable idle time
	 */
	private static int MAX_IDLE_TIME_HOURS = 9 * 24;
	/**
	 * Nominal speed
	 */
	private static final int MAX_SPEED = 16_000;

	@NonNull
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IElementDurationProvider elementDurationProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	public LadenIdleTimeConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev = null;
		ISequenceElement cur = null;

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int maxSpeed = vesselAvailability.getVessel().getMaxSpeed();

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed, messages)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under the best possible circumstances, if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource
	 *            the vessel in question
	 * @return
	 */
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return checkPairwiseConstraint(first, second, resource, vesselAvailability.getVessel().getMaxSpeed(), messages);
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final int resourceMaxSpeed, final List<String> messages) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ITimeWindow tw1 = slot1.getTimeWindow();
			final ITimeWindow tw2 = slot2.getTimeWindow();

			if ((tw1 == null) || (tw2 == null)) {
				return true; // if the time windows are null, there is no effective constraint
			}

			final int voyageStartTime = tw1.getExclusiveEnd() - 1 + elementDurationProvider.getElementDuration(first, resource);

			@NonNull
			Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), slot1.getPort(), slot2.getPort(), MAX_SPEED,
					AvailableRouteChoices.OPTIMAL);
			if (quickestTravelTime.getSecond() == Integer.MAX_VALUE) {
				if (messages != null)
					messages.add(explain(first, second, resource));
				return false;
			}

			int travelTime = quickestTravelTime.getSecond();

			final int earliestArrivalTime = voyageStartTime + travelTime;
			final int idle = Math.max(tw2.getInclusiveStart() - earliestArrivalTime, 0);
			final boolean result = idle <= MAX_IDLE_TIME_HOURS;
			if (!result)
				explain(first, second, resource);
			return result;
		}
		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);
		final ITimeWindow tw1 = slot1.getTimeWindow();
		final ITimeWindow tw2 = slot2.getTimeWindow();

		assert tw1 != null;
		assert tw2 != null;

		final int distance = distanceProvider.getDistance(ERouteOption.DIRECT, slot1.getPort(), slot2.getPort(), vesselProvider.getVesselAvailability(resource).getVessel());

		if (distance == Integer.MAX_VALUE) {
			return "No edge connecting ports";
		}

		return "Excessive idle time : " + slot1.getPort().getName() + " to " + slot2.getPort().getName() + " = " + distance + ", but " + " start of first tw = " + tw1.getInclusiveStart()
				+ " and end of second = " + tw2.getExclusiveEnd();
	}

	public void setMaxIdleTimeInHours(int hours) {
		this.MAX_IDLE_TIME_HOURS = hours;
	}
}
