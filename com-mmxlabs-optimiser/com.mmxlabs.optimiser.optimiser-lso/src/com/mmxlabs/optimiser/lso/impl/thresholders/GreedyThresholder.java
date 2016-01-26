/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * A greedy thresholder that only accepts better solutions.
 * 
 * @author achurchill
 */
public class GreedyThresholder implements IThresholder {
	private static final Logger log = LoggerFactory.getLogger(GreedyThresholder.class);

	/**
	 * Create a new greedy thresholder
	 * 
	 */
	public GreedyThresholder() {
	}

	@Override
	public void init() {
	}

	@Override
	public boolean accept(final long delta) {
		if (delta < 0) {
			return true;
		}
		return false;
	}


	@Override
	public void step() {
	}

	@Override
	public void reset() {
	}

}
