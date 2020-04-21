/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;

public class ScheduledPlanInput {

	private final int sequenceStartTime;
	private final int planStartTime;

	public int getPlanStartTime() {
		return planStartTime;
	}

	private final PreviousHeelRecord previousHeelRecord;

	public ScheduledPlanInput(final int sequenceStartTime, final int planStartTime, final @Nullable PreviousHeelRecord previousHeelRecord) {

		this.planStartTime = planStartTime;
		this.previousHeelRecord = previousHeelRecord;
		this.sequenceStartTime = sequenceStartTime;
	}

	public int getSequenceStartTime() {
		return sequenceStartTime;
	}

	public @Nullable PreviousHeelRecord getPreviousHeelRecord() {
		return previousHeelRecord;
	}
}