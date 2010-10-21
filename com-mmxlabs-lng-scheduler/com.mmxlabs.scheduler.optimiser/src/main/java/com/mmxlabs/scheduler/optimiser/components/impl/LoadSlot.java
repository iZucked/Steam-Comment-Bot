/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
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

	private int purchasePrice;

	private int cargoCVValue;

	public LoadSlot() {
		setPortType(PortType.Load);
	}

	public LoadSlot(final String id, final IPort port,
			final ITimeWindow timwWindow, final long minLoadVolume,
			final long maxLoadVolume, final int purchasePrice,
			final int cargoCVValue) {
		super(id, port, timwWindow);
		this.minLoadVolume = minLoadVolume;
		this.maxLoadVolume = maxLoadVolume;
		this.purchasePrice = purchasePrice;
		this.cargoCVValue = cargoCVValue;
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

	@Override
	public int getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(final int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

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
			
			if (purchasePrice != slot.purchasePrice) {
				return false;
			}

			if (cargoCVValue != slot.cargoCVValue) {
				return false;
			}
			
			return super.equals(obj);
		}
		return false;
	}

	public int getCargoCVValue() {
		return cargoCVValue;
	}

	public void setCargoCVValue(final int cargoCVValue) {
		this.cargoCVValue = cargoCVValue;
	}

}
