/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

public interface ILightWeightFitnessFunction {
	void init();
	long evaluate(List<List<Integer>> sequences);
	//ALEX_TODO: change to long
	long annotate(List<List<Integer>> sequences);
}
