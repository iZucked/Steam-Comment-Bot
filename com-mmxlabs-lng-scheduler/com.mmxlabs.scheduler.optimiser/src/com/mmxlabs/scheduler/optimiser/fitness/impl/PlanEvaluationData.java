/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Used for plan evaluation in the {@link VoyagePlanner} and Charter Out Generation
 */
public class PlanEvaluationData {

	@NonNull
	IPortTimesRecord portTimesRecord;
	@NonNull
	VoyagePlan plan;

	@Nullable
	IAllocationAnnotation allocation;
	long endHeelVolumeInM3;
	long startHeelVolumeInM3;
	boolean ignoreEndSet;

	public PlanEvaluationData(@NonNull IPortTimesRecord portTimesRecord, @NonNull VoyagePlan voyagePlan) {
		this.plan = voyagePlan;
		this.portTimesRecord = portTimesRecord;
		this.ignoreEndSet = plan.isIgnoreEnd();
		this.endHeelVolumeInM3 = plan.getRemainingHeelInM3();
		this.startHeelVolumeInM3 = plan.getStartingHeelInM3();
		this.allocation = null;
	}

	public @NonNull VoyagePlan getPlan() {
		return plan;
	}

	public void setPlan(@NonNull VoyagePlan plan) {
		this.plan = plan;
	}

	public long getStartHeelVolumeInM3() {
		return startHeelVolumeInM3;
	}

	public void setStartHeelVolumeInM3(long startHeelVolumeInM3) {
		this.startHeelVolumeInM3 = startHeelVolumeInM3;
	}

	public long getEndHeelVolumeInM3() {
		return endHeelVolumeInM3;
	}

	public void setEndHeelVolumeInM3(long endHeelVolumeInM3) {
		this.endHeelVolumeInM3 = endHeelVolumeInM3;
	}

	public IAllocationAnnotation getAllocation() {
		return allocation;
	}

	public void setAllocation(IAllocationAnnotation allocation) {
		this.allocation = allocation;
	}

	public @NonNull IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public void setPortTimesRecord(@NonNull IPortTimesRecord portTimesRecord) {
		this.portTimesRecord = portTimesRecord;
	}

	public boolean isIgnoreEndSet() {
		return ignoreEndSet;
	}

	public void setIgnoreEndSet(boolean ignoreEndSet) {
		this.ignoreEndSet = ignoreEndSet;
	}
}