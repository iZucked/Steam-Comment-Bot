/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.scheduler.optimiser.annotations.IHedgingAnnotation;

public final class HedgingAnnotation implements IHedgingAnnotation {

	private final long hedgingValue;

	public HedgingAnnotation(final long hedgingValue) {
		this.hedgingValue = hedgingValue;

	}

	@Override
	public long getHedgingValue() {
		return hedgingValue;
	}
}
