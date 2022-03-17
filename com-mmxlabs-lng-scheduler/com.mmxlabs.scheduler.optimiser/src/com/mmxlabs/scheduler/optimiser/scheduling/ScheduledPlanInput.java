/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;

public final class ScheduledPlanInput {

	private final int vesselStartTime;
	private final int planStartTime;
	private final PreviousHeelRecord previousHeelRecord;

	public ScheduledPlanInput(final int vesselStartTime, final int planStartTime, final @Nullable PreviousHeelRecord previousHeelRecord) {

		this.planStartTime = planStartTime;
		this.previousHeelRecord = previousHeelRecord;
		this.vesselStartTime = vesselStartTime;
	}

	public int getVesselStartTime() {
		return vesselStartTime;
	}

	public int getPlanStartTime() {
		return planStartTime;
	}

	public @Nullable PreviousHeelRecord getPreviousHeelRecord() {
		return previousHeelRecord;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof ScheduledPlanInput) {
			final ScheduledPlanInput other = (ScheduledPlanInput) obj;

			return planStartTime == other.planStartTime //
					&& vesselStartTime == other.vesselStartTime //
					&& Objects.equals(previousHeelRecord, other.previousHeelRecord);

		}
		return false;
	}
}