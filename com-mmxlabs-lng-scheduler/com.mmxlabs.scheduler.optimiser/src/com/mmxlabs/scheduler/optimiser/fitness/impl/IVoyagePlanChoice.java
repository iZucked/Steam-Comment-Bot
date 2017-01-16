/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * Implementations of {@link IVoyagePlanChoice} represent the range of choices that need to be considered for a particular variable when optimising the {@link VoyagePlan}. For example this may be the
 * flag indicating whether or not to use base fuel as the supplement. Note: Currently it is assumed that only {@link VoyageOptions} implementations will be modified and {@link IPortSlot}
 * implementations will be left unchanged. As {@link IPortSlot} objects are part of the input data, additional layers will need to be created - such as an IPortOptions set of classes.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVoyagePlanChoice {

	/**
	 * Returns the number of choices. E.g. for a boolean variable this will return 2.
	 * 
	 * @return
	 */
	int numChoices();

	/**
	 * Apply a single choice. Range of values is [0 : {@link #numChoices()}). Returns false if the option cannot be applied (e.g. due to conflicting constraints)
	 * 
	 * @param choice
	 */
	boolean apply(int choice);

	boolean reset();

	boolean nextChoice();
}