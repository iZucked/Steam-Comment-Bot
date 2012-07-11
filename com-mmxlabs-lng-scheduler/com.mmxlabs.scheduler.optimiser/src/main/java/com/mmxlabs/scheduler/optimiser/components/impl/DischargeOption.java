/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * @author hinton
 * 
 */
public class DischargeOption extends PortSlot implements IDischargeOption {

	private long minDischargeVolume;

	private long maxDischargeVolume;

	private ISalesPriceCalculator priceCalculator;

	public DischargeOption() {
		setPortType(PortType.Discharge);
	}

	public DischargeOption(final String id, final IPort port, final ITimeWindow timwWindow, final long minDischargeVolume, final long maxDischargeVolume, final ISalesPriceCalculator priceCalculator) {
		super(id, port, timwWindow);
		this.minDischargeVolume = minDischargeVolume;
		this.maxDischargeVolume = maxDischargeVolume;
		this.priceCalculator = priceCalculator;
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
	public boolean equals(final Object obj) {
		if (obj instanceof DischargeOption) {
			final DischargeOption slot = (DischargeOption) obj;

			if (maxDischargeVolume != slot.maxDischargeVolume) {
				return false;
			}
			if (minDischargeVolume != slot.minDischargeVolume) {
				return false;
			}
			if (priceCalculator != slot.priceCalculator) {
				return false;
			}

			return super.equals(obj);
		}
		return false;
	}

	/**
	 * @param salesPriceCurve
	 */
	public void setDischargePriceCalculator(final ISalesPriceCalculator priceCalculator) {
		this.priceCalculator = priceCalculator;
	}

	@Override
	public final ISalesPriceCalculator getDischargePriceCalculator() {
		return priceCalculator;
	}

}
