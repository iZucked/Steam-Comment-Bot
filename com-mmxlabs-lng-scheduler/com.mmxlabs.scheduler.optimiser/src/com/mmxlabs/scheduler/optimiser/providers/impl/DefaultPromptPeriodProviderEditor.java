/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProviderEditor;

public class DefaultPromptPeriodProviderEditor implements IPromptPeriodProviderEditor {

	private int startOfPromptPeriod = Integer.MIN_VALUE;
	private int endOfPromptPeriod = Integer.MAX_VALUE;

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
}
