/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
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
	private ILoadPriceCalculator2 loadPriceCalculator;
	/**
	 * The CV value of gas at this option.
	 */
	private int cargoCVValue;

	public LoadOption() {
		setPortType(PortType.Load);
	}

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
	public LoadOption(String id, IPort port, ITimeWindow timeWindow, long minLoadVolume, long maxLoadVolume, ILoadPriceCalculator2 loadPriceCalculator, int cargoCVValue) {
		super(id, port, timeWindow);
		this.minLoadVolume = minLoadVolume;
		this.maxLoadVolume = maxLoadVolume;
		this.loadPriceCalculator = loadPriceCalculator;
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

	public void setLoadPriceCalculator(final ILoadPriceCalculator2 loadPriceCalculator) {
		this.loadPriceCalculator = loadPriceCalculator;
	}

	@Override
	public int getCargoCVValue() {
		return cargoCVValue;
	}

	public void setCargoCVValue(final int cargoCVValue) {
		this.cargoCVValue = cargoCVValue;
	}

	@Override
	public ILoadPriceCalculator2 getLoadPriceCalculator() {
		return loadPriceCalculator;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj instanceof LoadOption) {
			final LoadOption lo = (LoadOption) obj;
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

}
