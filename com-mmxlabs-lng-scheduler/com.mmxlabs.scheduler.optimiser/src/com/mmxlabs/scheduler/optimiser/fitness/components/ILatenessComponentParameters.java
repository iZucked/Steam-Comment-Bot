/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public interface ILatenessComponentParameters {

	public enum Interval {
		@NonNull
		PROMPT, // As defined by IPromptPeriodProvider
		@NonNull
		MID_TERM, // End of prompt to +90 Days (90 * 24)
		@NonNull
		BEYOND // End of mid-term to end
	}

	int getThreshold(@NonNull Interval interval);

	int getLowWeight(@NonNull Interval interval);

	int getHighWeight(@NonNull Interval interval);
}
