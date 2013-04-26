/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
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

	/**
	 * The maximum amount of lateness which will even be considered (20 days)
	 */
	private int maxLateness = 1000 * 24;
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
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	public TravelTimeConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		for (final Map.Entry<IResource, ISequence> entry : sequences.getSequences().entrySet()) {
			if (!checkSequence(entry.getValue(), entry.getKey())) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(final ISequence sequence, final IResource resource) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev, cur;
		prev = cur = null;

		final IVessel vessel = vesselProvider.getVessel(resource);
		final int maxSpeed = vessel.getVesselClass().getMaxSpeed();

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		return checkConstraints(sequences);
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

	@Override
	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under the best possible circumstances,
	 * if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource the vessel in question
	 * @return
	 */
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		return checkPairwiseConstraint(first, second, resource, vessel.getVesselClass().getMaxSpeed());
	}

	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, final int resourceMaxSpeed) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		final IVessel vessel = vesselProvider.getVessel(resource);
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

		final int distance = distanceProvider.getMinimumValue(slot1.getPort(), slot2.getPort());

		if (distance == Integer.MAX_VALUE) {
			return false;
		}

		final int travelTime = Calculator.getTimeFromSpeedDistance(resourceMaxSpeed, distance);
		final ITimeWindow tw1 = slot1.getTimeWindow();
		final ITimeWindow tw2 = slot2.getTimeWindow();

		if ((tw1 == null) || (tw2 == null)) {
			return true; // if the time windows are null, there is no effective constraint
		}

		final int earliestArrivalTime = tw1.getStart() + elementDurationProvider.getElementDuration(first, resource) + travelTime;

		final int latestAllowableTime = tw2.getEnd() + maxLateness;

		return earliestArrivalTime < latestAllowableTime;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);
		final int distance = distanceProvider.get(IMultiMatrixProvider.Default_Key).get(slot1.getPort(), slot2.getPort());
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
