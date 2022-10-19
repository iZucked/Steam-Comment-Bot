package com.mmxlabs.common.parser.astnodes;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SCurveFunctionConstructor;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class SCurveFunctionASTNode implements ASTNode {
	private ASTNode base;
	private final double firstThreshold;
	private final double secondThreshold;
	private ASTNode lowerSeries;
	private ASTNode middleSeries;
	private ASTNode higherSeries;
	private double a1;
	private double b1;
	private double a2;
	private double b2;
	private double a3;
	private double b3;

	public SCurveFunctionASTNode(final ASTNode base, final double lowerThan, final double higherThan, final double a1, final double b1, final double a2, final double b2, final double a3,
			final double b3) {

		this.base = base;
		this.firstThreshold = lowerThan;
		this.secondThreshold = higherThan;
		this.a1 = a1;
		this.b1 = b1;
		this.a2 = a2;
		this.b2 = b2;
		this.a3 = a3;
		this.b3 = b3;
		this.lowerSeries = makeExpression(base, a1, b1);
		this.middleSeries = makeExpression(base, a2, b2);
		this.higherSeries = makeExpression(base, a3, b3);
	}

	private ASTNode makeExpression(ASTNode base, double a, double b) {
		return new OperatorASTNode(new OperatorASTNode(base, new ConstantASTNode(a), Operator.TIMES), new ConstantASTNode(b), Operator.PLUS);
	}

	public ASTNode getBase() {
		return base;
	}

	public double getFirstThreshold() {
		return firstThreshold;
	}

	public double getSecondThreshold() {
		return secondThreshold;
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
	public void replace(ASTNode original, ASTNode replacement) {
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
		return String.format("S(%s, %s,%s, %f, %f, %f, %f, %f, %f)", base.asString(), firstThreshold, secondThreshold, a1, b1, a2, b2, a3, b3);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {

		return new SCurveFunctionConstructor(base.asExpression(seriesParser), //
				firstThreshold, secondThreshold, //
				lowerSeries.asExpression(seriesParser), middleSeries.asExpression(seriesParser), higherSeries.asExpression(seriesParser));
	}
}
