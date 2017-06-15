/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.NonSchedulingScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PanamaPriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * An implementation of {@link IConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class PanamaSlotsConstraintChecker implements IConstraintChecker {

	private final String name;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	@Inject
	private NonSchedulingScheduler scheduler;
	
	@Inject
	private IVesselProvider vesselProvider;
	
	private Set<ISequenceElement> unbookedSlots; 
	
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
		int[][] schedule = scheduler.schedule(sequences);
		boolean[][] throughPanama = scheduler.canalDecision();
		IRouteOptionBooking[][] assignedSlots = scheduler.slotsAssigned();
		
		int strictBoundary = panamaSlotsProvider.getStrictBoundary();
		int relaxedBoundary = panamaSlotsProvider.getRelaxedBoundary();
		
		Set<ISequenceElement> currentUnbookedSlots = new HashSet<>();
		Set<ISequenceElement> currentUnbookedSlotsInRelaxed = new HashSet<ISequenceElement>();
				
		//TODO: what to do with potential vessel return when end time is not specified?
		//TODO: nominal vessel
		//TODO: original solution might not be feasible at 16 knots and 24h early arrival time
		
		for (int r = 0; r < sequences.getResources().size(); r++){
			IResource resource = sequences.getResources().get(r);
			ISequence sequence = sequences.getSequence(resource);
			
			
			// skip resources that are not scheduled
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				// TODO: Implement something here rather than rely on VoyagePlanner
				continue;
			}

			// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
			if (sequence.size() < 2) {
				continue;
			}
			
			for (int index = 0; index < sequence.size(); index++){
				
				if (!throughPanama[r][index]){
					// not going through Panama, ignore
					continue;
				}
				if (assignedSlots[r][index] != null){
					//has a slot... all good.
					continue;
				}
				if (schedule[r][index] >= relaxedBoundary){
					continue;
				}
				
				if (schedule[r][index] >= strictBoundary && schedule[r][index] < relaxedBoundary){
					currentUnbookedSlotsInRelaxed.add(sequence.get(index));
				}
				currentUnbookedSlots.add(sequence.get(index));
			}
		}
		
		if (unbookedSlots != null){
			// strict constraint
			currentUnbookedSlots.removeAll(unbookedSlots);
			currentUnbookedSlots.removeAll(currentUnbookedSlotsInRelaxed);
			if (!currentUnbookedSlots.isEmpty()){
				return false;
			}
			
			// relaxed constraint
			int countBefore = currentUnbookedSlots.size();						// 0
			currentUnbookedSlotsInRelaxed.removeAll(unbookedSlots);
			int countAfter = currentUnbookedSlotsInRelaxed.size();				// 0
			int whitelistedSlotCount = (countBefore - countAfter);				// 6
			
			int relaxedSlotCount = panamaSlotsProvider.getRelaxedSlotCount(); 	// 5
			int newCount = relaxedSlotCount - whitelistedSlotCount;				// -1
			
			if (countAfter == 0 || countAfter <= newCount){
				return true;
			}else{
				return false;
			}
		}else {
			unbookedSlots = currentUnbookedSlots;
		}
		return true;
	}
	

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
