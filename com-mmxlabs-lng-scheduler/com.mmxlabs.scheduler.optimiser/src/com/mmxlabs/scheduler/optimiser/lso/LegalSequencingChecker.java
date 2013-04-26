/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;

/**
 * A utility class for testing whether pairs of sequence elements can be scheduled next to one another. Typical usage is to construct it with some context and then call {@code allowSequence(e1, e2)}
 * to determine if e1 can follow e2.
 * 
 * @author hinton
 * 
 */
public class LegalSequencingChecker {

	private final List<IPairwiseConstraintChecker> pairwiseCheckers;
	private final List<IResource> resources;

	public LegalSequencingChecker(final IOptimisationContext context) {
		this(context.getOptimisationData(), createPairwiseCheckers(context));
	}

	/**
	 * Utility method to get the pairwise checkers which are enabled in the given context
	 * 
	 * @param
	 * @param context
	 * @return
	 */
	private static List<IPairwiseConstraintChecker> createPairwiseCheckers(final IOptimisationContext context) {

		final ArrayList<IPairwiseConstraintChecker> pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker>();

		for (final IConstraintCheckerFactory factory : context.getConstraintCheckerRegistry().getConstraintCheckerFactories(context.getConstraintCheckers())) {
			final IConstraintChecker checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker) checker);
			}
		}

		return pairwiseCheckers;
	}

	@Inject
	public LegalSequencingChecker(final IOptimisationData data, final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
		this.resources = data.getResources();
	}

	/**
	 * Compute whether element1 should ever be sequenced preceding element2
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean allowSequence(final ISequenceElement e1, final ISequenceElement e2, final IResource resource) {
		// Check with hard constraints like resource allocation and ordered elements
		for (final IPairwiseConstraintChecker pairwiseChecker : pairwiseCheckers) {
			if (!pairwiseChecker.checkPairwiseConstraint(e1, e2, resource)) {
				// if (log.isInfoEnabled()) {
				// log.info("Rejected: " + pairwiseChecker.getName() + ": " + pairwiseChecker.explain(e1, e2, resource));
				// }
				return false;
			}
		}

		return true;
	}

	public List<String> getSequencingProblems(final ISequenceElement e1, final ISequenceElement e2, final IResource resource) {
		final List<String> result = new ArrayList<String>();

		for (final IPairwiseConstraintChecker pairwiseChecker : pairwiseCheckers) {
			if (!pairwiseChecker.checkPairwiseConstraint(e1, e2, resource)) {
				result.add(pairwiseChecker.getName() + " says " + pairwiseChecker.explain(e1, e2, resource));
			}
		}

		return result;
	}

	public boolean allowSequence(final ISequenceElement e1, final ISequenceElement e2) {
		for (final IResource r : resources) {
			if (allowSequence(e1, e2, r)) {
				return true;
			}
		}
		return false;
	}

	// TODO quick hack to try something
	public void disallowLateness() {
		for (final IPairwiseConstraintChecker checker : pairwiseCheckers) {
			if (checker instanceof TravelTimeConstraintChecker) {
				final TravelTimeConstraintChecker tt = (TravelTimeConstraintChecker) checker;
				tt.setMaxLateness(0);
			}
		}
	}
}
