/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesOperatorExpression;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class OperatorASTNode implements ASTNode {

	private ASTNode lhs;
	private ASTNode rhs;
	private final Operator operator;

	public OperatorASTNode(final ASTNode lhs, final Operator operator, final ASTNode rhs) {
		this.lhs = lhs;
		this.operator = operator;
		this.rhs = rhs;
	}

	public ASTNode getLHS() {
		return lhs;
	}

	public ASTNode getRHS() {
		return rhs;
	}

	public Operator getOperator() {
		return operator;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Lists.newArrayList(lhs, rhs);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (lhs == original) {
			lhs = replacement;
		} else if (rhs == original) {
			rhs = replacement;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public @NonNull String asString() {
		return String.format("%s%s%s", wrap(lhs), operator.asString(), wrap(rhs));
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		return new SeriesOperatorExpression(operator.asChar(), lhs.asExpression(seriesParser), rhs.asExpression(seriesParser));
	}

	private String wrap(ASTNode node) {
		if (node instanceof OperatorASTNode) {
			return String.format("(%s)", node.asString());
		}
		return node.asString();
	}
}
