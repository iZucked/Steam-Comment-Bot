/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DischargeSlot extends PortSlot implements IDischargeSlot {

	private long minDischargeVolume;

	private long maxDischargeVolume;

	private int salesPrice;

	public DischargeSlot() {
		setPortType(PortType.Discharge);
	}

	public DischargeSlot(final String id, final IPort port,
			final ITimeWindow timwWindow, final long minDischargeVolume,
			final long maxDischargeVolume, final int salesPrice) {
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
	public int getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(final int salesPrice) {
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