/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public interface ISimilarityComponentParameters {

	public enum Interval {
		LOW,
		MEDIUM,
		HIGH,
	}

	int getThreshold(@NonNull Interval interval);

	int getWeight(@NonNull Interval interval);
	
	int getOutOfBoundsWeight();
}
