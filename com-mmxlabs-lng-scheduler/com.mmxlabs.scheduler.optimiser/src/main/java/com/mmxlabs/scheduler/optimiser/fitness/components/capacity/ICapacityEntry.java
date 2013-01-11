/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity;

/**
 * A record of capacity violation type and quantity.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICapacityEntry {

	/**
	 * Returns a human readable string relating to the type of violation which occurred.
	 * 
	 * @return
	 */
	String getType();

	/**
	 * Returns the excess volume causing the violation
	 * 
	 * @return
	 */
	long getVolume();
}
