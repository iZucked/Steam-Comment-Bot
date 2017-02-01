/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;

/**
 * A utility class for testing whether pairs of sequence elements can be scheduled next to one another. Typical usage is to construct it with some context and then call {@code allowSequence(e1, e2)}
 * to determine if e1 can follow e2.
 * 
 * @author hinton
 * 
 */
public class LegalSequencingChecker {
	private static final Logger log = LoggerFactory.getLogger(LinearSimulatedAnnealingFitnessEvaluator.class);

	private final List<IPairwiseConstraintChecker> pairwiseCheckers;
	private final List<IResource> resources;

	private IPairwiseConstraintChecker resourceAllocationChecker;

	public LegalSequencingChecker(final IOptimisationData data, final IOptimisationContext context) {
		this(data, createPairwiseCheckers(context));
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
		this.pairwiseCheckers = new ArrayList<>(pairwiseCheckers);
		this.resources = data.getResources();

		for (IPairwiseConstraintChecker checker : this.pairwiseCheckers) {
			if (checker instanceof ResourceAllocationConstraintChecker) {
				this.resourceAllocationChecker = checker;
				this.pairwiseCheckers.remove(checker);
				break;
			}
		}
	}

	/**
	 * Compute whether element1 should ever be sequenced preceding element2
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean allowSequence(final ISequenceElement e1, final ISequenceElement e2, final IResource resource, boolean print) {
		// Check with hard constraints like resource allocation and ordered elements

		if (!resourceAllocationChecker.checkPairwiseConstraint(e1, e2, resource)) {
			// if (log.isInfoEnabled()) {
			// log.info("Rejected: " + pairwiseChecker.getName() + ": " + pairwiseChecker.explain(e1, e2, resource));
			// }
			if (print)
				System.out.println("RAC: Rejected: " + resourceAllocationChecker.getName() + ": " + resourceAllocationChecker.explain(e1, e2, resource));
			return false;
		} else {
			// System.out.println("True");
		}
		for (final IPairwiseConstraintChecker pairwiseChecker : pairwiseCheckers) {
			if (!pairwiseChecker.checkPairwiseConstraint(e1, e2, resource)) {
				// if (log.isInfoEnabled()) {
				// log.info("Rejected: " + pairwiseChecker.getName() + ": " + pairwiseChecker.explain(e1, e2, resource));
				// }
				if (print) {
					System.out.println("PW: Rejected: " + pairwiseChecker.getName() + ": " + pairwiseChecker.explain(e1, e2, resource));
				}
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

	public boolean allowSequence(final ISequenceElement e1, final ISequenceElement e2, boolean print) {
		for (final IResource r : resources) {
			if (allowSequence(e1, e2, r, print)) {
				return true;
			}
		}
		return false;
	}

	// TODO quick hack to try something
	public void disallowLateness() {
		setMaxLateness(0);
	}

	public int getMaxLateness() {
		int time = 0;
		for (final IPairwiseConstraintChecker checker : pairwiseCheckers) {
			if (checker instanceof TravelTimeConstraintChecker) {
				final TravelTimeConstraintChecker tt = (TravelTimeConstraintChecker) checker;
				time = tt.getMaxLateness();
				return time;
			}
		}
		return time;
	}

	public void setMaxLateness(int time) {
		for (final IPairwiseConstraintChecker checker : pairwiseCheckers) {
			if (checker instanceof TravelTimeConstraintChecker) {
				final TravelTimeConstraintChecker tt = (TravelTimeConstraintChecker) checker;
				tt.setMaxLateness(time);
			}
		}
	}
}
