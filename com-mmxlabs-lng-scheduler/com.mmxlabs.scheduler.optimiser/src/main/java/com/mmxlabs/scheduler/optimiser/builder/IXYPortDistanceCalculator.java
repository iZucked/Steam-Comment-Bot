/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder;

import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * Interface to provide or calculate distances between {@link IXYPort}s.
 * 
 * @author Simon Goodall
 * 
 */
public interface IXYPortDistanceCalculator {

	/**
	 * 
	 * Return the one-way distance between these ports.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	double getDistance(IXYPort from, IXYPort to);

}