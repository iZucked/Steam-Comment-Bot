/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public class LoadSlot extends LoadOption implements ILoadSlot {

	private boolean cooldownSet;

	private boolean cooldownForbidden;

	public LoadSlot(final @NonNull String id, final @NonNull IPort port, final ITimeWindow timeWindow, boolean volumeInM3, final long minLoadVolume, final long maxLoadVolume,
			final @NonNull ILoadPriceCalculator loadPriceCalculator, final int cargoCVValue, final boolean cooldownSet, final boolean cooldownForbidden) {
		super(id, port, timeWindow, volumeInM3, minLoadVolume, maxLoadVolume, loadPriceCalculator, cargoCVValue);
		this.cooldownSet = cooldownSet;
		this.cooldownForbidden = cooldownForbidden;
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

	@Override
	public boolean doEquals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof LoadSlot) {
			final LoadSlot slot = (LoadSlot) obj;

			if (cooldownSet != slot.cooldownSet) {
				return false;
			}

			if (cooldownForbidden != slot.cooldownForbidden) {
				return false;
			}

			return super.doEquals(obj);
		}
		return false;
	}
}
