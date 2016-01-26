/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LoadSlot extends LoadOption implements ILoadSlot {

	private boolean cooldownSet;

	private boolean cooldownForbidden;

	public LoadSlot() {
		setPortType(PortType.Load);
	}

	public LoadSlot(final String id, final IPort port, final ITimeWindow timwWindow, final long minLoadVolume, final long maxLoadVolume, final ILoadPriceCalculator loadPriceCalculator,
			final int cargoCVValue, final boolean cooldownSet, final boolean cooldownForbidden) {
		super(id, port, timwWindow, minLoadVolume, maxLoadVolume, loadPriceCalculator, cargoCVValue);
		this.cooldownSet = cooldownSet;
		this.cooldownForbidden = cooldownForbidden;
	}

	// @Override
	// public int getPurchasePriceAtTime(final int time) {
	// return (int) purchasePriceCurve.getValueAtPoint(time);
	// }

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof LoadSlot) {
			final LoadSlot slot = (LoadSlot) obj;

			if (cooldownSet != slot.cooldownSet) {
				return false;
			}

			if (cooldownForbidden != slot.cooldownForbidden) {
				return false;
			}

			return super.equals(obj);
		}
		return false;
	}

	@Override
	public final boolean isCooldownSet() {
		return cooldownSet;
	}

	public final void setCooldownSet(final boolean cooldownSet) {
		this.cooldownSet = cooldownSet;
	}

	@Override
	public final boolean isCooldownForbidden() {
		return cooldownForbidden;
	}

	public final void setCooldownForbidden(final boolean cooldownForbidden) {
		this.cooldownForbidden = cooldownForbidden;
	}
}
