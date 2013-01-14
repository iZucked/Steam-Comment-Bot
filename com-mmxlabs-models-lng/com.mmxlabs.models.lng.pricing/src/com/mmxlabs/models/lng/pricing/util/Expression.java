/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Date;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class Expression<Value> implements IExpression<Value> {
	private String expressionString;
	
	@Override
	public DataIndex<Value> computeAll(PricingModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexPoint<Value> computeValue(PricingModel model, Date before) {
		// TODO Auto-generated method stub
		return null;
	}

}
