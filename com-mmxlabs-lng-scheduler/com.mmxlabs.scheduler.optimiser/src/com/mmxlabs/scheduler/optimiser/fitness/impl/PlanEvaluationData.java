package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Used for plan evaluation in the {@link VoyagePlanner} and Charter Out Generation
 */
public class PlanEvaluationData {
	
	VoyagePlan plan;
	long endHeelVolumeInM3;
	long startHeelVolumeInM3;
	IAllocationAnnotation allocation;
	IPortTimesRecord portTimesRecord;
	boolean ignoreEndSet;

	public PlanEvaluationData() {
		plan = null;
		endHeelVolumeInM3 = 0;
		startHeelVolumeInM3 = 0;
		allocation = null;
		portTimesRecord = null;
		ignoreEndSet = false;
	}

	public VoyagePlan getPlan() {
		return plan;
	}

	public void setPlan(VoyagePlan plan) {
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

	public IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public void setPortTimesRecord(IPortTimesRecord portTimesRecord) {
		this.portTimesRecord = portTimesRecord;
	}

	public boolean isIgnoreEndSet() {
		return ignoreEndSet;
	}

	public void setIgnoreEndSet(boolean ignoreEndSet) {
		this.ignoreEndSet = ignoreEndSet;
	}
}