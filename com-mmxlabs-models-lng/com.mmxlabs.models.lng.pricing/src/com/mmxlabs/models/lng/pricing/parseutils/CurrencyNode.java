/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.CurrencyCurve;

@NonNullByDefault
public class CurrencyNode extends AbstractMarkedUpNode {

	private CurrencyCurve curve;

	public CurrencyNode(CurrencyCurve curve) {
		this.curve = curve;
	}

	public CurrencyCurve getCurve() {
		return curve;
	}
}
