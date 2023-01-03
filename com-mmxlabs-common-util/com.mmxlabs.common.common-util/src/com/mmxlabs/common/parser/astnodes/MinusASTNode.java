/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.functions.Minus;

public final class MinusASTNode implements ASTNode {

	private ASTNode expression;

	public ASTNode getExpression() {
		return expression;
	}

	public MinusASTNode(final ASTNode expression) {
		this.expression = expression;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.singletonList(expression);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (expression != original) {
			throw new IllegalArgumentException();
		}
		expression = replacement;
	}

	@Override
	public @NonNull String asString() {
		if (expression instanceof OperatorASTNode) {
			return String.format("-(%s)", expression.asString());
		}
		return String.format("-%s", expression.asString());
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		final IExpression<@NonNull ISeries> series = getExpression().asExpression(seriesParser);
		return new IExpression<ISeries>() {
			@Override
			public ISeries evaluate() {
				return new Minus(series.evaluate());
			}

			@Override
			public boolean canEvaluate() {
				return series.canEvaluate();
			}
		};
	}
}
