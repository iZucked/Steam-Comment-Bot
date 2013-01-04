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
	
	private long minCvValue;
	
	private long maxCvValue;

	private ISalesPriceCalculator priceCalculator;

	public DischargeOption() {
		setPortType(PortType.Discharge);
	}

	/**
	 * @since 2.0
	 */
	public DischargeOption(final String id, final IPort port, final ITimeWindow timeWindow, final long minDischargeVolume, final long maxDischargeVolume, final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator) {
		super(id, port, timeWindow);
		this.minDischargeVolume = minDischargeVolume;
		this.maxDischargeVolume = maxDischargeVolume;
		this.priceCalculator = priceCalculator;
		this.minCvValue = minCvValue;
		this.maxCvValue = maxCvValue;
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

	/**
	 * @since 2.0
	 */
	@Override
	public long getMinCvValue() {
		return minCvValue;
	}

	/**
	 * @since 2.0
	 */
	public void setMinCvValue(long value) {
		minCvValue = value;
	}
	
	/**
	 * @since 2.0
	 */
	@Override
	public long getMaxCvValue() {
		return maxCvValue;
	}

	/**
	 * @since 2.0
	 */
	public void setMaxCvValue(long value) {
		maxCvValue = value;
	}
	
}
