/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * @author hinton
 * 
 */
public interface IDischargeOption extends IPortSlot {
	/**
	 * Returns the minimum quantity that can be discharged. A value of zero is equivalent to no minimum bound.
	 * 
	 * @return
	 */
	long getMinDischargeVolume();

	/**
	 * Returns the maximum quantity that can be discharged. A value of {@link Long#MAX_VALUE} is equivalent to no maximum bound.
	 * 
	 * @return
	 */
	long getMaxDischargeVolume();

	/**
	 * Returns the minimum CV for this discharge slot, based on the sales contract. A value of zero is equivalent to no minimum bound.
	 * 
	 * @return
	 * @since 2.0
	 */
	long getMinCvValue();

	/**
	 * Returns the maximum CV for this discharge slot, based on the sales contract. A value of {@link Long#MAX_VALUE} is equivalent to no maximum bound.
	 * 
	 * @return
	 * @since 2.0
	 */
	long getMaxCvValue();

	ISalesPriceCalculator getDischargePriceCalculator();

	int getPricingDate();
}
