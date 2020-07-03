/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.BasicCommodityCurveData;

@NonNullByDefault
public class CommodityNode extends AbstractMarkedUpNode {	
	private BasicCommodityCurveData curve;

	public CommodityNode(final String name, final String volumeUnit, final String currencyUnit, final @Nullable String expression) {
		curve = new BasicCommodityCurveData(name, volumeUnit, currencyUnit, expression);
	}
	
	public CommodityNode(final BasicCommodityCurveData curve) {
		this.curve = curve;
	}

	public boolean isSetExpression() {
		return curve.isSetExpression();
	}
	
	public String getName() {
		return curve.getName();
	}
	
	public String getVolumeUnit() {
		return curve.getVolumeUnit();
	}

	public String getCurrencyUnit() {
		return curve.getCurrencyUnit();
	}

	public @Nullable String getExpression() {
		return curve.getExpression();
	}
}