/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * This interface defines a BaseFuel
 * 
 * @author achurchill
 * 
 */
public interface IBaseFuel {

	/**
	 * The name of the base fuel.
	 * 
	 * @return the base fuel's name
	 */
	String getName();

	/**
	 * Get the equivalence factor of the base fuel to convert to lng
	 * @return
	 */
	int getEquivalenceFactor();

	/**
	 * Set the equivalence factor of the base fuel to convert to lng
	 * @return
	 */
	void setEquivalenceFactor(int value);
}
