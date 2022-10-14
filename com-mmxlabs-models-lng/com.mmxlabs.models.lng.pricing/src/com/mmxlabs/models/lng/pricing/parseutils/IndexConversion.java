/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

/**
 * Utility class holding methods used to convert a breakeven price from e.g.
 * $9.8 to 115%HH + 6.8
 * 
 * @author achurchill, refactor by FM
 *
 */
public class IndexConversion extends ExposuresIndexConversion {

	public static double getRearrangedPrice(@NonNull PricingModel pricingModel, @NonNull String expression, double breakevenPrice, YearMonth date) {
		double price = 0.0;
		ASTNode markedUpNode = getMarkedUpNode(pricingModel, expression);
		Form form = com.mmxlabs.common.parser.ExposuresIndexConversion.getForm(markedUpNode);
		if (form == null) {
			return price;
		}
		@Nullable
		ASTNode rearrangeGraph = com.mmxlabs.common.parser.ExposuresIndexConversion.rearrangeGraph(breakevenPrice, markedUpNode, form);
		if (rearrangeGraph == null) {
			return price;
		}
		String rearrangedExpression = com.mmxlabs.common.parser.ExposuresIndexConversion.getExpression(rearrangeGraph);
		if (rearrangedExpression == null) {
			return price;
		}
		price = parseExpression(pricingModel, rearrangedExpression, date);
		return price;
	}

	private static double parseExpression(@NonNull PricingModel pricingModel, @NonNull String expression, YearMonth date) {
		final @NonNull LookupData lookupData = LookupData.createLookupData(pricingModel);

		final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.asIExpression(expression);
		double unitPrice = 0.0;
		if (series instanceof SeriesOperatorExpression opExpr) {
			final @NonNull ISeries opSeries = opExpr.evaluate();
			final Number evaluate = opSeries.evaluate(Hours.between(PriceIndexUtils.dateZero, date), Collections.emptyMap());
			unitPrice = evaluate.doubleValue();
		}
		return unitPrice;
	}

	public static ASTNode getMarkedUpNode(@NonNull PricingModel pricingModel, @NonNull String expression) {

		final @NonNull LookupData lookupData = LookupData.createLookupData(pricingModel);
		// Parse the expression

		final SeriesParser commodityIndices = makeCommodityParser(lookupData);
		return commodityIndices.parse(expression);
	}

	public static boolean isExpressionValidForIndexConversion(@NonNull PricingModel pricingModel, @NonNull String expression) {
		try {
			ASTNode markedUpNode = getMarkedUpNode(pricingModel, expression);
			return com.mmxlabs.common.parser.ExposuresIndexConversion.getForm(markedUpNode) != null;
		} catch (Exception e) {
			return false;
		}
	}

	private static SeriesParser makeCommodityParser(final LookupData lookupData) {
		return PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
	}

}
