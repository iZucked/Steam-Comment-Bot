/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public abstract class NamedSeriesASTNode implements ASTNode {

	private final String name;

	public String getName() {
		return name;
	}

	protected NamedSeriesASTNode(String name) {
		this.name = name;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public void replace(ASTNode original, ASTNode replacement) {
		throw new IllegalStateException();
	}

	@Override
	public @NonNull String asString() {
		return name;
	}
}
