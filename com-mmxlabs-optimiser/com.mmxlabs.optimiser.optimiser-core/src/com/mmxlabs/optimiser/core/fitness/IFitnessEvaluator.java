/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * The {@link IFitnessEvaluator} interface defines objects which can be used in an optimisation process to determine whether or not the current fitness is a suitable improvement. The
 * {@link #init(ISequences)} method needs to be called at the start of the process and further invocations of this class should use {@link #checkSequences(ISequences)}. The {@link IFitnessComponent}
 * passed to the {@link #setFitnessComponents(List)} will be used to evaluate fitnesses. As such they should not be used while using this instance.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessEvaluator {

	/**
	 * Returns the {@link List} of {@link IFitnessComponent}s used by this {@link IFitnessEvaluator}
	 * 
	 * @return
	 */
	List<IFitnessComponent> getFitnessComponents();

	/**
	 * Set the list of {@link IFitnessComponent}s to be used by this {@link IFitnessEvaluator}. Note these {@link IFitnessComponent}s instances should not be elsewhere while using this object.
	 * 
	 * @param fitnessComponents
	 */
	void setFitnessComponents(List<IFitnessComponent> fitnessComponents);

	/**
	 * Returns the {@link IFitnessHelper} used to evaluate {@link ISequences} using the {@link IFitnessComponent}s passed into the {@link #setFitnessComponents(List)} method.
	 * 
	 * @return
	 */
	IFitnessHelper getFitnessHelper();

	/**
	 * Set the {@link IFitnessHelper} used to evaluate {@link ISequences} using the {@link IFitnessComponent}s passed into the {@link #setFitnessComponents(List)} method.
	 * 
	 */
	void setFitnessHelper(IFitnessHelper fitnessHelper);

	/**
	 * Initialise {@link IFitnessEvaluator} and ensure all the relevant setters have been called.
	 */
	void init();

	/**
	 * Initialise the {@link IFitnessEvaluator} with {@link IOptimisationData} object. This should be called once at the beginning of an optimisation process.
	 * 
	 * @param data
	 */
	void setOptimisationData(IOptimisationData data);

	/**
	 * Set the initial sequences. These should have had any manipulation from {@link ISequencesManipulator} already applied.
	 * 
	 * @param sequences
	 */
	void setInitialSequences(ISequences sequences);

	/**
	 * Evaluate the given {@link ISequences} to determine whether or not they are accepted as a better state than the previously accepted state. If accepted, then the {@link ISequences} "state" will
	 * be recorded and used as the basis for future checks. {@link #setInitialSequences(ISequences)} must have been called previously to setup the initial state.
	 * 
	 * @param sequences
	 * @return
	 */
	boolean evaluateSequences(ISequences sequences, Collection<IResource> affectedResources);

	/**
	 * Returns the best {@link ISequences} instance seen by this {@link IFitnessEvaluator}. The value of best is determined by the implementation.
	 * 
	 * @return
	 */
	ISequences getBestSequences();

	/**
	 * Returns the fitness value of the best {@link ISequences} instance returned by {@link #getBestSequences()};
	 * 
	 * @return
	 */
	long getBestFitness();

	/**
	 * Returns the current {@link ISequences} instance in use by this {@link IFitnessEvaluator}.
	 * 
	 * @return
	 */
	ISequences getCurrentSequences();

	/**
	 * Returns the fitness value of the current {@link ISequences} instance returned by {@link #getCurrentSequences()};
	 * 
	 * @return
	 */
	long getCurrentFitness();

	void dispose();

	/**
	 * Returns an annotated solution for the best sequences so far
	 * 
	 * @param context
	 * @return
	 */
	IAnnotatedSolution getBestAnnotatedSolution(final IOptimisationContext context, final boolean forExport);

	/**
	 * Returns an annotated solution for the current sequences.
	 * 
	 * @param context
	 * @return
	 */
	IAnnotatedSolution getCurrentAnnotatedSolution(final IOptimisationContext context, final boolean forExport);
}
