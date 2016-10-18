/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

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
