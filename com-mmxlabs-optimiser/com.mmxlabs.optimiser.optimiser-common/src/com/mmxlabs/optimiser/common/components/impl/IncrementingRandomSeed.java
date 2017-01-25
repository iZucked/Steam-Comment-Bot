/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

public class IncrementingRandomSeed {
	private long seed;

	public IncrementingRandomSeed() {
		seed = -1;
	}

	public IncrementingRandomSeed(long seed) {
		this.seed = seed;
	}

	public long getSeed() {
		return ++seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
}
