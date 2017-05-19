/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EarliestSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * An implementation of {@link IConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class PanamaSlotsConstraintChecker implements IConstraintChecker {

	private final String name;
	private static final int VESSEL_START = 0;
	private static final int LOAD = 1;
	private static final int DISCHARGE = 2;
	private static final int VESSEL_END = 3;
	

	@Inject
	private IPanamaSlotsProvider panamaSlotsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;
	
	@Inject 
	private DateAndCurveHelper dateAndCurveHelper;
	
	@Inject
	private EarliestSequenceScheduler earliestSequenceScheduler;
	
	@Inject
	private IDistanceProvider distanceProvider;
	
	@Inject
	private IRouteCostProvider routeCostProvider;
	
	@Inject
	private IPortProvider portProvider;
	
	@Inject
	private IElementDurationProvider elementDurationProvider;
	
	
	
	public PanamaSlotsConstraintChecker(final String name) {
		this.name = name;
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
		Map<IPort, SortedSet<Integer>> slots = new HashMap<IPort, SortedSet<Integer>>();
		panamaSlotsProvider.getSlots().forEach((k,v) -> {
			slots.put(k, new TreeSet(v));
		});
		int[][] schedule = earliestSequenceScheduler.schedule(sequences);
		
		for (int r = 0; r < sequences.getResources().size(); r++){
			IResource resource = sequences.getResources().get(r);
			ISequence sequence = sequences.getSequence(resource);
			IPort loadPort = portProvider.getPortForElement(sequence.get(LOAD));
			IPort dischargePort = portProvider.getPortForElement(sequence.get(DISCHARGE));
			
			int startTime = 0; // TODO: where to get it?? 

			vesselProvider.getVesselAvailability(resource); // vessel
			// check if direct voyage possible
			int directTravelTimeOutward = distanceProvider.getTravelTime(ERouteOption.DIRECT, 
					vesselProvider.getVesselAvailability(resource).getVessel(), 
					loadPort, 
					dischargePort, 
					startTime, 
					vesselProvider.getVesselAvailability(resource).getVessel().getVesselClass().getMaxSpeed());
			
			int directTravelTimeReturn = distanceProvider.getTravelTime(ERouteOption.DIRECT, 
					vesselProvider.getVesselAvailability(resource).getVessel(), 
					loadPort, 
					dischargePort, 
					startTime + directTravelTimeOutward + elementDurationProvider.getElementDuration(sequence.get(DISCHARGE), resource), 
					vesselProvider.getVesselAvailability(resource).getVessel().getVesselClass().getMaxSpeed());
			
			if (schedule[r][DISCHARGE] < (startTime + directTravelTimeOutward) ||
					schedule[r][VESSEL_END] > (startTime + directTravelTimeOutward + directTravelTimeReturn)){
				if (checkSlots(slots, vesselProvider.getVesselAvailability(resource).getVessel()) == false){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkSlots(Map<IPort, SortedSet<Integer>> slots, IVessel vessel){
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
