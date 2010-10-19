/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link IVoyagePlanOptimiser} optimises the choices in a
 * {@link VoyagePlan} based on {@link IVoyagePlanChoice} implementations. These
 * are provided in a set order and they can edit the voyage plan objects.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IVoyagePlanOptimiser<T> {

	/**
	 * Check internal state is valid (i.e. all setters have been called).
	 */
	void init();

	/**
	 * Reset the state of this object ready for a new optimisation.
	 */
	void reset();

	/**
	 * Clean up all references.
	 */
	void dispose();

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	VoyagePlan optimise();

	/**
	 * Returns the basic sequence that is being optimised over.
	 * 
	 * @return
	 */
	List<Object> getBasicSequence();

	/**
	 * Sets the basic voyage plan sequence. This should be {@link IPortSlot}
	 * instances separated by {@link VoyageOptions} instances implementing
	 * {@link Cloneable}. The {@link VoyageOptions} objects will be modified
	 * during optimisation.
	 * 
	 * @param basicSequence
	 */
	void setBasicSequence(final List<Object> basicSequence);

	/**
	 * Get the {@link IVessel} to evaluate voyages against.
	 * 
	 * @return
	 */
	IVessel getVessel();

	/**
	 * Set the {@link IVessel} to evaluate voyages against.
	 * 
	 * @param vessel
	 */
	void setVessel(final IVessel vessel);

	/**
	 * Once optimised, returns the best {@link VoyagePlan} cost.
	 * 
	 * @return
	 */
	long getBestCost();

	/**
	 * Once optimised, returns the best {@link VoyagePlan}.
	 * 
	 * @return
	 */
	VoyagePlan getBestPlan();

	/**
	 * Returns the {@link ILNGVoyageCalculator} used in the
	 * {@link VoyagePlanOptimiser}.
	 * 
	 * @return
	 */
	ILNGVoyageCalculator<T> getVoyageCalculator();

	/**
	 * Add a new choice to the ordered stack of choices. If this choice depends
	 * upon the choice of another {@link IVoyagePlanChoice}, then that object
	 * should have already been added.
	 * 
	 * @param choice
	 */
	void addChoice(final IVoyagePlanChoice choice);

}