/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.UnitConversion;

@NonNullByDefault
public class ConversionNode extends AbstractMarkedUpNode {
	private final String name;
	private final UnitConversion factor;
	private final boolean isReverse;

	public boolean isReverse() {
		return isReverse;
	}

	public ConversionNode(final String name, final UnitConversion factor, boolean isReverse) {
		this.name = name;
		this.factor = factor;
		this.isReverse = isReverse;

	}

	public String getName() {
		return name;
	}

	public UnitConversion getFactor() {
		return factor;
	}
}