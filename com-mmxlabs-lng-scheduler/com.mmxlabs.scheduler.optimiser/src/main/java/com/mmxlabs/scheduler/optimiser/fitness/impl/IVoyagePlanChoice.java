package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * 
 * Implementations of {@link IVoyagePlanChoice} represent the range of choices
 * that need to be considered for a particular variable when optimising the
 * {@link IVoyagePlan}. For example this may be the flag indicating whether or
 * not to use base fuel as the supplement.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVoyagePlanChoice {

	/**
	 * Returns the number of choices. E.g. for a boolean variable this will
	 * return 2.
	 * 
	 * @return
	 */
	int numChoices();

	/**
	 * Apply a single choice. Range of values is [0 : {@link #numChoices()}).
	 * Returns false if the option cannot be applied (e.g. due to conflicting
	 * constraints)
	 * 
	 * @param choice
	 */
	boolean apply(int choice);
}