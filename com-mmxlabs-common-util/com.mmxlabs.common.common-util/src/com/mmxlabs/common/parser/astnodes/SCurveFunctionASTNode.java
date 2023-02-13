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
import com.mmxlabs.common.parser.series.SCurveFunctionConstructor;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class SCurveFunctionASTNode implements ASTNode {

	public enum ExprSelector {
		LOW, MID, HIGH
	}

	private ASTNode base;
	private final Number firstThreshold;
	private final Number secondThreshold;
	private ASTNode lowerSeries;
	private ASTNode middleSeries;
	private ASTNode higherSeries;
	private final Number a1;
	private final Number b1;
	private final Number a2;
	private final Number b2;
	private final Number a3;
	private final Number b3;

	public SCurveFunctionASTNode(final ASTNode base, final Number lowerThan, final Number higherThan, final Number a1, final Number b1, final Number a2, final Number b2, final Number a3,
			final Number b3) {

		this.base = base;
		this.firstThreshold = lowerThan;
		this.secondThreshold = higherThan;
		this.a1 = a1;
		this.b1 = b1;
		this.a2 = a2;
		this.b2 = b2;
		this.a3 = a3;
		this.b3 = b3;
		this.lowerSeries = makeExpression(base, a1.doubleValue(), b1.doubleValue());
		this.middleSeries = makeExpression(base, a2.doubleValue(), b2.doubleValue());
		this.higherSeries = makeExpression(base, a3.doubleValue(), b3.doubleValue());
	}

	private ASTNode makeExpression(final ASTNode base, final double a, final double b) {
		return new OperatorASTNode(new OperatorASTNode(base, Operator.TIMES, new ConstantASTNode(a)), Operator.PLUS, new ConstantASTNode(b));
	}

	public ASTNode getBase() {
		return base;
	}

	public double getFirstThreshold() {
		return firstThreshold.doubleValue();
	}

	public double getSecondThreshold() {
		return secondThreshold.doubleValue();
	}

	public ASTNode getLowerSeries() {
		return lowerSeries;
	}

	public ASTNode getMiddleSeries() {
		return middleSeries;
	}

	public ASTNode getHigherSeries() {
		return higherSeries;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Lists.newArrayList(lowerSeries, middleSeries, higherSeries);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (base == original) {
			base = replacement;
		} else if (lowerSeries == original) {
			lowerSeries = replacement;
		} else if (middleSeries == original) {
			middleSeries = replacement;
		} else if (higherSeries == original) {
			higherSeries = replacement;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public @NonNull String asString() {
		return String.format("S(%s, %s, %s, %s, %s, %s, %s, %s, %s)", base.asString(), firstThreshold, secondThreshold, a1, b1, a2, b2, a3, b3);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {

		return new SCurveFunctionConstructor(base.asExpression(seriesParser), //
				firstThreshold.doubleValue(), secondThreshold.doubleValue(), //
				lowerSeries.asExpression(seriesParser), middleSeries.asExpression(seriesParser), higherSeries.asExpression(seriesParser));
	}

	public static ExprSelector select(final double baseValue, final double lowCheck, final double midCheck) {

		if (baseValue < lowCheck) {
			return ExprSelector.LOW;
		} else if (baseValue <= midCheck) {
			return ExprSelector.MID;
		} else {
			return ExprSelector.HIGH;
		}
	}
}
