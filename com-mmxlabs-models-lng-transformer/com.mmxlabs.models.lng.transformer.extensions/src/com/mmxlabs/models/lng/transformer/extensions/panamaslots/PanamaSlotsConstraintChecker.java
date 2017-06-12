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

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionSlot;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.NonSchedulingScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PanamaPriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaSlotsProvider;

/**
 * An implementation of {@link IConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class PanamaSlotsConstraintChecker implements IConstraintChecker {

	private final String name;

	@Inject
	private IPanamaSlotsProvider panamaSlotsProvider;

	@Inject
	private NonSchedulingScheduler scheduler;
	
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
		IRouteOptionSlot[][] assignedSlots = scheduler.slotsAssigned();
		
		int strictBoundary = panamaSlotsProvider.getStrictBoundary();
		int relaxedBoundary = panamaSlotsProvider.getRelaxedBoundary();
		int relaxedCount = panamaSlotsProvider.getRelaxedSlotCount();
				
		//TODO: what to do with potential vessel return when end time is not specified?
		//TODO: nominal vessel
		//TODO: original solution might not be feasible at 16 knots and 24h early arrival time
		
		for (int r = 0; r < sequences.getResources().size(); r++){
			IResource resource = sequences.getResources().get(r);
			ISequence sequence = sequences.getSequence(resource);
			
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
				
				if (schedule[r][index] >= strictBoundary && schedule[r][index] < relaxedBoundary && relaxedCount > 0){
					relaxedCount--;
					continue;
				}
				return false;
			}
		}
		return true;
	}
	

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
