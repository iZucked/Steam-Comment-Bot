/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A constraint checker which tests whether the ports in a sequence can be reached from one another presuming the vessel travels at its maximum speed all the way and spends a minimum amount of time at
 * each point. Allows a certain quantity of lateness.
 * 
 * 
 * @author hinton
 * 
 * @param
 */
public class TravelTimeConstraintChecker implements IPairwiseConstraintChecker {
	public final static String OPTIMISER_START_ELEMENT_FIX = "enableStartElementOptimisation";

	@Inject
	@Named(TravelTimeConstraintChecker.OPTIMISER_START_ELEMENT_FIX)
	private boolean START_ELEMENT_OPTIMISATION_FIX; // if set to true it fixes a bug that prevents start elements from being valid preceders

	/**
	 * The maximum amount of lateness which will even be considered (20 days)
	 */
	private int maxLateness = 1000 * 24; // note, this particular value is never used, as calls to setMaxLateness() are made

	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IPortTypeProvider portTypeProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IElementDurationProvider elementDurationProvider;

	@Inject
	@NonNull
	private IDistanceProvider distanceProvider;

	@Inject
	@NonNull
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	@NonNull
	private IActualsDataProvider actualsDataProvider;

	public TravelTimeConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences) {
		for (final Map.Entry<IResource, ISequence> entry : sequences.getSequences().entrySet()) {
			final IResource resource = entry.getKey();
			assert resource != null;
			final ISequence sequence = entry.getValue();
			assert sequence != null;
			if (!checkSequence(sequence, resource)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev, cur;
		prev = cur = null;

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int maxSpeed = vesselAvailability.getVessel().getVesselClass().getMaxSpeed();

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final List<String> messages) {
		return checkConstraints(sequences);
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

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
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return checkPairwiseConstraint(first, second, resource, vesselAvailability.getVessel().getVesselClass().getMaxSpeed());
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final int resourceMaxSpeed) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vessel = vesselProvider.getVesselAvailability(resource);
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);
		if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			final ITimeWindow tw1 = slot1.getTimeWindow();
			final ITimeWindow tw2 = slot2.getTimeWindow();

			if ((tw1 == null) || (tw2 == null)) {
				return true; // if the time windows are null, there is no effective constraint
			}

			if (firstType == PortType.Load) {
				if (secondType == PortType.Discharge) {

					// See ShippingHoursRestrictions otherwise
					// if (slot1.getPort() == slot2.getPort()) {
					if (!shippingHoursRestrictionProvider.isDivertable(first)) {
						if (tw1.getStart() <= tw2.getStart() && tw1.getEnd() >= tw2.getStart()) {
							return true;
						}
						if (tw1.getStart() <= tw2.getEnd() && tw1.getEnd() >= tw2.getEnd()) {
							return true;
						}

						if (tw2.getStart() <= tw1.getStart() && tw2.getEnd() >= tw1.getStart()) {
							return true;
						}
						if (tw2.getStart() <= tw1.getEnd() && tw2.getEnd() >= tw1.getEnd()) {
							return true;
						}
						return false;
					}
				}
			}
			return true;
		}

		if (vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
			// Ignore problems with short cargoes between discharge and next load
			if (firstType == PortType.Start && secondType == PortType.End) {
				return true;
			}
			if (firstType == PortType.Discharge) {
				return true;
			}
		}

		final List<Pair<ERouteOption, Integer>> distanceValues = distanceProvider.getDistanceValues(slot1.getPort(), slot2.getPort());
		int distance = Integer.MAX_VALUE;
		for (final Pair<ERouteOption, Integer> distanceOption : distanceValues) {
			final int routeDistance = distanceOption.getSecond();
			if (routeDistance < distance) {
				distance = routeDistance;
			}
		}

		// final int distance = distanceProvider.getMinimumValue(slot1.getPort(), slot2.getPort());

		if (distance == Integer.MAX_VALUE) {
			return false;
		}

		// FIXME: TRANSIT TIME
		final int travelTime = Calculator.getTimeFromSpeedDistance(resourceMaxSpeed, distance);
		final ITimeWindow tw1 = slot1.getTimeWindow();
		final ITimeWindow tw2 = slot2.getTimeWindow();

		if ((tw1 == null) || (tw2 == null)) {
			return true; // if the time windows are null, there is no effective constraint
		}

		final int earliestArrivalTime = tw1.getStart() + elementDurationProvider.getElementDuration(first, resource) + travelTime;

		final int latestAllowableTime = tw2.getEnd() + maxLateness;

		if (START_ELEMENT_OPTIMISATION_FIX) {
			return earliestArrivalTime <= latestAllowableTime;
		} else {
			return earliestArrivalTime < latestAllowableTime;
		}
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);
		final int distance = distanceProvider.getDistance(ERouteOption.DIRECT, slot1.getPort(), slot2.getPort());
		if (distance == Integer.MAX_VALUE) {
			return "No edge connecting ports";
		}
		final ITimeWindow tw1 = slot1.getTimeWindow();
		final ITimeWindow tw2 = slot2.getTimeWindow();

		return "Excessive lateness : " + slot1.getPort().getName() + " to " + slot2.getPort().getName() + " = " + distance + ", but " + " start of first tw = " + tw1.getStart()
				+ " and end of second = " + tw2.getEnd();
	}

	public int getMaxLateness() {
		return maxLateness;
	}

	public void setMaxLateness(final int maxLateness) {
		this.maxLateness = maxLateness;
	}

}
