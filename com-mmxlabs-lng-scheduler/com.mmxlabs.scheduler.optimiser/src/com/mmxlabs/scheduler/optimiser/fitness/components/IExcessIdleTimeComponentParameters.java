/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IExcessIdleTimeComponentParameters {

	public enum Interval {
		LOW, // As defined by IPromptPeriodProvider
		HIGH, // End of prompt to +90 Days (90 * 24)
	}

	int getThreshold(@NonNull Interval interval);

	int getWeight(@NonNull Interval interval);

	int getEndWeight();
}
