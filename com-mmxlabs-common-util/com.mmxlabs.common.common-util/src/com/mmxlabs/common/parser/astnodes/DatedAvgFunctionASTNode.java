package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.DatedAvgFunctionConstructor;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class DatedAvgFunctionASTNode implements ASTNode {

	private ASTNode series;

	private final int months;
	private final int lag;
	private final int reset;

	public DatedAvgFunctionASTNode(final ASTNode series, final Integer months, final Integer lag, final Integer reset) {
		this.series = series;
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
		return String.format("DATEDAVG(%s, %d, %d, %d)", series.asString(), months, lag, reset);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		final IExpression<ISeries> childExpression = series.asExpression(seriesParser);
		return new DatedAvgFunctionConstructor(seriesParser.getSeriesParserData(), childExpression, months, lag, reset);
	}
}
