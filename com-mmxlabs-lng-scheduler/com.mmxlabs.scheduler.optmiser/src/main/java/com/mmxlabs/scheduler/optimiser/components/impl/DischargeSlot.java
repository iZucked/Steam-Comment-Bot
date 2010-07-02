package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

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

	public DischargeSlot() {

	}

	public DischargeSlot(final String id, final IPort port,
			final ITimeWindow timwWindow, final long minDischargeVolume,
			final long maxDischargeVolume, final long salesPrice) {
		super(id, port, timwWindow);
		this.minDischargeVolume = minDischargeVolume;
		this.maxDischargeVolume = maxDischargeVolume;
		this.salesPrice = salesPrice;
	}

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

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DischargeSlot) {
			final DischargeSlot slot = (DischargeSlot) obj;

			if (maxDischargeVolume != slot.maxDischargeVolume) {
				return false;
			}
			if (minDischargeVolume != slot.minDischargeVolume) {
				return false;
			}
			if (salesPrice != slot.salesPrice) {
				return false;
			}

			return super.equals(obj);
		}
		return false;
	}
}