/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class MinFunctionNode extends AbstractMarkedUpNode {
	private List<MarkedUpNode> children = new ArrayList<>(2);

	@Override
	public List<MarkedUpNode> getChildren() {
		return children;
	}
}
