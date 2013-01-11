/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.common.detailtree.IDetailTree;

/**
 * @since 2.0
 * 
 */
public interface IShippingCostAnnotation {

	int getBookingTime();

	long getShippingCost();

	IDetailTree getDetails();
}
