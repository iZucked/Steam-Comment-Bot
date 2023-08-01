/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.TierBlendASTNode;
import com.mmxlabs.common.parser.astnodes.TierBlendASTNode.ExprSelector;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class TierBlendFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final TierBlendASTNode tierBlendNode, final InputRecord inputRecord) {

		final Pair<Long, IExposureNode> baseNodeData = ExposuresASTToCalculator.getExposureNode(tierBlendNode.getTarget(), inputRecord);

		final double baseValue = baseNodeData.getFirst();
		final long threshold = Math.round(tierBlendNode.getThreshold().doubleValue() * Calculator.HighScaleFactor);

		final var selected = tierBlendNode.select(baseValue, threshold);
		if (selected == ExprSelector.LOW) {
			return ExposuresASTToCalculator.getExposureNode(tierBlendNode.getLowTier(), inputRecord);
		}

		// Convert to a new expression of "x%low + (1-x)%high" to make it easier to compute
		// x is the fraction of the total volume linked to the low tier.
		// Note: how can we handle < vs <= ? Will it make a difference?
		final double fraction = 100.0 * threshold / baseValue;
		final OperatorASTNode low = new OperatorASTNode(new ConstantASTNode(fraction), Operator.PERCENT, tierBlendNode.getLowTier());
		final OperatorASTNode high = new OperatorASTNode(new ConstantASTNode(100.0 - fraction), Operator.PERCENT, tierBlendNode.getHighTier());
		final OperatorASTNode node = new OperatorASTNode(low, Operator.PLUS, high);
		return ExposuresASTToCalculator.getExposureNode(node, inputRecord);
	}
}