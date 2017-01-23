/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * @author hinton
 * 
 */
public class DischargeOption extends PortSlot implements IDischargeOption {

	private long minDischargeVolume;

	private long maxDischargeVolume;

	private long minDischargeVolumeMMBTU;

	private long maxDischargeVolumeMMBTU;

	private boolean volumeSetInM3;

	private long minCvValue;

	private long maxCvValue;

	private ISalesPriceCalculator priceCalculator;

	private int pricingDate = IPortSlot.NO_PRICING_DATE;

	private @NonNull PricingEventType pricingEvent = PricingEventType.START_OF_DISCHARGE;

	// public DischargeOption() {
	// setPortType(PortType.Discharge);
	// }

	/**
	 */
	public DischargeOption(final @NonNull String id, final @NonNull IPort port, final ITimeWindow timeWindow, boolean volumeInM3, final long minDischargeVolume, final long maxDischargeVolume,
			final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator) {
		super(id, PortType.Discharge, port, timeWindow);
		this.volumeSetInM3 = volumeInM3;
		this.minDischargeVolume = minDischargeVolume;
		this.maxDischargeVolume = maxDischargeVolume;
		this.priceCalculator = priceCalculator;
		this.minCvValue = minCvValue;
		this.maxCvValue = maxCvValue;
	}

	@Override
	public long getMinDischargeVolume(int cv) {
		if (volumeSetInM3) {
			return minDischargeVolume;
		} else {
			assert cv > 0;
			return Calculator.convertMMBTuToM3(minDischargeVolumeMMBTU, cv);
		}
	}

	@Override
	public long getMaxDischargeVolume(int cv) {
		if (volumeSetInM3) {
			return maxDischargeVolume;
		} else {
			if (maxDischargeVolumeMMBTU == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			assert cv > 0;
			return Calculator.convertMMBTuToM3(maxDischargeVolumeMMBTU, cv);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DischargeOption) {
			final DischargeOption slot = (DischargeOption) obj;

			if (volumeSetInM3 != slot.volumeSetInM3) {
				return false;
			}
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
	 */
	@Override
	public long getMinCvValue() {
		return minCvValue;
	}

	/**
	 */
	public void setMinCvValue(final long value) {
		minCvValue = value;
	}

	/**
	 */
	@Override
	public long getMaxCvValue() {
		return maxCvValue;
	}

	/**
	 */
	public void setMaxCvValue(final long value) {
		maxCvValue = value;
	}

	/**
	 */
	@Override
	public int getPricingDate() {
		return pricingDate;
	}

	/**
	 */
	public void setPricingDate(final int value) {
		pricingDate = value;
	}

	@Override
	public PricingEventType getPricingEvent() {
		return pricingEvent;
	}

	public void setPricingEvent(@NonNull PricingEventType pricingEvent) {
		this.pricingEvent = pricingEvent;
	}

	@Override
	public long getMinDischargeVolumeMMBTU(int cv) {
		if (volumeSetInM3) {
			assert cv > 0;
			return Calculator.convertM3ToMMBTu(minDischargeVolume, cv);
		} else {
			return minDischargeVolumeMMBTU;
		}
	}

	@Override
	public long getMaxDischargeVolumeMMBTU(int cv) {
		if (volumeSetInM3) {
			if (maxDischargeVolume == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			assert cv > 0;
			return Calculator.convertM3ToMMBTu(maxDischargeVolume, cv);
		} else {
			return maxDischargeVolumeMMBTU;
		}
	}

	public void setVolumeLimits(boolean m3, long minVolume, long maxVolume) {
		this.volumeSetInM3 = m3;
		if (volumeSetInM3) {
			this.minDischargeVolume = minVolume;
			this.maxDischargeVolume = maxVolume;
			this.minDischargeVolumeMMBTU = 0;
			this.maxDischargeVolumeMMBTU = 0;
		} else {
			this.minDischargeVolume = 0;
			this.maxDischargeVolume = 0;
			this.minDischargeVolumeMMBTU = minVolume;
			this.maxDischargeVolumeMMBTU = maxVolume;
		}
	}

	@Override
	public boolean isVolumeSetInM3() {
		return volumeSetInM3;
	}

}
