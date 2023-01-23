/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.Tier2FunctionConstructor;

public class Tier2FunctionASTNode implements ASTNode {
	public enum ExprSelector {
		LOW, HIGH
	}

	private ASTNode target;
	private ComparisonOperators lowOp;
	private Number low;
	private ASTNode lowValue;
	private ASTNode highValue;

	public Tier2FunctionASTNode(ASTNode target, ComparisonOperators lowOp, Number low, ASTNode lowValue, ASTNode highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	@Override
	public @NonNull Iterable<@NonNull ASTNode> getChildren() {
		return Lists.newArrayList(target, lowValue, highValue);

	}

	@Override
	public void replace(@NonNull ASTNode original, @NonNull ASTNode replacement) {
		if (original == target) {
			target = replacement;
		} else if (original == lowValue) {
			lowValue = replacement;
		} else if (original == highValue) {
			highValue = replacement;
		} else {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public @NonNull String asString() {
		return String.format("TIER(%s, %s %s,  %s, %s)", target.asString(), lowOp.asString(), low, lowValue.asString(), highValue.asString());
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {

		return new Tier2FunctionConstructor(target.asExpression(seriesParser), //
				lowOp, low, lowValue.asExpression(seriesParser), //
				highValue.asExpression(seriesParser));
	}

	public ExprSelector select(double baseValue, double lowCheck) {
		return select(baseValue, lowOp, lowCheck);
	}

	public static ExprSelector select(double baseValue, ComparisonOperators lowOp, double lowCheck) {

		if ((lowOp == ComparisonOperators.LT && baseValue < lowCheck) //
				|| (lowOp == ComparisonOperators.LTE && baseValue <= lowCheck)) {
			return ExprSelector.LOW;
		} else {
			return ExprSelector.HIGH;
		}
	}

	public ASTNode getTarget() {
		return target;
	}

	public ComparisonOperators getLowOp() {
		return lowOp;
	}

	public Number getLow() {
		return low;
	}

	public ASTNode getLowValue() {
		return lowValue;
	}

	public ASTNode getHighValue() {
		return highValue;
	}

}
