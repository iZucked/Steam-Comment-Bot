package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A utility class for testing whether pairs of sequence elements can be scheduled next to one another.
 * Typical usage is to construct it with some context and then call {@code allowSequence(e1, e2)} to determine if e1 can follow e2.
 * @author hinton
 *
 */
public class LegalSequencingChecker<T> {
//	private IOptimisationContext<T> context;
	private IPortTypeProvider<T> portTypeProvider;
	private IPortSlotProvider<T> portSlotProvider;
	private IElementDurationProvider<T> elementDurationProvider;
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;
	private IVesselProvider vesselProvider;
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationProvider;
	private IOrderedSequenceElementsDataComponentProvider<T> orderedSequenceProvider;
	private Collection<IResource> allResources;
	
	public LegalSequencingChecker(IOptimisationContext<T> context) {
		this(context.getOptimisationData());			
	}
	
	@SuppressWarnings("unchecked")
	public LegalSequencingChecker(IOptimisationData<T> data) {
		this.portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		this.portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		this.elementDurationProvider = data.getDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, IElementDurationProvider.class);
		this.distanceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);
		this.vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		this.resourceAllocationProvider = data.getDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider, IResourceAllocationConstraintDataComponentProvider.class);
		this.orderedSequenceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_orderedElementsProvider, IOrderedSequenceElementsDataComponentProvider.class);
		this.allResources = data.getResources();
	}

	/**
	 * Compute whether element1 should ever be sequenced preceding element2
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean allowSequence(T e1, T e2) {
		// Check port types, as this is probably most common possible kind of error
		final PortType portType1 = portTypeProvider.getPortType(e1);
		final PortType portType2 = portTypeProvider.getPortType(e2);
		if (!allowPortTypes(portType1, portType2)) return false;
		
		//so port types are valid, now check feasibility of travel times
		//and incidentally resource-element binding constraints.
		if (!reachableFrom(e1, e2)) return false; 
		
		//travel time constraints can be met by some vessel, 
		//now consider fixed load/discharge pairs, which must not be separated.
		if (!canFollow(e1, e2)) return false;
		
		return true;
	}

	/**
	 * Check whether e2 can follow e1 according to the ordered sequence constraints.
	 * @param e1
	 * @param e2
	 * @return
	 */
	private boolean canFollow(T e1, T e2) {
		final T after1 = orderedSequenceProvider.getNextElement(e1);
		if (after1 == null) {
			final T before2 = orderedSequenceProvider.getPreviousElement(e2);
			return before2 == null || before2.equals(e1); //second test may not be necessary, if the orderedSequenceConstraint is reflexive
		} else {
			return after1.equals(e2);
		}
	}

	/**
	 * Can element 2 be reached from element 1 in time, using any vessel in the fleet legal for both elements.
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean reachableFrom(T e1, T e2) {
		//get resources which can service both elements
		Collection<IResource> allowed = resourceAllocationProvider.getAllowedResources(e1);
		Set<IResource> sharedResources = new HashSet<IResource>(allowed == null ? allResources : allowed);
		
		allowed = resourceAllocationProvider.getAllowedResources(e2);
		if (allowed != null)
			sharedResources.retainAll(resourceAllocationProvider.getAllowedResources(e2));
		
		for (IResource resource : sharedResources) {
			if (reachableFrom(e1, e2, resource)) return true;
		}
		return false;
	}

	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under the best possible circumstances,
	 * if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource the vessel in question
	 * @return
	 */
	public boolean reachableFrom(final T e1, final T e2, final IResource resource) {
		final IPortSlot slot1 = portSlotProvider.getPortSlot(e1);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(e2);
		
		final IVessel vessel = vesselProvider.getVessel(resource);
		
		final int travelTime = distanceProvider.get(IMultiMatrixProvider.Default_Key)
			.get(slot1.getPort(), slot2.getPort()) / vessel.getVesselClass().getMaxSpeed();
		
		final int earliestArrivalTime = slot1.getTimeWindow().getStart() + 
			elementDurationProvider.getElementDuration(e1, resource) +
			travelTime;
		
		return earliestArrivalTime < slot2.getTimeWindow().getEnd();
	}

	/**
	 * Should port visits of these types follow one another?
	 * 
	 * TODO this is code effectively shared with the {@link PortTypeConstraintChecker}, and could be factored out.
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public boolean allowPortTypes(final PortType pt1, final PortType pt2) {
		//End can't come before anything and Start can't come after anything
		if (pt1.equals(PortType.End) || pt2.equals(PortType.Start)) return false;
		
		//load must precede discharge or waypoint, but nothing else
		if (pt1.equals(PortType.Load)) return (pt2.equals(PortType.Discharge) || pt2.equals(PortType.Waypoint));
		
		//discharge may precede anything but Discharge (and start, but we already did that)
		if (pt1.equals(PortType.Discharge) &&
				pt2.equals(PortType.Discharge)) return false; 
		
		return true;
	}
}
