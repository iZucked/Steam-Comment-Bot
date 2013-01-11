/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Date;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;

/**
 * Interface for parsed expressions from derived indices.
 * @author hinton
 *
 */
public interface IExpression<Value> {
	public DataIndex<Value> computeAll(final PricingModel model);
	public IndexPoint<Value> computeValue(final PricingModel model, final Date before);
}
