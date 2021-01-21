/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;

public class ScheduledPlanInput {

	private final int vesselStartTime;
	private final int planStartTime;

	public int getPlanStartTime() {
		return planStartTime;
	}

	private final PreviousHeelRecord previousHeelRecord;

	public ScheduledPlanInput(final int vesselStartTime, final int planStartTime, final @Nullable PreviousHeelRecord previousHeelRecord) {

		this.planStartTime = planStartTime;
		this.previousHeelRecord = previousHeelRecord;
		this.vesselStartTime = vesselStartTime;
	}

	public int getVesselStartTime() {
		return vesselStartTime;
	}

	public @Nullable PreviousHeelRecord getPreviousHeelRecord() {
		return previousHeelRecord;
	}
}