/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.ISeriesContainer;
import com.mmxlabs.common.parser.series.NamedSeriesExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesType;

@NonNullByDefault
public final class CommoditySeriesASTNode extends NamedSeriesASTNode {

	public CommoditySeriesASTNode(String name) {
		super(name);
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {
		ISeriesContainer seriesContainer = seriesParser.getSeries(getName());
		assert seriesContainer.getType() == SeriesType.COMMODITY;
		return new NamedSeriesExpression(seriesContainer, getName());
	}
}
