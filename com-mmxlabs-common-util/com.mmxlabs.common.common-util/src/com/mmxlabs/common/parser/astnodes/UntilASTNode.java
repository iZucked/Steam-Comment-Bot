/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.UntilFunctionConstructor;

@NonNullByDefault
public final class UntilASTNode implements ASTNode {

	private final ASTNode lhs;
	private final LocalDate date;
	private final String dateStr;
	private final ASTNode rhs;

	public UntilASTNode(final ASTNode lhs, final String dateStr, final ASTNode rhs) {
		this.lhs = lhs;
		this.dateStr = dateStr;
		this.date = LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-M-d"));
		this.rhs = rhs;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Lists.newArrayList(lhs, rhs);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		throw new IllegalStateException();
	}

	@Override
	public String asString() {
		return String.format("UNTIL(%s, %s, %s)", lhs.asString(), dateStr, rhs.asString());
	}

	@Override
	public IExpression<ISeries> asExpression(final SeriesParser seriesParser) {
		return new UntilFunctionConstructor(seriesParser.getSeriesParserData(), lhs.asExpression(seriesParser), date.atStartOfDay(), rhs.asExpression(seriesParser));
	}

	public ChronoLocalDate getDate() {
		return date;
	}

	public ASTNode getLHS() {
		return lhs;
	}

	public ASTNode getRHS() {
		return rhs;
	}
}
