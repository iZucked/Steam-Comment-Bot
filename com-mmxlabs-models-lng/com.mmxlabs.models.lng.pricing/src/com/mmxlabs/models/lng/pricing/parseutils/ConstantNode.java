/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class ConstantNode extends AbstractMarkedUpNode {
	private double constant;

	public ConstantNode(double constant) {
		this.constant = constant;

	}

	public double getConstant() {
		return constant;
	}
}
