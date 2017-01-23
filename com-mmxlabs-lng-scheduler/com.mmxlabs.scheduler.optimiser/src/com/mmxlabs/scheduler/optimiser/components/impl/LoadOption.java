/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A load option models things which can come before a discharge option; these are either {@link ILoadSlot}s, which correspond to actual physical load opportunities which we will ship, or they can be
 * some other kind of option, for example a DES purchase.
 * 
 * @author hinton
 * 
 */
public class LoadOption extends PortSlot implements ILoadOption {
	/**
	 * The minimum volume which must be loaded at this load option
	 */
	private long minLoadVolume;
	/**
	 * The maximum volume which may be loaded at this load option
	 */
	private long maxLoadVolume;
	/**
	 * A price calculator which can compute the unit price for this option.
	 */

	private long minLoadVolumeMMBTU;

	private long maxLoadVolumeMMBTU;

	private boolean volumeSetInM3;

	private ILoadPriceCalculator loadPriceCalculator;
	/**
	 * The CV value of gas at this option.
	 */
	private int cargoCVValue;

	/**
	 * The date which LNG price indices should be based on.
	 */
	private int pricingDate = IPortSlot.NO_PRICING_DATE;

	private PricingEventType pricingEvent = PricingEventType.START_OF_LOAD;

	//
	// public LoadOption() {
	// setPortType(PortType.Load);
	// }

	/**
	 * Construct a new load option
	 * 
	 * @param id
	 *            - a key identifying this option
	 * @param port
	 *            - the port where this option is located
	 * @param timeWindow
	 *            - the time window in which this option is available
	 * @param minLoadVolume
	 *            - the minimum amount which must be used here
	 * @param maxLoadVolume
	 *            - the maximum amount that may be used here
	 * @param loadPriceCalculator
	 *            - price calculator to set unit prices here
	 * @param cargoCVValue
	 *            - the CV value for gas here.
	 */
	public LoadOption(final @NonNull String id, final IPort port, final @NonNull ITimeWindow timeWindow, boolean volumeInM3, final long minLoadVolume, final long maxLoadVolume,
			final ILoadPriceCalculator loadPriceCalculator, final int cargoCVValue) {
		super(id, PortType.Load, port, timeWindow);
		this.loadPriceCalculator = loadPriceCalculator;
		assert cargoCVValue != 0;
		this.cargoCVValue = cargoCVValue;
		setVolumeLimits(volumeInM3, minLoadVolume, maxLoadVolume);
	}

	@Override
	public long getMinLoadVolume() {
		return minLoadVolume;
	}

	@Override
	public void setVolumeLimits(boolean volumeInM3, long minVolume, long maxVolume) {
		this.volumeSetInM3 = volumeInM3;
		if (volumeInM3) {
			minLoadVolume = minVolume;
			maxLoadVolume = maxVolume;
			minLoadVolumeMMBTU = Calculator.convertM3ToMMBTu(minVolume, getCargoCVValue());
			maxLoadVolumeMMBTU = maxLoadVolume == Long.MAX_VALUE ? Long.MAX_VALUE : Calculator.convertM3ToMMBTu(maxLoadVolume, getCargoCVValue());
		} else {
			minLoadVolumeMMBTU = minVolume;
			maxLoadVolumeMMBTU = maxVolume;
			minLoadVolume = Calculator.convertMMBTuToM3(minLoadVolumeMMBTU, getCargoCVValue());
			maxLoadVolume = maxLoadVolumeMMBTU == Long.MAX_VALUE ? Long.MAX_VALUE : Calculator.convertMMBTuToM3(maxLoadVolumeMMBTU, getCargoCVValue());
		}
	}

	@Override
	public long getMaxLoadVolume() {
		return maxLoadVolume;
	}

	public void setLoadPriceCalculator(final ILoadPriceCalculator loadPriceCalculator) {
		this.loadPriceCalculator = loadPriceCalculator;
	}

	@Override
	public int getCargoCVValue() {
		return cargoCVValue;
	}

	public void setCargoCVValue(final int cargoCVValue) {
		this.cargoCVValue = cargoCVValue;
		// CV has changed, so update volume limits
		if (isVolumeSetInM3()) {
			setVolumeLimits(true, minLoadVolume, maxLoadVolume);
		} else {
			setVolumeLimits(false, minLoadVolumeMMBTU, maxLoadVolumeMMBTU);
		}
	}

	@Override
	public ILoadPriceCalculator getLoadPriceCalculator() {
		return loadPriceCalculator;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof LoadOption) {
			final LoadOption lo = (LoadOption) obj;
			if (volumeSetInM3 != lo.volumeSetInM3) {
				return false;
			}

			if (minLoadVolume != lo.minLoadVolume) {
				return false;
			}

			if (maxLoadVolume != lo.maxLoadVolume) {
				return false;
			}

			if (loadPriceCalculator != lo.loadPriceCalculator) {
				return false;
			}

			if (cargoCVValue != lo.cargoCVValue) {
				return false;
			}

			return super.equals(obj);
		}

		return false;
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

	public void setPricingEvent(PricingEventType pricingEvent) {
		this.pricingEvent = pricingEvent;
	}

	@Override
	public long getMinLoadVolumeMMBTU() {
		return minLoadVolumeMMBTU;
	}

	@Override
	public long getMaxLoadVolumeMMBTU() {
		return maxLoadVolumeMMBTU;
	}

	@Override
	public boolean isVolumeSetInM3() {
		return volumeSetInM3;
	}

}
