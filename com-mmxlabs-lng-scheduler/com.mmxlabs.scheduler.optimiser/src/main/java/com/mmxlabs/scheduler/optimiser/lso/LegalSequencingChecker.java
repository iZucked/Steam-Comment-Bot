/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for testing whether pairs of sequence elements can be scheduled next to one another.
 * Typical usage is to construct it with some context and then call {@code allowSequence(e1, e2)} to determine if e1 can follow e2.
 * @author hinton
 *
 */
public class LegalSequencingChecker<T> {
private IPortSlotProvider<T> portSlotProvider;
	private IElementDurationProvider<T> elementDurationProvider;
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;
	private IVesselProvider vesselProvider;
	private List<IPairwiseConstraintChecker<T>> pairwiseCheckers;
	private List<IResource> resources;
	
	public LegalSequencingChecker(IOptimisationContext<T> context) {
		this(context.getOptimisationData(), createPairwiseCheckers(context));			
	}
	
	/**
	 * Utility method to get the pairwise checkers which are enabled in the given context
	 * @param <T>
	 * @param context
	 * @return
	 */
	private static <T> List<IPairwiseConstraintChecker<T>> createPairwiseCheckers(
			IOptimisationContext<T> context) {
		
		ArrayList<IPairwiseConstraintChecker<T>> pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker<T>>();
		
		for (final IConstraintCheckerFactory factory : context.getConstraintCheckerRegistry().
				getConstraintCheckerFactories(context.getConstraintCheckers())) {
			IConstraintChecker<T> checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker<T>) checker);
			}
		}
		
		return pairwiseCheckers;
	}

	@SuppressWarnings("unchecked")
	public LegalSequencingChecker(IOptimisationData<T> data, List<IPairwiseConstraintChecker<T>> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
		for (IPairwiseConstraintChecker<T> pairwiseChecker : pairwiseCheckers) {
			pairwiseChecker.setOptimisationData(data);
		}
		this.portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		this.elementDurationProvider = data.getDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, IElementDurationProvider.class);
		this.distanceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);
		this.vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		this.resources = data.getResources();
	}

	/**
	 * Compute whether element1 should ever be sequenced preceding element2
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean allowSequence(T e1, T e2, IResource resource) {
		// Check with hard constraints like resource allocation and ordered elements
		for (IPairwiseConstraintChecker<T> pairwiseChecker : pairwiseCheckers) {
			if (!pairwiseChecker.checkPairwiseConstraint(e1, e2, resource)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<String> getSequencingProblems(T e1, T e2, IResource resource) {
		List<String> result = new ArrayList<String>();
		
		for (IPairwiseConstraintChecker<T> pairwiseChecker : pairwiseCheckers) {
			if (!pairwiseChecker.checkPairwiseConstraint(e1, e2, resource)) {
				result.add(pairwiseChecker.getName() + " says " + pairwiseChecker.explain(e1, e2, resource));
			}
		}
		
		return result;
	}

	public boolean allowSequence(T e1, T e2) {
		for (IResource r : resources) {
			if (allowSequence(e1, e2, r)) return true;
		}
		return false;
	}
}
