/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class ShiftNode extends AbstractMarkedUpNode {
	private final MarkedUpNode shiftedNode;
	private final int months;

	public ShiftNode(final MarkedUpNode shiftedNode, final int months) {
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
