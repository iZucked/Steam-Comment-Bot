/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.VolumeTierM3FunctionConstructor;
import com.mmxlabs.common.parser.series.VolumeTierMMBTUFunctionConstructor;

@NonNullByDefault
public class VolumeTierASTNode implements ASTNode {

	private ASTNode tier1Series;
	private ASTNode tier2Series;
	private final double threshold;
	private final boolean isM3Volume;
	private final ComparisonOperators op;

	public VolumeTierASTNode(final boolean isM3Volume, final ASTNode tier1Series, final ComparisonOperators op, final Number threshold, final ASTNode tier2Series) {

		this.isM3Volume = isM3Volume;
		this.tier1Series = tier1Series;
		this.tier2Series = tier2Series;
		this.op = op;
		this.threshold = threshold.doubleValue();
	}

	@Override
	public Iterable<ASTNode> getChildren() {
		return Lists.newArrayList(tier1Series, tier2Series);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (original == tier1Series) {
			tier1Series = replacement;
		}
		if (original == tier2Series) {
			tier2Series = replacement;
		}
	}

	public double getThreshold() {
		return threshold;
	}

	public boolean isM3Volume() {
		return isM3Volume;
	}

	public ASTNode getLowTier() {
		return tier1Series;
	}

	public ASTNode getHighTier() {
		return tier2Series;
	}

	@Override
	public String asString() {
		String function;
		if (isM3Volume) {
			function = "VOLUMETIERM3";
		} else {
			function = "VOLUMETIERMMBTU";
		}
		return String.format("%s(%s, %s %s, %s)", function, tier1Series.asString(), op.toString(), threshold, tier2Series.asString());
	}

	@Override
	public IExpression<ISeries> asExpression(final SeriesParser seriesParser) {
		if (isM3Volume) {
			return new VolumeTierM3FunctionConstructor(seriesParser.getSeriesParserData(), tier1Series.asExpression(seriesParser), op, threshold, tier2Series.asExpression(seriesParser));
		} else {
			return new VolumeTierMMBTUFunctionConstructor(seriesParser.getSeriesParserData(), tier1Series.asExpression(seriesParser), op, threshold, tier2Series.asExpression(seriesParser));
		}
	}

	public enum ExprSelector {
		LOW, BLEND
	}

	public static ExprSelector select(final double baseValue, final ComparisonOperators op, final double lowCheck) {

		if ((op == ComparisonOperators.LT && baseValue < lowCheck) //
				|| (op == ComparisonOperators.LTE && baseValue <= lowCheck)) {
			return ExprSelector.LOW;
		} else {
			return ExprSelector.BLEND;
		}
	}
}
