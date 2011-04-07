/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * Interface for objects which calculate a load price from various parameters
 * 
 * @author hinton
 * 
 */
public interface ILoadPriceCalculator {
	public int calculateLoadUnitPrice(final int loadTime,
			final long loadVolume, final int dischargeTime,
			final int actualSalesPrice, final int cvValue,
			final VoyageDetails ladenLeg, final VoyageDetails ballastLeg,
			final IVesselClass vesselClass);
}
