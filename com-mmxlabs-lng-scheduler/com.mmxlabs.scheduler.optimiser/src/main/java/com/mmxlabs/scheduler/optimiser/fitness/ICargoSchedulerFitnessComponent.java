/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Extension of the {@link IFitnessComponent} interface for use with the {@link CargoSchedulerFitnessCore} to provide fitnesses based on {@link ISequence}s scheduled with an {@link ISequenceScheduler}
 * .
 * 
 * Uses an iterating interface, as this is currently sufficient for all the implementors. To evaluate a scheduled solution,
 * 
 * @author Simon Goodall
 * 
 */
public interface ICargoSchedulerFitnessComponent extends ICargoFitnessComponent {
	/**
	 * Start evaluating a solution
	 */
	void startEvaluation();

	/**
	 * Start evaluating a sequence, which has the given resource associated with it
	 * 
	 * @param resource
	 *            the resource for the next sequence
	 * @param sequenceHasChanged
	 *            whether the sequence for this resource has changed at all since the last accepted evaluation
	 */
	void startSequence(IResource resource, boolean sequenceHasChanged);

	/**
	 * Consider the next voyageplan - note that the contents of the given plan will be presented to nextObject after this
	 * 
	 * @param voyagePlan
	 * @param time
	 * @return
	 */
	boolean nextVoyagePlan(final VoyagePlan voyagePlan, final int time);

	/**
	 * Evaluate an object from the sequence, either a VoyageDetails or a PortDetails
	 * 
	 * @param object
	 *            a PortDetails or a VoyageDetails
	 * @param time
	 *            the time at this port
	 * @return true if this is an OK object, or false if this solution is invalid
	 */
	boolean nextObject(final Object object, final int time);

	/**
	 * Indicates the end of the current sequence
	 * 
	 * @return true if the sequence is OK, or false if there's a problem
	 */
	boolean endSequence();

	/**
	 * Finish evaluating a solution and return its cost. The cost should also be stored for {@link IFitnessComponent#getFitness()}.
	 * 
	 * @return the total cost of this solution
	 */
	long endEvaluationAndGetCost();
}
