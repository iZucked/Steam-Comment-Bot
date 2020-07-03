/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.curves.BasicUnitConversionData;

@NonNullByDefault
public class ConversionNode extends AbstractMarkedUpNode {
	private final String name;
	private final BasicUnitConversionData factor;
	private final boolean isReverse;

	public boolean isReverse() {
		return isReverse;
	}

	public ConversionNode(final String name, final BasicUnitConversionData factor, boolean isReverse) {
		this.name = name;
		this.factor = factor;
		this.isReverse = isReverse;

	}

	public String getName() {
		return name;
	}

	public BasicUnitConversionData getFactor() {
		return factor;
	}

	public String getToUnits() {
		if (isReverse) {
			return factor.getFrom();
		}
		return factor.getTo();
	}
}