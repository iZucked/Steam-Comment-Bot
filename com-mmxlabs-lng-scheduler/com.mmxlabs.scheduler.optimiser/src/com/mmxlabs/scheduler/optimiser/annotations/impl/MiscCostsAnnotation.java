/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.scheduler.optimiser.annotations.IMiscCostsAnnotation;

public final class MiscCostsAnnotation implements IMiscCostsAnnotation {

	private final long miscCostsValue;

	public MiscCostsAnnotation(final long hedgingValue) {
		this.miscCostsValue = hedgingValue;

	}

	@Override
	public long getMiscCostsValue() {
		return miscCostsValue;
	}
}
