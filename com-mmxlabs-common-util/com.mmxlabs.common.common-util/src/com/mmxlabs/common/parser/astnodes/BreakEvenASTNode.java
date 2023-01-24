/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.BreakevenSeriesExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class BreakEvenASTNode implements ASTNode {

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
		return "?";
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(SeriesParser seriesParser) {
		return new BreakevenSeriesExpression();
	}
}
