/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;

/**
 * Inputs to the exposures calculators. Some data is shared (e.g. SeriesParser),
 * other fields can be modified to pass into nested parts of the expression (i.e
 * date and volume)
 *
 */
public record InputRecord(LocalDate date, long volumeInMMBTU, int cargoCV, ExposuresLookupData lookupData, IExternalDateProvider externalDateProvider, SeriesParser commodityIndices,
		SeriesParser currencyIndices) {

	public InputRecord withDate(final LocalDate d) {
		return new InputRecord(d, volumeInMMBTU(), cargoCV(), lookupData(), externalDateProvider(), commodityIndices(), currencyIndices());
	}

	public InputRecord withVolume(final long v) {
		return new InputRecord(date(), v, cargoCV(), lookupData(), externalDateProvider(), commodityIndices(), currencyIndices());
	}

	/**
	 * Determines the amount of exposure to a particular index which is created by a
	 * specific contract.
	 * 
	 * @param priceExpression
	 * @param lookupData
	 * @return
	 */
	public @Nullable ASTNode getExposureCoefficient(final String priceExpression) {
		if (priceExpression != null && !priceExpression.isEmpty()) {
			if (!priceExpression.equals("?")) {

				if (lookupData.expressionToNode.containsKey(priceExpression)) {
					return lookupData.expressionToNode.get(priceExpression);
				}

				// Parse the expression
				final ASTNode expressionAST = commodityIndices.parse(priceExpression);
				lookupData.expressionToNode.put(priceExpression, expressionAST);
				return expressionAST;
			}
		}
		return null;
	}
}