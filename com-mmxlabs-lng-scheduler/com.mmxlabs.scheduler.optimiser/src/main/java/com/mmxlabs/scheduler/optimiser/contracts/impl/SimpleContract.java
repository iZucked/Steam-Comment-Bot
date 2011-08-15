/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * @author hinton
 *
 */
public abstract class SimpleContract implements ISimpleLoadPriceCalculator,
		ILoadPriceCalculator {

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator#calculateLoadUnitPrice(int, long, int, int, int, com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails, com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails, com.mmxlabs.scheduler.optimiser.components.IVesselClass)
	 */
	@Override
	public int calculateLoadUnitPrice(int loadTime, long loadVolume,
			int dischargeTime, int actualSalesPrice, int cvValue,
			VoyageDetails ladenLeg, VoyageDetails ballastLeg,
			IVesselClass vesselClass) {
		// TODO Auto-generated method stub
		return calculateSimpleLoadUnitPrice(loadTime);
	}
}
