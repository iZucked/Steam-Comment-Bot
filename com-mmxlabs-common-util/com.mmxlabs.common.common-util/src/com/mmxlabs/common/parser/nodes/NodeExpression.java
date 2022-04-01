/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

/**
 * IExpression class for parser to produce raw tree objects
 */
public final class NodeExpression implements IExpression<Node> {
	private final @NonNull Node node;

	public NodeExpression(final @NonNull Node node) {
		this.node = node;
	}

	public NodeExpression(final @NonNull String token, final @NonNull Node[] children) {
		this.node = new Node(token, children);
	}

	@Override
	public @NonNull Node evaluate() {
		return node;
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}
}