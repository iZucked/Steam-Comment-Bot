/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.CurrencyIndex;

@NonNullByDefault
public class CurrencyNode extends AbstractMarkedUpNode {

	private CurrencyIndex index;

	public CurrencyNode(CurrencyIndex index) {
		this.index = index;

	}

	public CurrencyIndex getIndex() {
		return index;
	}
}
