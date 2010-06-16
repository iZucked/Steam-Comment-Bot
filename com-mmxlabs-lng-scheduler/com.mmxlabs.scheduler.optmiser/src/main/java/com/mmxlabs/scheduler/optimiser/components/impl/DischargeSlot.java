package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DischargeSlot extends PortSlot implements IDischargeSlot {

	private long minDischargeVolume;

	private long maxDischargeVolume;

	private long salesPrice;

	@Override
	public long getMinDischargeVolume() {
		return minDischargeVolume;
	}

	public void setMinDischargeVolume(final long minDischargeVolume) {
		this.minDischargeVolume = minDischargeVolume;
	}

	@Override
	public long getMaxDischargeVolume() {
		return maxDischargeVolume;
	}

	public void setMaxDischargeVolume(final long maxDischargeVolume) {
		this.maxDischargeVolume = maxDischargeVolume;
	}

	@Override
	public long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(final long salesPrice) {
		this.salesPrice = salesPrice;
	}

}