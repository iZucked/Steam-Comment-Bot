/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @since 2.0
 * 
 */
public interface IShippingCostTransformer {

	/**
	 * Single method which evaluates a {@link ShippingCostPlan}.
	 */
	public List<UnitCostLine> evaulateShippingPlan(final MMXRootObject root, final ShippingCostPlan plan, final IProgressMonitor monitor);
}
