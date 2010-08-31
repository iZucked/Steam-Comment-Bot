package com.mmxlabs.scheduler.optimiser.lso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * <p>
 * 	A move generator which tries to create moves that respect timed constraints, to avoid
 * wasting too many cycles testing clearly impossible moves. Suggested in ticket #9.
 * </p>
 * <ol>
 * The moves generated should try to respect the following aspects, where possible.
 * <li>Time Windows + Voyage Durations</li>
 * <li>Port visit durations</li>
 * <li>Port restrictions</li>
 * <li>Load / Discharge ordering</li>
 * <li>Fixed load/discharge pairs</li>
 * <li>Vessel events</li>
 * <li>Vessel or class assignments</li>
 * </ol>
 * 
 * @author hinton
 *
 * @param <T>
 */
public class ConstrainedMoveGenerator<T> implements IMoveGenerator<T> {
	Map<T, List<T>> validFollowers = new HashMap<T, List<T>>();
	private IOptimisationContext<T> context;
	private IPortTypeProvider<T> portTypeProvider;
	private IPortSlotProvider<T> portSlotProvider;
	private IElementDurationProvider<T> elementDurationProvider;
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;
	private IVesselProvider vesselProvider;
	public ConstrainedMoveGenerator(IOptimisationContext<T> context) {
		this.context = context;
		final IOptimisationData<T> data = context.getOptimisationData();
		this.portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		this.portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		this.elementDurationProvider = data.getDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, IElementDurationProvider.class);
		this.distanceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);
		this.vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
	}
	
	/**
	 * Compute whether element1 should ever be sequenced preceding element2
	 * @param e1
	 * @param e2
	 * @return
	 */
	private final boolean allowSequence(T e1, T e2) {
		// Check port types, as this is probably most common possible kind of error
		final PortType portType1 = portTypeProvider.getPortType(e1);
		final PortType portType2 = portTypeProvider.getPortType(e2);
		if (!allowPortTypes(portType1, portType2)) return false;
		
		//so port types are valid, now check feasibility of travel times
		if (!reachableFrom(e1, e2)) return false; 
		
		//travel time constraints can be met by some vessel, now consider fixed pairs, which must not be separated.
		
		
		
		return true;
	}

	/**
	 * Can element 2 be reached from element 1 in time, using any vessel in the fleet?
	 * TODO: this may say yes when it should say no, because it does not respect resource allocation constraints. fix this.
	 * @param e1
	 * @param e2
	 * @return
	 */
	private boolean reachableFrom(T e1, T e2) {
		for (IResource resource : context.getOptimisationData().getResources()) {
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
	private final boolean reachableFrom(final T e1, final T e2, final IResource resource) {
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
	 * Should ports visits of these types follow one another?
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	private boolean allowPortTypes(final PortType pt1, final PortType pt2) {
		//Start can never be scheduled before end
		if (pt1.equals(PortType.End) || pt2.equals(PortType.Start)) return false;
		
		//load must precede discharge
		if (pt1.equals(PortType.Load)) return pt2.equals(PortType.Discharge);
		
		//discharge may precede anything but Discharge (and start, but we already did that)
		if (pt1.equals(PortType.Discharge) &&
				pt2.equals(PortType.Discharge)) return false; 
		
		return true;
	}

	@Override
	public IMove<T> generateMove() {
		return null;
	}

	@Override
	public ISequences<T> getSequences() {
		return null;
	}

	@Override
	public void setSequences(ISequences<T> sequences) {
		
	}
}

/*
 * NOTES: The question of the relative frequencies of calls to setSequences and generateMove
 * is germane to an efficient implementation, if there is any sorting to be done. Here's one possible idea:
 * the total set of elements is fixed, so we could create some structure recording all the valid sequential pairs,
 * which we can use to select breakpoints (in order to induce a valid sequential pair in the solution)
 */