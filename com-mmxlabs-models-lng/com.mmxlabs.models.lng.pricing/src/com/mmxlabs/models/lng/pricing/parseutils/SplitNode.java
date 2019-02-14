/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class SplitNode extends AbstractMarkedUpNode {
	List<MarkedUpNode> children = new ArrayList<>(2);
	private int splitPointInDays;

	public SplitNode(int splitPointInDays) {
		this.splitPointInDays = splitPointInDays;
	}

	public List<MarkedUpNode> getChildren() {
		return children;
	}

	public int getSplitPointInDays() {
		return splitPointInDays;
	}
}
