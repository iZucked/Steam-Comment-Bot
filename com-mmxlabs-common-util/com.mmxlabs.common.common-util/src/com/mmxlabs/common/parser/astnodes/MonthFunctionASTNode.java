/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.MonthFunctionConstructor;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Months;

public final class MonthFunctionASTNode implements ASTNode {

	private ASTNode series;
	private final Month month;

	public MonthFunctionASTNode(final ASTNode series, final Month month) {
		this.series = series;
		this.month = month;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.singletonList(series);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (series != original) {
			throw new IllegalArgumentException();
		}
		series = replacement;
	}

	@Override
	public String asString() {
		final String monthStr = switch (month) {
		case JANUARY -> "JAN";
		case FEBRUARY -> "FEB";
		case MARCH -> "MAR";
		case APRIL -> "APR";
		case MAY -> "MAY";
		case JUNE -> "JUN";
		case JULY -> "JUL";
		case AUGUST -> "AUG";
		case SEPTEMBER -> "SEP";
		case OCTOBER -> "OCT";
		case NOVEMBER -> "NOV";
		case DECEMBER -> "DEC";
		};
		return String.format("%s[%s]", series.asString(), monthStr);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		final IExpression<ISeries> childExpression = series.asExpression(seriesParser);
		return new MonthFunctionConstructor(seriesParser.getSeriesParserData(), childExpression, month);
	}

	public Month getMonth() {
		return month;
	}

	public @NonNull ASTNode getSeries() {
		return series;
	}

	public LocalDateTime mapTime(final LocalDateTime currentTime) {
		return mapTime(currentTime, month);
	}

	public static LocalDateTime mapTime(final LocalDateTime currentTime, final Month month) {

		final YearMonth current = YearMonth.from(currentTime);

		YearMonth prev;
		YearMonth next;

		// If our month is already ahead, then roll forward a year
		if (currentTime.getMonth().getValue() >= month.getValue()) {
			prev = current.withMonth(month.getValue());
			next = prev.plusYears(1);
		} else {
			next = current.withMonth(month.getValue());
			prev = next.minusYears(1);
		}

		LocalDateTime newValue;

		final int a = Months.between(prev, current);
		final int b = Months.between(current, next);
		if (a > b) {
			newValue = currentTime.withYear(next.getYear()).withMonth(next.getMonthValue()).withDayOfMonth(1);
		} else {
			newValue = currentTime.withYear(prev.getYear()).withMonth(prev.getMonthValue()).withDayOfMonth(1);
		}

		return newValue;
	}
}
