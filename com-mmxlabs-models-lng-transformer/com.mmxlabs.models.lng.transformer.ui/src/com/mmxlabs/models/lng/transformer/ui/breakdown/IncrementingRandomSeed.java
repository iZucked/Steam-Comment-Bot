/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

public class IncrementingRandomSeed {
	long seed = -1;
	
	public long getSeed() {
		return ++seed;
	}
}
