/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class OperatorNode extends AbstractMarkedUpNode {
	private List<MarkedUpNode> children = new ArrayList<>(2);
	private String operator;

	public OperatorNode(String operator) {
		this.operator = operator;
	}

	@Override
	public List<MarkedUpNode> getChildren() {
		return children;
	}

	public String getOperator() {
		return operator;
	}
}
