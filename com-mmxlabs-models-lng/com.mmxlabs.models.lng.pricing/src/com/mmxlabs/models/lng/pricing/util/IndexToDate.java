/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.BunkersSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CharterSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CurrencySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.PricingBasisSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class IndexToDate {

	private static final BinaryOperator<LocalDate> mergeFunc = (a, b) -> a.isBefore(b) ? a : b;

	public static @NonNull Map<AbstractYearMonthCurve, LocalDate> calculateIndicesToFirstDate(final @NonNull String priceExpression, final LocalDate date, final @NonNull LookupData lookupData) {

		// Parse the expression
		final ASTNode markedUpNode = lookupData.expressionToNode.computeIfAbsent(priceExpression, expr -> {
			final SeriesParser commodityIndices = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
			return commodityIndices.parse(expr);
		});

		return getIndexToFirstDateNode(markedUpNode, date, lookupData);
	}

	private static @NonNull Map<AbstractYearMonthCurve, LocalDate> getIndexToFirstDateNode(final @NonNull ASTNode node, final LocalDate date, final LookupData lookupData) {

		if (node instanceof final ShiftFunctionASTNode shiftNode) {
			final LocalDate pricingDate = shiftNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(shiftNode.getToShift(), pricingDate, lookupData);
		} else if (node instanceof final MonthFunctionASTNode monthNode) {
			final LocalDate pricingDate = monthNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(monthNode.getSeries(), pricingDate, lookupData);
		} else if (node instanceof final DatedAvgFunctionASTNode averageNode) {
			final LocalDate startDate = averageNode.mapTimeToStartDate(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(averageNode.getSeries(), startDate, lookupData);
		} else if (node instanceof final CommoditySeriesASTNode commodityNode) {
			return make(lookupData.commodityMap.get(commodityNode.getName().toLowerCase()), date);
		} else if (node instanceof final CurrencySeriesASTNode currencyNode) {
			return make(lookupData.currencyMap.get(currencyNode.getName().toLowerCase()), date);
		} else if (node instanceof final CharterSeriesASTNode charterNode) {
			return make(lookupData.charterMap.get(charterNode.getName().toLowerCase()), date);
		} else if (node instanceof final BunkersSeriesASTNode bunkersNode) {
			return make(lookupData.baseFuelMap.get(bunkersNode.getName().toLowerCase()), date);
		} else if (node instanceof final PricingBasisSeriesASTNode basisNode) {
			return make(lookupData.pricingBases.get(basisNode.getName().toLowerCase()), date);
		} else {
			return mergeIterables(date, lookupData, node.getChildren());
		}
	}

	private static @NonNull Map<AbstractYearMonthCurve, LocalDate> mergeIterables(final LocalDate date, final LookupData lookupData, Iterable<ASTNode> iterables) {

		Map<AbstractYearMonthCurve, LocalDate> m = new HashMap<>();
		for (var c : iterables) {
			for (var e : getIndexToFirstDateNode(c, date, lookupData).entrySet()) {
				m.merge(e.getKey(), e.getValue(), mergeFunc);
			}
		}
		return m;
	}

	private static @NonNull Map<AbstractYearMonthCurve, LocalDate> make(AbstractYearMonthCurve c, LocalDate d) {
		Map<AbstractYearMonthCurve, LocalDate> m = new HashMap<>();
		m.put(c, d);
		return m;
	}

}
