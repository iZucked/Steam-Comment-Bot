/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * @author Tom Hinton
 * 
 */
public interface IDiscountCurveProvider extends IDataComponentProvider {
	public ICurve getDiscountCurve(final String componentName);
}
