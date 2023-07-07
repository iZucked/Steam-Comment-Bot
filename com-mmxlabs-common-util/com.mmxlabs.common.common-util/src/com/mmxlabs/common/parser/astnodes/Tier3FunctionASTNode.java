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
import com.mmxlabs.common.parser.series.Tier3FunctionConstructor;

public class Tier3FunctionASTNode implements ASTNode {
	public enum ExprSelector {
		LOW, MID, HIGH
	}

	private ASTNode target;
	private ComparisonOperators lowOp;
	private Number low;
	private ASTNode lowValue;
	private ComparisonOperators midOp;
	private Number mid;
	private ASTNode midValue;
	private ASTNode highValue;

	public Tier3FunctionASTNode(ASTNode target, ComparisonOperators lowOp, Number low, ASTNode lowValue, ComparisonOperators midOp, Number mid, ASTNode midValue, ASTNode highValue) {
		this.target = target;
		this.lowOp = lowOp;
		this.low = low;
		this.lowValue = lowValue;
		this.midOp = midOp;
		this.mid = mid;
		this.midValue = midValue;
		this.highValue = highValue;
	}

	@Override
	public @NonNull Iterable<@NonNull ASTNode> getChildren() {
		return Lists.newArrayList(target, lowValue, midValue, highValue);

	}

	@Override
	public void replace(@NonNull ASTNode original, @NonNull ASTNode replacement) {
		if (original == target) {
			target = replacement;
		} else if (original == lowValue) {
			lowValue = replacement;
		} else if (original == midValue) {
			midValue = replacement;
		} else if (original == highValue) {
			highValue = replacement;
		} else {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public @NonNull String asString() {
		return String.format("TIER(%s, %s, %s %s, %s, %s %s, %s)", target.asString(), lowValue.asString(), lowOp.asString(), low, midValue.asString(), midOp.asString(), mid, highValue.asString());
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {

		return new Tier3FunctionConstructor(target.asExpression(seriesParser), //
				lowOp, low, lowValue.asExpression(seriesParser), //
				midOp, mid, midValue.asExpression(seriesParser), //
				highValue.asExpression(seriesParser));
	}

	public ExprSelector select(double baseValue, double lowCheck, double midCheck) {
		return select(baseValue, lowOp, lowCheck, midOp, midCheck);
	}

	public static ExprSelector select(double baseValue, ComparisonOperators lowOp, double lowCheck, ComparisonOperators midOp, double midCheck) {

		if ((lowOp == ComparisonOperators.LT && baseValue < lowCheck) //
				|| (lowOp == ComparisonOperators.LTE && baseValue <= lowCheck)) {
			return ExprSelector.LOW;
		} else if ((midOp == ComparisonOperators.LT && baseValue < midCheck) //
				|| (midOp == ComparisonOperators.LTE && baseValue <= midCheck)) {
			return ExprSelector.MID;
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

	public ComparisonOperators getMidOp() {
		return midOp;
	}

	public Number getMid() {
		return mid;
	}

	public ASTNode getMidValue() {
		return midValue;
	}

	public ASTNode getHighValue() {
		return highValue;
	}

}
