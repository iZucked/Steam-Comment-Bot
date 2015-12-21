package com.mmxlabs.models.lng.transformer.ui.breakdown;

public class IncrementingRandomSeed {
	long seed = -1;
	
	public long getSeed() {
		return ++seed;
	}
}
