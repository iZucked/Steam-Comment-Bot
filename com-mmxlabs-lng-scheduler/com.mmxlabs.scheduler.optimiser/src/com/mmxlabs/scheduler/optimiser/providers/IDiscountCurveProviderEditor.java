/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.common.curves.ICurve;

/**
 * @author Tom Hinton
 * 
 */
public interface IDiscountCurveProviderEditor extends IDiscountCurveProvider {
	public void setDiscountCurve(final String componentName, final ICurve discountCurve);
}
