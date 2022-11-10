/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SplitMonthFunctionConstructor;

public final class SplitMonthFunctionASTNode implements ASTNode {

	private ASTNode series1;
	private ASTNode series2;
	private final int splitPoint;

	public SplitMonthFunctionASTNode(final ASTNode series1, final ASTNode series2, final Integer splitPoint) {

		this.series1 = series1;
		this.series2 = series2;
		this.splitPoint = splitPoint.intValue();
	}

	public int getSplitPoint() {
		return splitPoint;
	}

	public ASTNode getSeries1() {
		return series1;
	}

	public ASTNode getSeries2() {
		return series2;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Lists.newArrayList(series1, series2);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (series1 == original) {
			series1 = replacement;
		} else if (series2 == original) {
			series2 = replacement;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String asString() {
		return String.format("SPLITMONTH(%s, %s, %d)", series1.asString(), series2.asString(), splitPoint);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		return new SplitMonthFunctionConstructor(seriesParser.getSeriesParserData(), series1.asExpression(seriesParser), series2.asExpression(seriesParser), splitPoint);
	}
}
