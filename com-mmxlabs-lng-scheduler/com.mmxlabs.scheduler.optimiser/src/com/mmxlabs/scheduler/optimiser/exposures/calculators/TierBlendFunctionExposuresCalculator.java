/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.TierBlendASTNode;
import com.mmxlabs.common.parser.astnodes.TierBlendASTNode.ThresholdSeries;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class TierBlendFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final TierBlendASTNode tierBlendNode, final InputRecord inputRecord) {

		final Pair<Long, IExposureNode> baseNodeData = ExposuresASTToCalculator.getExposureNode(tierBlendNode.getTarget(), inputRecord);

		final double baseValue = baseNodeData.getFirst();

		final List<ThresholdSeries> thresholds = tierBlendNode.getThresholds();
		
		// Compute the fraction each band contributes to the full value.
		final List<OperatorASTNode> nodes = new LinkedList<>();
		long usedValue = 0L;
		for (final var t : thresholds) {
			final double contribValue;
			boolean breakLoop = false;
			// MAX_VALUE should be the final band.
			if (t.threshold().doubleValue() == Double.MAX_VALUE) {
				contribValue = baseValue - usedValue;
			} else {
				// Compute the used part of the band
				final long threshold = Math.round(t.threshold().doubleValue() * Calculator.HighScaleFactor);
				contribValue = Math.min(baseValue, threshold) - usedValue;
				usedValue = threshold;
				if (baseValue <= threshold) {
					// We have covered all the value and so there is no need to check further bands.
					breakLoop = true;
				}
			}
			final double fraction = 100.0 * contribValue / baseValue;
			final OperatorASTNode n = new OperatorASTNode(new ConstantASTNode(fraction), Operator.PERCENT, t.series());
			nodes.add(n);

			if (breakLoop) {
				// Terminate the loop
				break;
			}
		}
		assert !nodes.isEmpty();
		
		
		if (nodes.size() == 1) {
			// Only one option - 100%x - Unwrap and just return x
			return ExposuresASTToCalculator.getExposureNode(nodes.get(0).getRHS(), inputRecord);
		}

		// Create a new expression which is the sum of each fracation. This should return the full 100% of the value/volume (but can be affected by rounding) 
		OperatorASTNode node = null;
		for (final var n : nodes) {
			if (node == null) {
				node = n;
			} else {
				node = new OperatorASTNode(node, Operator.PLUS, n);
			}
		}
		assert node != null;

		return ExposuresASTToCalculator.getExposureNode(node, inputRecord);
	}
}
