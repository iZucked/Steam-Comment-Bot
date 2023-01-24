/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.ISeriesContainer;
import com.mmxlabs.common.parser.series.NamedSeriesExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesType;

public final class PricingBasisSeriesASTNode extends NamedSeriesASTNode {

	public PricingBasisSeriesASTNode(String name) {
		super(name);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {
		ISeriesContainer seriesContainer = seriesParser.getSeries(getName());
		assert seriesContainer.getType() == SeriesType.PRICING_BASIS;
		return new NamedSeriesExpression(seriesContainer, getName());
	}
}
