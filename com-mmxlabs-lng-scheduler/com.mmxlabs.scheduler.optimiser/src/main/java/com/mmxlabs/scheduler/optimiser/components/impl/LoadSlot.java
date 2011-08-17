/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
public final class LoadSlot extends PortSlot implements ILoadSlot {

	private long minLoadVolume;

	private long maxLoadVolume;

//	private ICurve purchasePriceCurve;

	private ILoadPriceCalculator loadPriceCalculator;
	
	private int cargoCVValue;

	private boolean cooldownSet;
	
	private boolean cooldownForbidden;
	
	public LoadSlot() {
		setPortType(PortType.Load);
	}

	public LoadSlot(final String id, final IPort port,
			final ITimeWindow timwWindow, final long minLoadVolume,
			final long maxLoadVolume, final ILoadPriceCalculator loadPriceCalculator,
			final int cargoCVValue, final boolean cooldownSet, final boolean cooldownForbidden) {
		super(id, port, timwWindow);
		this.minLoadVolume = minLoadVolume;
		this.maxLoadVolume = maxLoadVolume;
		this.loadPriceCalculator = loadPriceCalculator;
		this.cargoCVValue = cargoCVValue;
		this.cooldownSet = cooldownSet;
		this.cooldownForbidden = cooldownForbidden;
	}

	@Override
	public long getMinLoadVolume() {
		return minLoadVolume;
	}

	public void setMinLoadVolume(final long minLoadVolume) {
		this.minLoadVolume = minLoadVolume;
	}

	@Override
	public long getMaxLoadVolume() {
		return maxLoadVolume;
	}

	public void setMaxLoadVolume(final long maxLoadVolume) {
		this.maxLoadVolume = maxLoadVolume;
	}

	public void setLoadPriceCalculator(final ILoadPriceCalculator loadPriceCalculator) {
		this.loadPriceCalculator = loadPriceCalculator;
	}

//	@Override
//	public int getPurchasePriceAtTime(final int time) {
//		return (int) purchasePriceCurve.getValueAtPoint(time);
//	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof LoadSlot) {
			final LoadSlot slot = (LoadSlot) obj;

			if (minLoadVolume != slot.minLoadVolume) {
				return false;
			}

			if (maxLoadVolume != slot.maxLoadVolume) {
				return false;
			}

			if (loadPriceCalculator != slot.loadPriceCalculator) {
				return false;
			}

			if (cargoCVValue != slot.cargoCVValue) {
				return false;
			}
			
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
	public int getCargoCVValue() {
		return cargoCVValue;
	}

	public void setCargoCVValue(final int cargoCVValue) {
		this.cargoCVValue = cargoCVValue;
	}

	@Override
	public ILoadPriceCalculator getLoadPriceCalculator() {
		return loadPriceCalculator;
	}

	public final boolean isCooldownSet() {
		return cooldownSet;
	}

	public final void setCooldownSet(boolean cooldownSet) {
		this.cooldownSet = cooldownSet;
	}

	public final boolean isCooldownForbidden() {
		return cooldownForbidden;
	}

	public final void setCooldownForbidden(boolean cooldownForbidden) {
		this.cooldownForbidden = cooldownForbidden;
	}
}
