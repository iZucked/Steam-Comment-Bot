/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public interface ISimilarityComponentParameters {

	public enum Interval {
		@NonNull
		LOW,
		@NonNull
		MEDIUM,
		@NonNull
		HIGH,
	}

	int getThreshold(@NonNull Interval interval);

	int getWeight(@NonNull Interval interval);
	
	int getOutOfBoundsWeight();
}
