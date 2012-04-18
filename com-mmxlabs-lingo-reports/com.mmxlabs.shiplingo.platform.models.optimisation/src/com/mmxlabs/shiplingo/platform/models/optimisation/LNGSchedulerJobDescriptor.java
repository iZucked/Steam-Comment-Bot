/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import java.io.Serializable;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSchedulerJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private MMXRootObject scenario;

	private boolean optimise;

	public LNGSchedulerJobDescriptor(final String name, final MMXRootObject scenario, final boolean optimise) {
		this.name = name;
		this.scenario = scenario;
		this.optimise = true;
	}

	@Override
	public void dispose() {
		scenario = null;
	}

	@Override
	public String getJobName() {
		return name;
	}

	@Override
	public MMXRootObject getJobContext() {
		return scenario;
	}

	@Override
	public Object getJobMetadata() {
		return null;
	}

	@Override
	public Object getJobType() {
		return null;
	}

	/**
	 * @return
	 */
	public boolean isOptimising() {
		return optimise;
	}

	public void setOptimising(final boolean optimise) {
		this.optimise = optimise;
	}
}
