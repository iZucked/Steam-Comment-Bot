package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A constraint checker which tests whether the ports in a sequence can be
 * reached from one another presuming the vessel travels at its maximum speed
 * all the way and spends a minimum amount of time at each point.
 * 
 * Currently only actually checks the pairwise constraint, because it is only
 * interesting to pairwise constraint users, but it might be useful to implement
 * in general as a quick first-pass rejection of routes which contain a leg
 * which cannot be made in the available time.
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class TravelTimeConstraintChecker<T> implements
		IPairwiseConstraintChecker<T> {

	private final String name;
	private IOptimisationData<T> data;
	private IPortSlotProvider<T> portSlotProvider;
	private IVesselProvider vesselProvider;
	private IElementDurationProvider<T> elementDurationProvider;
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	public TravelTimeConstraintChecker(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(ISequences<T> sequences) {
		for (Map.Entry<IResource, ISequence<T>> entry : sequences
				.getSequences().entrySet()) {
			if (!checkSequence(entry.getValue(), entry.getKey()))
				return false;
		}
		return true;
	}

	private boolean checkSequence(ISequence<T> sequence, IResource resource) {
		Iterator<T> iter = sequence.iterator();
		T prev, cur;
		prev = cur = null;

		final IVessel vessel = vesselProvider.getVessel(resource);
		final int maxSpeed = vessel.getVesselClass().getMaxSpeed();
		final IMatrixProvider<IPort, Integer> distanceMatrix = distanceProvider
				.get(IMultiMatrixProvider.Default_Key);
		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed, distanceMatrix))
					return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(ISequences<T> sequences,
			List<String> messages) {
		return checkConstraints(sequences);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOptimisationData(IOptimisationData<T> optimisationData) {
		this.data = optimisationData;
		this.portSlotProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider,
				IPortSlotProvider.class);
		this.vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		this.elementDurationProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class);
		this.distanceProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMultiMatrixProvider.class);
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
	public boolean checkPairwiseConstraint(T first, T second, IResource resource) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		return checkPairwiseConstraint(first, second, resource, vessel
				.getVesselClass().getMaxSpeed(),
				distanceProvider.get(IMultiMatrixProvider.Default_Key));
	}

	public boolean checkPairwiseConstraint(final T first, final T second,
			final IResource resource, final int resourceMaxSpeed,
			final IMatrixProvider<IPort, Integer> distanceMatrix) {
		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		final int travelTime = Calculator.getTimeFromSpeedDistance(
				resourceMaxSpeed,
				distanceMatrix.get(slot1.getPort(), slot2.getPort()));

		final int earliestArrivalTime = slot1.getTimeWindow().getStart()
				+ elementDurationProvider.getElementDuration(first, resource)
				+ travelTime;

		final int latestAllowableTime = slot2.getTimeWindow().getEnd();

		return earliestArrivalTime < latestAllowableTime;
	}

}
