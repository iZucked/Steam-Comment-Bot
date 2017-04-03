/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 * LoadSlot sub class implementing equals/hashCode for notional slots.
 * 
 * @author Simon Goodall
 * 
 */
public final class NotionalLoadSlot extends LoadSlot {

	public NotionalLoadSlot(@NonNull String id, @NonNull IPort port, ITimeWindow timeWindow, boolean volumeInM3, long minLoadVolume, long maxLoadVolume,
			@NonNull ILoadPriceCalculator loadPriceCalculator, int cargoCVValue, boolean cooldownSet, boolean cooldownForbidden) {
		super(id, port, timeWindow, volumeInM3, minLoadVolume, maxLoadVolume, loadPriceCalculator, cargoCVValue, cooldownSet, cooldownForbidden);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof NotionalLoadSlot) {
			return super.doEquals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
