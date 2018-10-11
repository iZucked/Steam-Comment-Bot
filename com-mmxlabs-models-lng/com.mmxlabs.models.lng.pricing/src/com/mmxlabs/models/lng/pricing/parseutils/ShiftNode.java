/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class ShiftNode extends AbstractMarkedUpNode {
	private MarkedUpNode shiftedNode;
	private int months;

	public ShiftNode(MarkedUpNode shiftedNode, int months) {
		this.shiftedNode = shiftedNode;
		this.months = months;
	}

	public MarkedUpNode getChild() {
		return shiftedNode;
	}

	public int getMonths() {
		return months;
	}
}
