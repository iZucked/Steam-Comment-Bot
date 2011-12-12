/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * The {@link IFitnessCore} is the main fitness function object. A single
 * {@link IFitnessCore} can represent multiple {@link IFitnessComponent}s. The
 * {@link IFitnessCore} is used to evaluate {@link ISequences} instances either
 * in whole, or progressively throughout an optimisation process.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessCore {

	/**
	 * Initialise or re-initialise the fitness core. This will reset the state
	 * associated with the {@link #evaluate(ISequences, List)} method.
	 */
	void init(IOptimisationData data);

	/**
	 * Return a {@link Collection} of {@link IFitnessComponent} instances that
	 * are represented by this {@link IFitnessCore}.
	 * 
	 * @return
	 */
	Collection<IFitnessComponent> getFitnessComponents();

	/**
	 * Evaluates the full fitness of the given sequences. If this returns false,
	 * the sequences are unacceptably bad, and the caller should back out of any more evaluation immediately
	 * 
	 * @param sequences
	 */
	boolean evaluate(ISequences sequences);

	/**
	 * Evaluates the fitness of the given sequence. This method takes a list of
	 * affected resources used to indicate that only the given resources have
	 * changed since the previous evaluation. If this is the first invocation of
	 * this method since the last {@link #init()} call, then a full evaluation
	 * must be performed and subsequent calls can take advantage of the affected
	 * list. Likewise, passing null into the affectedResources will perform a
	 * full evaluation.
	 * 
	 * @param sequences
	 */
	boolean evaluate(ISequences sequences,
			Collection<IResource> affectedResources);

	/**
	 * Notify the fitness core that the given sequences have been accepted.
	 * These parameters should have been given to the last invocation of
	 * {@link #evaluate(ISequences, Collection)}.
	 * 
	 * @param sequences
	 * @param affectedResources
	 */
	void accepted(ISequences sequences,
			Collection<IResource> affectedResources);

	/**
	 * Clean up resources once the {@link IFitnessCore} is no longer required.
	 */
	void dispose();

	/**
	 * Add annotations of the given sequences to the associated {@link IAnnotatedSolution}.
	 * Performs a full evaluation, but should <em>not</em> change the fitness stored in the associated components
	 * 
	 * @param sequences sequences to evaluate
	 * @param solution annotated solution for these sequences
	 */
	void annotate(final ISequences sequences, final IAnnotatedSolution solution, final boolean forExport);
}
