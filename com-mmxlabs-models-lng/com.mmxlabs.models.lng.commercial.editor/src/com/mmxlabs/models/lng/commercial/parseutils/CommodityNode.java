/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.CommodityIndex;

@NonNullByDefault
public class CommodityNode extends AbstractMarkedUpNode {
	private CommodityIndex index;

	public CommodityNode(CommodityIndex index) {
		this.index = index;

	}

	public CommodityIndex getIndex() {
		return index;
	}
}