/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.DatedAvgFunctionConstructor;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class DatedAvgFunctionASTNode implements ASTNode {

	public enum InputMode {
		FUNCTION, NOTATION_WITH_COMMA, NOTATION_WITHOUT_COMMA
	}

	private ASTNode series;

	private final int months;
	private final int lag;
	private final int reset;

	private final InputMode inputMode;

	public DatedAvgFunctionASTNode(final ASTNode series, final Integer combined, final InputMode inputMode) {
		// Check range
		if (combined.intValue() < 100 || combined.intValue() >= 1000) {
			throw new IllegalArgumentException();
		}

		this.series = series;
		this.inputMode = inputMode;

		int[] array = combined.toString().chars().map(x -> x - '0').toArray();

		this.months = array[0];
		this.lag = array[1];
		this.reset = array[2];
	}

	public DatedAvgFunctionASTNode(final ASTNode series, final Integer months, final Integer lag, final Integer reset, final InputMode inputMode) {
		this.series = series;
		this.inputMode = inputMode;
		this.months = months.intValue();
		this.lag = lag.intValue();
		this.reset = reset.intValue();
	}

	public int getMonths() {
		return months;
	}

	public int getLag() {
		return lag;
	}

	public int getReset() {
		return reset;
	}

	public ASTNode getSeries() {
		return series;
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
		return switch (inputMode) {
		case FUNCTION -> String.format("DATEDAVG(%s, %d, %d, %d)", series.asString(), months, lag, reset);
		case NOTATION_WITH_COMMA -> String.format("%s[%d,%d,%d]", series.asString(), months, lag, reset);
		case NOTATION_WITHOUT_COMMA -> String.format("%s[%d%d%d]", series.asString(), months, lag, reset);
		};
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		final IExpression<ISeries> childExpression = series.asExpression(seriesParser);
		return new DatedAvgFunctionConstructor(seriesParser.getSeriesParserData(), childExpression, months, lag, reset);
	}

	public LocalDateTime mapTimeToStartDate(final LocalDateTime currentTime) {
		return mapTimeToStartDate(currentTime, months, lag, reset);
	}

	public static LocalDateTime mapTimeToStartDate(final LocalDateTime date, final int months, final int lag, final int reset) {
		LocalDateTime startDate = date.minusMonths(months);

		if (reset != 1) {
			startDate = startDate.minusMonths((date.getMonthValue() - 1) % reset);
		}

		startDate = startDate.minusMonths(lag);

		return startDate;
	}

}
