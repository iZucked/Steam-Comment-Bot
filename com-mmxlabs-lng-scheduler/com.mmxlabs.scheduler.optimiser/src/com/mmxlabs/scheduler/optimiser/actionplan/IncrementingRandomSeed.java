/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan;

public class IncrementingRandomSeed {
	long seed = -1;
	
	public long getSeed() {
		return ++seed;
	}
}
