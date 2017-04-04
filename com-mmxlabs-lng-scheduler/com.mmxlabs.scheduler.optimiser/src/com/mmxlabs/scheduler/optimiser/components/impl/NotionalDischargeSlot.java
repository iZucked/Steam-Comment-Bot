/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

public class NotionalDischargeSlot extends DischargeSlot {

	public NotionalDischargeSlot(@NonNull String id, @NonNull IPort port, ITimeWindow timeWindow, boolean volumeInM3, long minDischargeVolume, long maxDischargeVolume,
			ISalesPriceCalculator priceCalculator, long minCvValue, long maxCvValue) {
		super(id, port, timeWindow, volumeInM3, minDischargeVolume, maxDischargeVolume, priceCalculator, minCvValue, maxCvValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof NotionalDischargeSlot) {
			return super.doEquals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
