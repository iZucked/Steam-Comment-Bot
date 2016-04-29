/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parser;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

/**
 * IExpression class for parser to produce raw tree objects
 */
public final class NodeExpression implements IExpression<Node> {
	final @NonNull Node node;

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
}