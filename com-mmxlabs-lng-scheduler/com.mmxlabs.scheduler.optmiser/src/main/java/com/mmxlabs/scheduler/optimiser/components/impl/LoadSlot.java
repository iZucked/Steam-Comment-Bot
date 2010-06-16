package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LoadSlot extends PortSlot implements ILoadSlot {

	private long minLoadVolume;

	private long maxLoadVolume;

	private long purchasePrice;

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
	public long getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(final long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
}
