/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga;

/**
 * Interface defining the individual to pass into the {@link IGeneticAlgorithm}. Implementations are assumed to be mutable.
 * 
 * @author Simon Goodall
 * 
 * @param <I>
 *            Individual type - the implementing class
 */
public interface Individual<I extends Individual<I>> extends Cloneable {

	I clone();
}
