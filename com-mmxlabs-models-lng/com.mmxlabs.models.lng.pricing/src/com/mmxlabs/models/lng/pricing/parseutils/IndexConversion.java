/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.time.YearMonth;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.ExposuresIndexConversion;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesOperatorExpression;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

/**
 * Utility class holding methods used to convert a breakeven price from e.g. $9.8 to 115%HH + 6.8
 * 
 * @author achurchill, refactor by FM
 *
 */
public class IndexConversion extends ExposuresIndexConversion {

	public static double getRearrangedPrice(@NonNull final PricingModel pricingModel, @NonNull final String expression, final double breakevenPrice, final YearMonth date) {
		double price = 0.0;
		final @Nullable ASTNode markedUpNode = getMarkedUpNode(pricingModel, expression);
		final @Nullable Form form = com.mmxlabs.common.parser.ExposuresIndexConversion.getForm(markedUpNode);
		if (form == null) {
			return price;
		}
		final @Nullable ASTNode rearrangeGraph = com.mmxlabs.common.parser.ExposuresIndexConversion.rearrangeGraph(breakevenPrice, markedUpNode, form);
		if (rearrangeGraph == null) {
			return price;
		}
		final String rearrangedExpression = com.mmxlabs.common.parser.ExposuresIndexConversion.getExpression(rearrangeGraph);
		if (rearrangedExpression == null) {
			return price;
		}
		price = parseExpression(pricingModel, rearrangedExpression, date);
		return price;
	}

	private static double parseExpression(@NonNull final PricingModel pricingModel, @NonNull final String expression, final YearMonth date) {

		final @Nullable IExpression<ISeries> series = getExpression(pricingModel, expression);
		if (series instanceof final SeriesOperatorExpression opExpr) {
			final @NonNull ISeries opSeries = opExpr.evaluate();
			final Number evaluate = opSeries.evaluate(Hours.between(PriceIndexUtils.dateZero, date), Collections.emptyMap());
			return evaluate.doubleValue();
		}
		return 0.0;
	}

	public static ASTNode getMarkedUpNode(@NonNull final PricingModel pricingModel, @NonNull final String expression) {
		final ModelMarketCurveProvider p = ModelMarketCurveProvider.getOrCreate(pricingModel);
		return p.getPricingDataCache().getASTNodeFor(expression, PriceIndexType.COMMODITY);
	}

	public static IExpression<ISeries> getExpression(@NonNull final PricingModel pricingModel, @NonNull final String expression) {
		final ModelMarketCurveProvider p = ModelMarketCurveProvider.getOrCreate(pricingModel);
		return p.getPricingDataCache().getIExpressionFor(expression, PriceIndexType.COMMODITY);
	}

	public static boolean isExpressionValidForIndexConversion(@NonNull final PricingModel pricingModel, @NonNull final String expression) {
		try {
			final ASTNode markedUpNode = getMarkedUpNode(pricingModel, expression);
			return com.mmxlabs.common.parser.ExposuresIndexConversion.getForm(markedUpNode) != null;
		} catch (final Exception e) {
			return false;
		}
	}
}
