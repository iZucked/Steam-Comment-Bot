/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.scheduler.optimiser.annotations.ICancellationAnnotation;

public final class CancellationAnnotation implements ICancellationAnnotation {

	private final long cancellationFees;

	public CancellationAnnotation(final long cancellationFees) {
		this.cancellationFees = cancellationFees;

	}

	@Override
	public long getCancellationFees() {
		return cancellationFees;
	}
}
