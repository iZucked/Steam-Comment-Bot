/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

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
