/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.annotations.ILatenessAnnotation;

/**
 */
public class LatenessAnnotation implements ILatenessAnnotation {

	private final int amount;
	private final ITimeWindow timeWindow;
	private final int actualTime;

	public LatenessAnnotation(final int amount, final ITimeWindow timeWindow, final int actualTime) {
		this.amount = amount;
		this.timeWindow = timeWindow;
		this.actualTime = actualTime;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getActualTime() {
		return actualTime;
	}
}
