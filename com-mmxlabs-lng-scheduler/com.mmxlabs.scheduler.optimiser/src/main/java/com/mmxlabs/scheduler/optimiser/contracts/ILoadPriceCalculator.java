/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Interface for objects which calculate a load price from various parameters
 * 
 * @author hinton
 * 
 */
public interface ILoadPriceCalculator {
	public int calculateLoadUnitPrice(final VoyagePlan voyagePlan);
}
