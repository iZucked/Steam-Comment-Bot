/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.scenario;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * {@link IDataComponentProvider} implementations provide additional data for
 * e.g. {@link IFitnessCore} and {@link IConstraintChecker} objects.
 * 
 * @author proshun, Simon Goodall
 */
public interface IDataComponentProvider {

	/**
	 * Returns a unique name for this {@link IDataComponentProvider}
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Notify {@link IDataComponentProvider} that it is no longer required and
	 * clean up any internal references.
	 */
	void dispose();
}
