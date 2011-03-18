/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class MarketPriceContract implements ILoadPriceCalculator {
	private final ICurve market;

	public MarketPriceContract(ICurve market) {
		super();
		this.market = market;
	}

	public int calculateLoadUnitPrice(final int loadTime,
			final long loadVolume, final int dischargeTime,
			final int actualSalesPrice, final int cvValue,
			final VoyageDetails ladenLeg, final VoyageDetails ballastLeg,
			final IVesselClass vesselClass) {

		return (int) market.getValueAtPoint(loadTime);
	}
}
