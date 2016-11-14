/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class BreakevenNode extends AbstractMarkedUpNode {
	private double constant = 1.0;

	public BreakevenNode() {
		this.constant = constant;
	}

	public double getConstant() {
		return constant;
	}
}
