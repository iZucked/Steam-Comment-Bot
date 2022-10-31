package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.ISeriesContainer;
import com.mmxlabs.common.parser.series.NamedSeriesExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesType;

public final class CharterSeriesASTNode extends NamedSeriesASTNode {

	public CharterSeriesASTNode(String name) {
		super(name);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {
		ISeriesContainer seriesContainer = seriesParser.getSeries(getName());
		assert seriesContainer.getType() == SeriesType.CHARTER;
		return new NamedSeriesExpression(seriesContainer, getName());
	}
}
