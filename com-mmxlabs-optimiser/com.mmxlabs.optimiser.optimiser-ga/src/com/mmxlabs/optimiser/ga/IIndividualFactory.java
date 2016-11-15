/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Factory interface to generate new {@link Individual}
 * 
 * @author Simon Goodall
 * 
 * @param <I>
 *            Individual type
 */
public interface IIndividualFactory<I extends Individual<I>> {

	/**
	 * Create a new {@link Individual} implementation.
	 * 
	 * @return
	 */
	@NonNull I createIndividual();
}