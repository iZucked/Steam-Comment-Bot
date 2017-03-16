/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProviderEditor;

public class DefaultPromptPeriodProviderEditor implements IPromptPeriodProviderEditor {

	private int startOfPromptPeriod = Integer.MIN_VALUE;
	private int endOfPromptPeriod = Integer.MAX_VALUE;
	private int endOfSchedulingPeriod = Integer.MAX_VALUE;

	private int startOfOptimisationPeriod = Integer.MIN_VALUE;
	private int endOfOptimisationPeriod = Integer.MAX_VALUE;
	private boolean isPeriodOptimisation = false;

	@Override
	public int getEndOfSchedulingPeriod() {
		return endOfSchedulingPeriod;
	}

	@Override
	public void setEndOfSchedulingPeriod(final int endOfSchedulingPeriod) {
		this.endOfSchedulingPeriod = endOfSchedulingPeriod;
	}

	@Override
	public int getStartOfPromptPeriod() {
		return startOfPromptPeriod;
	}

	@Override
	public int getEndOfPromptPeriod() {
		return endOfPromptPeriod;
	}

	@Override
	public void setStartOfPromptPeriod(final int time) {
		this.startOfPromptPeriod = time;
	}

	@Override
	public void setEndOfPromptPeriod(final int time) {
		this.endOfPromptPeriod = time;
	}

	@Override
	public boolean isPeriodOptimisation() {
		return isPeriodOptimisation;
	}

	@Override
	public int getStartOfOptimisationPeriod() {
		return startOfOptimisationPeriod;
	}

	@Override
	public int getEndOfOptimisationPeriod() {
		return endOfOptimisationPeriod;
	}

	@Override
	public void setPeriodOptimisation(final boolean isPeriodOptimisation) {
		this.isPeriodOptimisation = isPeriodOptimisation;

	}

	@Override
	public void setStartOfOptimisationPeriod(final int time) {
		this.startOfOptimisationPeriod = time;
	}

	@Override
	public void setEndOfOptimisationPeriod(final int time) {
		this.endOfOptimisationPeriod = time;
	}
}
