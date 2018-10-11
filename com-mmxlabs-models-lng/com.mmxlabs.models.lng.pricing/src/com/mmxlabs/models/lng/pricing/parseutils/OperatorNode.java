/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class OperatorNode extends AbstractMarkedUpNode {
	List<MarkedUpNode> children = new ArrayList<>(2);
	private String operator;

	public OperatorNode(String operator) {
		this.operator = operator;

	}

	public List<MarkedUpNode> getChildren() {
		return children;
	}

	public String getOperator() {
		return operator;
	}
}
