/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.CommodityCurve;

@NonNullByDefault
public class CommodityNode extends AbstractMarkedUpNode {
	private CommodityCurve curve;

	public CommodityNode(CommodityCurve curve) {
		this.curve = curve;

	}

	public CommodityCurve getCurve() {
		return curve;
	}
}