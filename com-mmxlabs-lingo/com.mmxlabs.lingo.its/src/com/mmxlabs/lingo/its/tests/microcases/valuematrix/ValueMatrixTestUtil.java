/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.valuematrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Assertions;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lingo.its.tests.microcases.valuematrix.ValueMatrixTests.IndexStateContainer;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult;
import com.mmxlabs.models.lng.analytics.ui.views.valuematrix.SwapValueChangeKey;
import com.mmxlabs.models.lng.analytics.ui.views.valuematrix.ValueMatrixUtils;

@NonNullByDefault
public class ValueMatrixTestUtil {

	private ValueMatrixTestUtil() {
	}

	public static Pair<Integer, Integer> getMatrixValuePair(final SwapValueMatrixResult result) {
		return Pair.of((int) result.getBaseCargo().getDischargePrice(), (int) result.getSwapDiversionCargo().getDischargePrice());
	}

	public static void checkEquality(final SwapValueMatrixResult result1, final SwapValueMatrixResult result2) {
		Assertions.assertEquals(result1.getSwapPnlMinusBasePnl(), result2.getSwapPnlMinusBasePnl());
		checkEquality(result1.getBaseCargo(), result2.getBaseCargo());
		checkEquality(result1.getSwapBackfillCargo(), result2.getSwapBackfillCargo());
		checkEquality(result1.getSwapDiversionCargo(), result2.getSwapDiversionCargo());
		Assertions.assertEquals(result1.getBasePrecedingPnl(), result2.getBasePrecedingPnl());
		Assertions.assertEquals(result1.getBaseSucceedingPnl(), result2.getBaseSucceedingPnl());
		Assertions.assertEquals(result1.getSwapPrecedingPnl(), result2.getSwapPrecedingPnl());
		Assertions.assertEquals(result1.getSwapSucceedingPnl(), result2.getSwapSucceedingPnl());
	}

	public static void checkEquality(final SwapValueMatrixShippedCargoResult result1, final SwapValueMatrixShippedCargoResult result2) {
		Assertions.assertEquals(result1.getAdditionalPnl(), result2.getAdditionalPnl());
		Assertions.assertEquals(result1.getDischargePrice(), result2.getDischargePrice());
		Assertions.assertEquals(result1.getDischargeVolume(), result2.getDischargeVolume());
		Assertions.assertEquals(result1.getLoadPrice(), result2.getLoadPrice());
		Assertions.assertEquals(result1.getLoadVolume(), result2.getLoadVolume());
		Assertions.assertEquals(result1.getPurchaseCost(), result2.getPurchaseCost());
		Assertions.assertEquals(result1.getSalesRevenue(), result2.getSalesRevenue());
		Assertions.assertEquals(result1.getShippingCost(), result2.getShippingCost());
		Assertions.assertEquals(result1.getTotalPnl(), result2.getTotalPnl());
	}

	public static void checkEquality(final SwapValueMatrixNonShippedCargoResult result1, final SwapValueMatrixNonShippedCargoResult result2) {
		Assertions.assertEquals(result1.getAdditionalPnl(), result2.getAdditionalPnl());
		Assertions.assertEquals(result1.getDischargePrice(), result2.getDischargePrice());
		Assertions.assertEquals(result1.getLoadPrice(), result2.getLoadPrice());
		Assertions.assertEquals(result1.getPurchaseCost(), result2.getPurchaseCost());
		Assertions.assertEquals(result1.getSalesRevenue(), result2.getSalesRevenue());
		Assertions.assertEquals(result1.getTotalPnl(), result2.getTotalPnl());
		Assertions.assertEquals(result1.getVolume(), result2.getVolume());
	}

	public static void verifyPermutations(final SwapValueMatrixModel model, BiConsumer<SwapValueMatrixModel, Iterable<Pair<Integer, Integer>>> evaluator) {
		final int numRows = model.getParameters().getSwapPriceRange().getMax() - model.getParameters().getSwapPriceRange().getMin() + 1;
		final int numColumns = model.getParameters().getBasePriceRange().getMax() - model.getParameters().getBasePriceRange().getMin() + 1;

		final SwapValueMatrixResult[][] individualEvaluationMatrix = new SwapValueMatrixResult[numRows][numColumns];
		{
			int j = 0;
			for (int basePrice = model.getParameters().getBasePriceRange().getMin(); basePrice <= model.getParameters().getBasePriceRange().getMax(); basePrice += model.getParameters()
					.getBasePriceRange().getStepSize()) {
				int i = 0;
				for (int swapPrice = model.getParameters().getSwapPriceRange().getMin(); swapPrice <= model.getParameters().getSwapPriceRange().getMax(); swapPrice += model.getParameters()
						.getSwapPriceRange().getStepSize()) {
					final SwapValueMatrixModel tempModel = ValueMatrixUtils.createEmptyValueMatrixModel("individual model");
					tempModel.getParameters().getBaseVesselCharter().setVesselCharter(model.getParameters().getBaseVesselCharter().getVesselCharter());
					tempModel.getParameters().getBaseLoad().setSlot(model.getParameters().getBaseLoad().getSlot());
					tempModel.getParameters().getBaseDischarge().setSlot(model.getParameters().getBaseDischarge().getSlot());
					tempModel.getParameters().getBasePriceRange().setMin(basePrice);
					tempModel.getParameters().getBasePriceRange().setMax(basePrice);
					tempModel.getParameters().getBasePriceRange().setStepSize(model.getParameters().getBasePriceRange().getStepSize());

					tempModel.getParameters().getSwapLoadMarket().setMarket(model.getParameters().getSwapLoadMarket().getMarket());
					tempModel.getParameters().getSwapLoadMarket().setMonth(model.getParameters().getSwapLoadMarket().getMonth());
					tempModel.getParameters().getSwapDischargeMarket().setMarket(model.getParameters().getSwapDischargeMarket().getMarket());
					tempModel.getParameters().getSwapDischargeMarket().setMonth(model.getParameters().getSwapDischargeMarket().getMonth());
					tempModel.getParameters().getSwapPriceRange().setMin(swapPrice);
					tempModel.getParameters().getSwapPriceRange().setMax(swapPrice);
					tempModel.getParameters().getSwapPriceRange().setStepSize(model.getParameters().getSwapPriceRange().getStepSize());
					tempModel.getParameters().setSwapFee(model.getParameters().getSwapFee());

					evaluator.accept(tempModel, Collections.singletonList(Pair.of(basePrice, swapPrice)));

					Assertions.assertEquals(1, tempModel.getSwapValueMatrixResult().getResults().size());
					individualEvaluationMatrix[i][j] = tempModel.getSwapValueMatrixResult().getResults().get(0);
					++i;
				}
				++j;
			}
		}
		final Map<SwapValueChangeKey, List<SwapValueMatrixResult>> groupedResults = new HashMap<>();
		for (final SwapValueMatrixResult[] row : individualEvaluationMatrix) {
			for (final SwapValueMatrixResult result : row) {
				groupedResults.computeIfAbsent(new SwapValueChangeKey(result), key -> new ArrayList<>()).add(result);
			}
		}
		// Expecting at most 6 groups -- larger numbers generate too many permutations
		Assertions.assertTrue(groupedResults.size() <= 6);

		final List<Pair<Integer, Integer>> evaluationPairs = new ArrayList<>();
		{
			for (int basePrice = model.getParameters().getBasePriceRange().getMin(); basePrice <= model.getParameters().getBasePriceRange().getMax(); basePrice += model.getParameters()
					.getBasePriceRange().getStepSize()) {
				for (int swapPrice = model.getParameters().getSwapPriceRange().getMin(); swapPrice <= model.getParameters().getSwapPriceRange().getMax(); swapPrice += model.getParameters()
						.getSwapPriceRange().getStepSize()) {
					evaluationPairs.add(Pair.of(basePrice, swapPrice));
				}
			}
		}

		final List<Pair<Integer, Integer>> pairsForPermutations = new ArrayList<>();
		final List<Pair<Integer, Integer>> extraPairs = new ArrayList<>();
		for (final List<SwapValueMatrixResult> results : groupedResults.values()) {
			final Iterator<SwapValueMatrixResult> resultsIter = results.iterator();
			pairsForPermutations.add(getMatrixValuePair(resultsIter.next()));
			resultsIter.forEachRemaining(res -> extraPairs.add(getMatrixValuePair(res)));
		}

		// At most 6 groups => at most 720 permutations
		for (final PermutationIterator permIter = new PermutationIterator(groupedResults.size()); permIter.hasNext();) {
			final int[] nextPermutation = permIter.next();
			final List<Pair<Integer, Integer>> permutedPairs = new ArrayList<>();
			for (final int index : nextPermutation) {
				permutedPairs.add(pairsForPermutations.get(index));
			}
			extraPairs.forEach(permutedPairs::add);
			evaluator.accept(model, permutedPairs);
			final Triple<IndexStateContainer, IndexStateContainer, SwapValueMatrixResult[][]> triple = ValueMatrixTests.buildResultsMatrix(model);
			final SwapValueMatrixResult[][] thisMatrix = triple.getThird();
			int i = 0;
			for (final SwapValueMatrixResult[] thisRow : thisMatrix) {
				final SwapValueMatrixResult[] expectedRow = individualEvaluationMatrix[i];
				int j = 0;
				for (final SwapValueMatrixResult thisResult : thisRow) {
					final SwapValueMatrixResult expectedResult = expectedRow[j];
					checkEquality(thisResult, expectedResult);
					++j;
				}
				++i;
			}
		}
	}
}
