/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DatedAverageNode extends AbstractMarkedUpNode {
	private MarkedUpNode shiftedNode;
	private int months;
	private int lag;
	private int reset;

	public DatedAverageNode(MarkedUpNode shiftedNode, int months, int lag, int reset) {
		this.shiftedNode = shiftedNode;
		this.months = months;
		this.lag = lag;
		this.reset = reset;
	}

	public int getLag() {
		return lag;
	}

	public int getReset() {
		return reset;
	}

	public MarkedUpNode getChild() {
		return shiftedNode;
	}

	public int getMonths() {
		return months;
	}
}
